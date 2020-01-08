package com.moaPlatform.moa.top_menu.location;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.top_menu.location.model.ReqAddressSave;
import com.moaPlatform.moa.top_menu.location.model.ReqAddressSearchModel;
import com.moaPlatform.moa.top_menu.location.model.ResAddressSearchBaseModel;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.ObjectUtil;
import com.moaPlatform.moa.util.interfaces.RetrofitConnectionResult;
import com.moaPlatform.moa.util.models.BaseModel;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DetailAddressFragment extends Fragment implements MapView.MapViewEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener {

    ResAddressSearchBaseModel.LocationCoordinate locationCoordinateModel;
    private ResAddressSearchBaseModel.AddressModel addressModel;
    private Unbinder unbinder;
    private View view;
    private LocationServerController locationServerController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.detail_location_setting_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    public void setLocationCoordinateModel(ResAddressSearchBaseModel.LocationCoordinate locationCoordinateModel) {
        this.locationCoordinateModel = locationCoordinateModel;
        daumMapInit();
    }

    private void init() {
        ((LocationSettingActivity)getActivity()).animationStart();
        locationServerController = new LocationServerController(getContext());
        locationServerController.setRetrofitConnectionResult(new RetrofitConnectionResult() {
            @Override
            public void onRetrofitSuccess(int type, BaseModel baseModel) {
                if (type == locationServerController.API_ADDRESS_SEARCH) {
                    addressSetting((ResAddressSearchBaseModel.AddressModel) baseModel);
                } else if (type == locationServerController.API_ADDRESS_SAVE) {
                    if (getActivity() != null && getActivity() instanceof LocationSettingActivity) {
                        ((LocationSettingActivity)getActivity()).addressSaveInit();
                    }
                }
            }

            @Override
            public void onRetrofitFail(int type, String msg) {
                if (type == locationServerController.API_ADDRESS_SEARCH) {
                    addressSetting(null);
                }
            }
        });
    }

    /**
     * 다음 지도 세팅
     */
    private void daumMapInit() {
        try {
            MapView mapView = new MapView(Objects.requireNonNull(getActivity()));
            mapView.setZoomLevel(2, true);
            mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(locationCoordinateModel.lat, locationCoordinateModel.lnt), true);
            FrameLayout maps = view.findViewById(R.id.mapView);
            maps.addView(mapView);
            mapView.setMapViewEventListener(this);
        } catch (Exception e) {
            Logger.i(e.toString());
        }
    }

    /**
     * 디테일 화면 주소 세팅
     *
     * @param addressModel 주소 정보를 담고 있는 모델
     */
    void addressSetting(ResAddressSearchBaseModel.AddressModel addressModel) {
        TextView roadAddress = view.findViewById(R.id.roadAddress);
        TextView jibunAddress = view.findViewById(R.id.jibunAddress);
        this.addressModel = addressModel;

        if (addressModel != null) {
            Logger.i(new Gson().toJson(addressModel));
            roadAddress.setText(addressModel.fullRoadAddress);
            jibunAddress.setText(getResources().getString(R.string.location_setting_address_item_jibun, addressModel.jibunAddress));
        } else {
            roadAddress.setText("");
            jibunAddress.setText("");
        }
        ((LocationSettingActivity)getActivity()).animationStop();
    }

    /**
     * 주소 설정 버튼 클릭 시
     */
    @OnClick(R.id.btnAddressDone)
    void addressDone() {
        // 사용자가 입력 한 상세 주소 정보를 불러옴
        EditText inputDetailAddress = view.findViewById(R.id.inputDetailAddress);
        String detailAddress = inputDetailAddress.getText().toString();
        if (ObjectUtil.checkNotNull(addressModel) && ObjectUtil.checkNotNull(locationCoordinateModel)) {
            // 서버에 주소 저장
            ReqAddressSave reqAddressSave = new ReqAddressSave();
            reqAddressSave.init(detailAddress, addressModel, locationCoordinateModel);
            locationServerController.addressSave(reqAddressSave);
//            ((LocationSettingActivity) Objects.requireNonNull(getActivity())).serverController.addressSave(reqAddressSave);
        } else if (!ObjectUtil.checkNotNull(detailAddress)) {
            // 상세 주소 입력이 비어있을경우
            Toast.makeText(getContext(), getString(R.string.detail_address_fragment_detail_address_empty), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 지도 드래그시
     *
     * @param mapView 맵뷰
     */
    @Override
    public void onMapViewInitialized(MapView mapView) {
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {
    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
        try {
            ApplicationInfo appInfo = view.getContext().getPackageManager().getApplicationInfo(view.getContext().getPackageName(), PackageManager.GET_META_DATA);
            String kakaoKey = appInfo.metaData.getString("com.kakao.sdk.AppKey");
            if (kakaoKey != null && !kakaoKey.isEmpty()) {
                MapReverseGeoCoder mapReverseGeoCoder = new MapReverseGeoCoder(kakaoKey, mapPoint, this, getActivity());
                mapReverseGeoCoder.startFindingAddress();
            }
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e(e.toString());
        }
    }


    @Override
    public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder mapReverseGeoCoder, String s) {
        try {
            ReqAddressSearchModel reqModel = new ReqAddressSearchModel();
            reqModel.keyword = s;
            locationServerController.onAddressSearch(reqModel);
//            ((LocationSettingActivity) Objects.requireNonNull(getActivity())).addressSearchServerConnect(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
