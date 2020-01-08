package com.moaPlatform.moa.top_menu.location;

import android.content.Context;

import com.google.gson.Gson;
import com.moaPlatform.moa.top_menu.location.model.ReqAddressSave;
import com.moaPlatform.moa.top_menu.location.model.ReqAddressSearchModel;
import com.moaPlatform.moa.top_menu.location.model.ReqLocationXySearchModel;
import com.moaPlatform.moa.top_menu.location.model.ResAddressSearchBaseModel;
import com.moaPlatform.moa.util.BaseController;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.manager.CodeTypeManager;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationServerController extends BaseController {

    public final int API_ADDRESS_HISTORY = 1000;
    public final int API_ADDRESS_REMOVE = 2000;
    public final int API_COORDINATES_TRANSFORM = 3000;
    public final int API_ADDRESS_SEARCH = 4000;
    public final int API_ADDRESS_SAVE = 5000;
    public final int API_DEFAULT_ADDRESS_CHANGE = 6000;

    public LocationServerController(Context context) {
        super(context);
    }

    /**
     * 주소의 xy 좌표를 받아옴
     *
     * @param reqModel req 모델
     */
    public void onLocationSearchXy(ReqLocationXySearchModel reqModel) {
        RetrofitClient.getInstance().getMoaAddressService()
                .locationSearchXy(reqModel.key, reqModel.admCd, reqModel.rnMgtSn, reqModel.isUnderground, reqModel.buildingNumber, reqModel.buildingSubCode, reqModel.resultType)
                .enqueue(new Callback<ResAddressSearchBaseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<ResAddressSearchBaseModel> call, @NonNull Response<ResAddressSearchBaseModel> response) {
                        if (response.body() != null) {
                            Logger.i(new Gson().toJson(response.body().getAddressModel()));
                            try {
                                coordinatesTransform(response.body().getAddressModel().get(0));
                            } catch (Exception e) {
                                Logger.e(e.toString());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResAddressSearchBaseModel> call, @NonNull Throwable t) {
                        Logger.e(t.toString());
                    }
                });
    }

    /**
     * 위치 좌표 변환
     */
    private void coordinatesTransform(ResAddressSearchBaseModel.AddressModel reqModel) {
        RetrofitClient.getInstance().getMoaService().coordinatesTransform(reqModel)
                .enqueue(new Callback<ResAddressSearchBaseModel.LocationCoordinate>() {
                    @Override
                    public void onResponse(@NonNull Call<ResAddressSearchBaseModel.LocationCoordinate> call, @NonNull Response<ResAddressSearchBaseModel.LocationCoordinate> response) {
                        ResAddressSearchBaseModel.LocationCoordinate locationCoordinate = response.body();
                        if (locationCoordinate == null) {
                            onRetrofitFail(API_COORDINATES_TRANSFORM, "coordinatesTransform");
                            return;
                        }
                        if (RetrofitClient.getInstance().hasReissuedAccessToken(locationCoordinate)) {
                            coordinatesTransform(reqModel);
                            return;
                        }
                        onRetrofitSuccess(API_COORDINATES_TRANSFORM, locationCoordinate);

                    }


                    @Override
                    public void onFailure(@NonNull Call<ResAddressSearchBaseModel.LocationCoordinate> call, @NonNull Throwable t) {
                        onRetrofitFail(API_COORDINATES_TRANSFORM, "coordinatesTransform");
                        Logger.e("coordinatesTransform onFailure");
                        t.printStackTrace();
                    }
                });
    }

    /**
     * AddressDataSource에서 사용하는 서버 통신 api와 통합 예정
     *
     * @param reqModel req 모델
     */
    void onAddressSearch(ReqAddressSearchModel reqModel) {
        RetrofitClient.getInstance().getMoaAddressService()
                .addressSearch(reqModel.key, reqModel.currentPage, reqModel.countPerPage, reqModel.keyword, reqModel.resultType)
                .enqueue(new Callback<ResAddressSearchBaseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<ResAddressSearchBaseModel> call, @NonNull Response<ResAddressSearchBaseModel> response) {
                        if (response.body() != null) {
                            Logger.toJson("addressSearch : ", response.body());
                            try {
                                onRetrofitSuccess(API_ADDRESS_SEARCH, response.body().getAddressModel().get(0));
                            } catch (Exception e) {
                                Logger.e(e.toString());
                                onRetrofitFail(API_ADDRESS_SEARCH, "onAddressSearch");
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResAddressSearchBaseModel> call, @NonNull Throwable t) {
                        Logger.e("addressSearch onFailure");
                        serverErrorMsgShow();
                        onRetrofitFail(API_ADDRESS_SEARCH, "onAddressSearch");
                        t.printStackTrace();
                    }
                });
    }

    /**
     * 서버에 주소 저장 하는 통신
     *
     * @param reqAddressSave req 모델
     */
    void addressSave(ReqAddressSave reqAddressSave) {
        RetrofitClient.getInstance().getMoaService().addressSave(reqAddressSave).enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {
                CommonModel commonModel = response.body();
//                if (commonModel == null)
//                    return;
//                if (RetrofitClient.getInstance().hasReissuedAccessToken(commonModel)) {
//                    addressSave(reqAddressSave);
//                    return;
//                }
//                onRetrofitSuccess(API_ADDRESS_SAVE, commonModel);

                switch (resModelCheck(commonModel)) {
                    case MODEL_NULL:
                        serverErrorMsgShow();
                        onRetrofitFail(API_ADDRESS_SAVE, "addressSave");
                        break;
                    case RESPONSE_DATA_FAIL:
                        serverErrorMsgShow();
                        onRetrofitFail(API_ADDRESS_SAVE, "addressSave");
                        break;
                    case MODEL_ACCESS_TOKEN_REFRESH:
                        addressSave(reqAddressSave);
                        break;
                    case RESPONSE_DATA_SUCCESS:
                        onRetrofitSuccess(API_ADDRESS_SAVE, commonModel);
                        break;
                }

            }

            @Override
            public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {
                serverErrorMsgShow();
                onRetrofitFail(API_ADDRESS_SAVE, "addressSave");
                Logger.e("addressSave onFailure");
                t.printStackTrace();
            }
        });
    }

    /**
     * 기본 및 최근 주소 조회
     */
    @Deprecated
    void callGetAddressHistoryList() {
        RetrofitClient.getInstance().getMoaService().addressHistory(new CommonModel()).enqueue(new Callback<ResAddressSearchBaseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResAddressSearchBaseModel> call, @NonNull Response<ResAddressSearchBaseModel> response) {
                ResAddressSearchBaseModel resAddressSearchBaseModel = response.body();
                if (resAddressSearchBaseModel == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(resAddressSearchBaseModel)) {
                    callGetAddressHistoryList();
                    return;
                }

                if (restApiResult != null)
                    restApiResult.onRestApiSuccess(CodeTypeManager.RestApi.LOAD_ADDRESS_HISTORY, resAddressSearchBaseModel);

            }

            @Override
            public void onFailure(@NonNull Call<ResAddressSearchBaseModel> call, @NonNull Throwable t) {
                restApiResult.onRestApiFail(CodeTypeManager.RestApi.LOAD_ADDRESS_HISTORY);
                Logger.e("callGetAddressHistoryList : " + t);
            }
        });
    }

    void onAddressHistory() {
        RetrofitClient.getInstance().getMoaService().addressHistory(new CommonModel()).enqueue(new Callback<ResAddressSearchBaseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResAddressSearchBaseModel> call, @NonNull Response<ResAddressSearchBaseModel> response) {

                switch (resModelCheck(response.body())) {
                    case MODEL_NULL:
                        serverErrorMsgShow();
                        onRetrofitFail(API_ADDRESS_HISTORY, "onAddressHistory");
                        break;
                    case RESPONSE_DATA_FAIL:
                        serverErrorMsgShow();
                        onRetrofitFail(API_ADDRESS_HISTORY, "onAddressHistory");
                        break;
                    case MODEL_ACCESS_TOKEN_REFRESH:
                        onAddressHistory();
                        break;
                    case RESPONSE_DATA_SUCCESS:
                        Logger.toJson("onAddressHistory", response.body());
                        onRetrofitSuccess(API_ADDRESS_HISTORY, response.body());
                        break;
                }

            }

            @Override
            public void onFailure(@NonNull Call<ResAddressSearchBaseModel> call, @NonNull Throwable t) {
//                restApiResult.onRestApiFail(CodeTypeManager.RestApi.LOAD_ADDRESS_HISTORY);
                Logger.e("callGetAddressHistoryList : " + t);
                onRetrofitFail(API_ADDRESS_HISTORY, "onAddressHistory");
                serverErrorMsgShow();
            }
        });
    }

    /**
     * 주소 삭제
     */
    void onAddressRemove(ResAddressSearchBaseModel.AddressHistoryModel addressHistoryModel) {
        Logger.i("removeAddress req : " + gson.toJson(addressHistoryModel));
        RetrofitClient.getInstance().getMoaService().removeAddress(addressHistoryModel).enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {

                switch (resModelCheck("onAddressRemove", response.body())) {
                    case RESPONSE_DATA_SUCCESS:
                        onRetrofitSuccess(API_ADDRESS_REMOVE, response.body());
                        break;
                    case MODEL_ACCESS_TOKEN_REFRESH:
                        onAddressRemove(addressHistoryModel);
                        break;
                    case RESPONSE_DATA_FAIL:
                        serverErrorMsgShow();
                        onRetrofitFail(API_ADDRESS_REMOVE, "onAddressRemove");
                        break;
                    case MODEL_NULL:
                        serverErrorMsgShow();
                        onRetrofitFail(API_ADDRESS_REMOVE, "onAddressRemove");
                        break;
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {
                Logger.e("onAddressRemove : " + t);
                onRetrofitFail(API_ADDRESS_REMOVE, "onAddressRemove");
                serverErrorMsgShow();
            }
        });
    }

    /**
     * 주소 삭제
     */
    @Deprecated
    void removeAddress(ResAddressSearchBaseModel.AddressHistoryModel addressHistoryModel) {
        Logger.i("removeAddress req : " + gson.toJson(addressHistoryModel));
        RetrofitClient.getInstance().getMoaService().removeAddress(addressHistoryModel).enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {
                CommonModel commonModel = response.body();
                if (commonModel == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(commonModel)) {
                    removeAddress(addressHistoryModel);
                    return;
                }
                if (httpConnectionResult != null)
                    httpConnectionResult.onHttpConnectionSuccess(CodeTypeManager.AddressManager.ADDRESS_REMOVE.getType(), commonModel);
            }

            @Override
            public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {
                Logger.e("removeAddress : " + t);
            }
        });
    }

    /**
     * 기본 주소 등록
     */
    void defaultAddressChange(ResAddressSearchBaseModel.AddressHistoryModel addressHistoryModel) {
        Logger.i("removeAddress req : " + gson.toJson(addressHistoryModel));
        RetrofitClient.getInstance().getMoaService().defaultAddressChange(addressHistoryModel).enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {
//                CommonModel commonModel = response.body();
//                if (commonModel == null)
//                    return;
//                if (RetrofitClient.getInstance().hasReissuedAccessToken(commonModel)) {
//                    defaultAddressChange(addressHistoryModel);
//                    return;
//                }
//
//                if (httpConnectionResult != null)
//                    httpConnectionResult.onHttpConnectionSuccess(CodeTypeManager.AddressManager.DEFAULT_ADDRESS_CHANGE.getType(), commonModel);

                switch (resModelCheck(response.body())) {
                    case RESPONSE_DATA_SUCCESS:
                        onRetrofitSuccess(API_DEFAULT_ADDRESS_CHANGE, response.body());
                        break;
                    case MODEL_ACCESS_TOKEN_REFRESH:
                        defaultAddressChange(addressHistoryModel);
                        break;
                    case RESPONSE_DATA_FAIL:
                        serverErrorMsgShow();
                        onRetrofitFail(API_DEFAULT_ADDRESS_CHANGE, "defaultAddressChange");
                        break;
                    case MODEL_NULL:
                        serverErrorMsgShow();
                        onRetrofitFail(API_DEFAULT_ADDRESS_CHANGE, "defaultAddressChange");
                        break;
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {
                Logger.e("removeAddress : " + t);
                serverErrorMsgShow();
                onRetrofitFail(API_DEFAULT_ADDRESS_CHANGE, "defaultAddressChange");
            }
        });
    }

}
