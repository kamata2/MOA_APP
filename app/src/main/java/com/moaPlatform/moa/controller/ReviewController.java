package com.moaPlatform.moa.controller;

import android.content.Context;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.model.req_model.ReqReviewModel;
import com.moaPlatform.moa.model.req_model.ReqStoreInfoModel;
import com.moaPlatform.moa.model.res_model.ResReviewChangeScreen;
import com.moaPlatform.moa.model.res_model.ResReviewListModel;
import com.moaPlatform.moa.model.res_model.ResReviewModel;
import com.moaPlatform.moa.model.res_model.ReviewHeaderInfoModel;
import com.moaPlatform.moa.util.BaseController;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.interfaces.ServerSideMsg;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import androidx.annotation.NonNull;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 리뷰 컨트롤러
 */
public class ReviewController extends BaseController {

    public final int REQUEST_REVIEW_STORE = 1000;
    public final int REQUEST_REVIEW_EAT_OUT = 1001;
    public final int REQUEST_REVIEW_MODIFY = 1002;
    public final int REQUEST_REVIEW_DELETE = 1003;
    public final int REQUEST_REVIEW_DETAIL_INFO = 1004;

    public ReviewController(Context context) {
        super(context);
    }

    /**
     * 배달 리뷰 리스트 조회
     */
    public void getStoreReviewList(ReqStoreInfoModel model) {
        RetrofitClient.getInstance().getMoaService().getDeliveryReviewList(model).enqueue(new Callback<ResReviewListModel>() {

            @Override
            public void onResponse(@NonNull Call<ResReviewListModel> call, @NonNull Response<ResReviewListModel> response) {
                ResReviewListModel resReviewListModel = response.body();
                if (resReviewListModel == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(resReviewListModel)) {
                    getStoreReviewList(model);
                    return;
                }
                if(resReviewListModel.resultValue.equals(ServerSideMsg.SUCCESS)){
                    onRetrofitSuccess(REQUEST_REVIEW_STORE, resReviewListModel);
                }else{
                    onRetrofitFail(REQUEST_REVIEW_STORE, resReviewListModel.resultValue);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResReviewListModel> call, @NonNull Throwable t) {
                onRetrofitFail(REQUEST_REVIEW_STORE, t.toString());
            }
        });
    }

    /**
     * 외식 리뷰 리스트 조회
     * @param reqModel
     */
    public void getEatOutStoreReviewList(ReqStoreInfoModel reqModel) {
        RetrofitClient.getInstance().getMoaService().getEatOutStoreReviewList(reqModel).enqueue(new Callback<ResReviewListModel>() {
            @Override
            public void onResponse(@NonNull Call<ResReviewListModel> call, @NonNull Response<ResReviewListModel> response) {
                ResReviewListModel resReviewListModel = response.body();
                if (resReviewListModel == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(resReviewListModel)) {
                    getEatOutStoreReviewList(reqModel);
                    return;
                }
                if(resReviewListModel.resultValue.equals(ServerSideMsg.SUCCESS)){
                    onRetrofitSuccess(REQUEST_REVIEW_EAT_OUT, resReviewListModel);
                }else{
                    onRetrofitFail(REQUEST_REVIEW_EAT_OUT, resReviewListModel.resultValue);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResReviewListModel> call, @NonNull Throwable t) {
                onRetrofitFail(REQUEST_REVIEW_EAT_OUT, t.toString());
            }
        });
    }

    /**
     * 배달 리뷰 상세 정보
     * @param reviewId
     */
    public void getDeliveryReviewInfo(String reviewId){

        ReqReviewModel model = new ReqReviewModel();
        model.userRevwId = reviewId;

        RetrofitClient.getInstance().getMoaService().reviewChangeScreen(model).enqueue(new Callback<ResReviewChangeScreen>() {
            @Override
            public void onResponse(@NonNull Call<ResReviewChangeScreen> call, @NonNull Response<ResReviewChangeScreen> response) {
                ResReviewChangeScreen resReviewChangeScreen = response.body();
                if (resReviewChangeScreen == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(resReviewChangeScreen)) {
                    getDeliveryReviewInfo(reviewId);
                    return;
                }

                ReviewHeaderInfoModel reviewHeaderInfoModel = resReviewChangeScreen.reviewHeaderInfoModel;

                if(resReviewChangeScreen.resultValue.equals(ServerSideMsg.SUCCESS)){
                    onRetrofitSuccess(REQUEST_REVIEW_DETAIL_INFO, reviewHeaderInfoModel);
                }else{
                    onRetrofitFail(REQUEST_REVIEW_DETAIL_INFO, resReviewChangeScreen.resultValue);
                }
//                ReviewHeaderInfoModel reviewHeaderInfoModel = resReviewChangeScreen.reviewHeaderInfoModel;
//                initData(reviewHeaderInfoModel);
            }

            @Override
            public void onFailure(@NonNull Call<ResReviewChangeScreen> call, @NonNull Throwable t) {
                Logger.e("리뷰 수정 에러 : " + t.getMessage());
                onRetrofitFail(REQUEST_REVIEW_DETAIL_INFO, context.getString(R.string.common_toast_msg_connection_fail));
            }
        });
    }

    /**
     * 외식 리뷰 상세 정보
     * @param reviewId
     */
    public void getPlaceReviewInfo(String reviewId){

        ReqReviewModel model = new ReqReviewModel();
        model.userRevwId = reviewId;

        RetrofitClient.getInstance().getMoaService().reviewEatOut(model).enqueue(new Callback<ResReviewChangeScreen>() {
            @Override
            public void onResponse(@NonNull Call<ResReviewChangeScreen> call, @NonNull Response<ResReviewChangeScreen> response) {
                ResReviewChangeScreen resReviewChangeScreen = response.body();
                if (resReviewChangeScreen == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(resReviewChangeScreen)) {
                    getPlaceReviewInfo(reviewId);
                    return;
                }

                ReviewHeaderInfoModel reviewHeaderInfoModel = resReviewChangeScreen.reviewHeaderInfoModel;

                if(resReviewChangeScreen.resultValue.equals(ServerSideMsg.SUCCESS)){
                    onRetrofitSuccess(REQUEST_REVIEW_DETAIL_INFO, reviewHeaderInfoModel);
                }else{
                    onRetrofitFail(REQUEST_REVIEW_DETAIL_INFO, resReviewChangeScreen.resultValue);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResReviewChangeScreen> call, @NonNull Throwable t) {
                Logger.e("리뷰 수정 에러 : " + t.getMessage());
                onRetrofitFail(REQUEST_REVIEW_DETAIL_INFO, context.getString(R.string.common_toast_msg_connection_fail));
            }
        });
    }

    /**
     * 배달,외식
     * 리뷰 수정하기
     * @param requestBody
     */
    public void postReviewModify(MultipartBody requestBody) {

        RetrofitClient.getInstance().getMoaService().postReviewModify(requestBody).enqueue(new Callback<ResReviewModel>() {
            @Override
            public void onResponse(@NonNull Call<ResReviewModel> call, @NonNull Response<ResReviewModel> response) {
                ResReviewModel commonModel = response.body();
                if (commonModel == null) {
                    return;
                }
                if (RetrofitClient.getInstance().hasReissuedAccessToken(commonModel)) {
                    postReviewModify(requestBody);
                    return;
                }
                if (commonModel.resultValue.equals(ServerSideMsg.SUCCESS)) {
                    onRetrofitSuccess(REQUEST_REVIEW_MODIFY, commonModel);
                } else {
                    onRetrofitFail(REQUEST_REVIEW_MODIFY, context.getString(R.string.common_toast_msg_review_modify_fail));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResReviewModel> call, @NonNull Throwable t) {
                onRetrofitFail(REQUEST_REVIEW_MODIFY, context.getString(R.string.common_toast_msg_connection_fail));
            }
        });
    }

    /**
     * 리뷰 삭제
     * @param model
     */
    public void deleteReview(ReqReviewModel model) {

        RetrofitClient.getInstance().getMoaService().reviewDelete(model).enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {
                CommonModel commonModel = response.body();
                if (commonModel == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(commonModel)) {
                    deleteReview(model);
                    return;
                }
                if(commonModel.resultValue.equals(ServerSideMsg.SUCCESS)){
                    onRetrofitSuccess(REQUEST_REVIEW_DELETE, null);
                }else{
                    onRetrofitFail(REQUEST_REVIEW_DELETE, context.getString(R.string.common_toast_msg_review_modify_fail));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {
                onRetrofitFail(REQUEST_REVIEW_DELETE, t.toString());
            }
        });
    }
}
