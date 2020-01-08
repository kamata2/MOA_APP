package com.moaPlatform.moa.delivery_menu.store_detail;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.constants.EatOutConstants;
import com.moaPlatform.moa.delivery_menu.eatout_store_detail.EatOutStoreDaumMapActivity;
import com.moaPlatform.moa.delivery_menu.eatout_store_detail.model.EatOutStoreInfoModel;
import com.moaPlatform.moa.delivery_menu.store_detail.model.ResStoreDetailInfo;
import com.moaPlatform.moa.delivery_menu.store_detail.model.StoreAddInfoViewModel;
import com.moaPlatform.moa.model.req_model.ReqStoreInfoModel;
import com.moaPlatform.moa.util.ObjectUtil;
import com.moaPlatform.moa.util.StringUtil;
import com.moaPlatform.moa.util.dialog.yesOrNo.YesOrNoDialog;
import com.moaPlatform.moa.util.dialog.yesOrNo.YesOrNoDialogFragmentListener;
import com.moaPlatform.moa.util.interfaces.RetrofitConnectionResult;
import com.moaPlatform.moa.util.interfaces.ViewDataInitHelper;
import com.moaPlatform.moa.util.manager.KakaoApiManager;
import com.moaPlatform.moa.util.models.BaseModel;
import com.moaPlatform.moa.util.singleton.App;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import static com.moaPlatform.moa.delivery_menu.eatout_store_detail.EatOutStoreDaumMapActivity.EXTRA_EATOUT_STORE_DAUM_MAP;

/**
 * 가맹점 부가 정보 표시해줄 fragment
 */
public class StoreAdditionalInformationFragment extends Fragment implements ViewDataInitHelper, LocationListener {

    private View view;
    private ReqStoreInfoModel reqStoreInfoModel;
    private String daumMapMoveType;
    private LocationManager locationManager;
    private String lont, lata;

    private StoreAddInfoViewModel storeAddInfoViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.store_additional_information_fragment, container, false);
        init();
        return view;
    }

    /**
     * 초기화
     */
    private void init() {
        storeAddInfoViewModel = ViewModelProviders.of(this).get(StoreAddInfoViewModel.class);
        storeAddInfoViewModel.getStoreAddInfoModel().observe(this, this::addInfoInit);
        getAdditionalInfo(reqStoreInfoModel);
    }

    /**
     * 가맹점 부가 정보 받아오기
     */
    private void getAdditionalInfo(ReqStoreInfoModel reqModel) {
        StoreDetailServerController serverController = new StoreDetailServerController(view.getContext());
        serverController.getStoreAdditionalInfo(reqModel);

        serverController.setRetrofitConnectionResult(new RetrofitConnectionResult() {
            @Override
            public void onRetrofitSuccess(int type, BaseModel baseModel) {
                storeAddInfoViewModel.setStoreAddInfoModel(((ResStoreDetailInfo) baseModel).storeInfoModel);
            }

            @Override
            public void onRetrofitFail(int type, String msg) {
            }
        });
    }

    private void addInfoInit(ResStoreDetailInfo.StoreAddInfoModel storeAddInfoModel) {
        if (storeAddInfoModel == null)
            return;
        if (view == null)
            return;

        ViewDataInitHelper viewDataInitHelper = this;
        if (getActivity() != null && !getActivity().isFinishing() && isAdded()) {
            try {
                //TODO JIWON : null 체크를 꼼꼼하게 할 필요성이 있음..
                // 해당메뉴 서버에서 데이터를 실수로 안내리는 경우에 앱정지가 빈번하게 발생됨

                // 매장 안내 문구
                TextView tvStoreAddInformationStoreGuide = view.findViewById(R.id.tvStoreAddInformationStoreGuide);
                if (storeAddInfoModel.storeInfo == null || storeAddInfoModel.storeInfo.equals("")) {
                    // 매장 안내 문구가 없을시
                    String storeGuideText = getString(R.string.fragment_store_additional_information_store_info_empty, storeAddInfoModel.storeName);
                    tvStoreAddInformationStoreGuide.setText(storeGuideText);
                } else {
                    tvStoreAddInformationStoreGuide.setText(storeAddInfoModel.storeInfo);
                }

                // 이벤트 및 혜택 안내
                TextView tvStoreEvent = view.findViewById(R.id.tvStoreEvent);
                if (storeAddInfoModel.eventInfo == null || storeAddInfoModel.eventInfo.equals("")) {
                    // 매장 이벤트 및 혜택 문구가 없을 시
                    String tvStoreEventText = getString(R.string.fragment_store_additional_information_store_event_empty);
                    tvStoreEvent.setText(tvStoreEventText);
                } else {
                    tvStoreEvent.setText(storeAddInfoModel.eventInfo);
                }

                // 영업 정보
                // 영업 시간
                if (ObjectUtil.checkNotNull(storeAddInfoModel.openTime) && ObjectUtil.checkNotNull(storeAddInfoModel.closeTime)) {
                    TextView tvBusinessHoursTime = view.findViewById(R.id.tvBusinessHoursTime);
                    tvBusinessHoursTime.setText(view.getResources().getString(R.string.store_business_hours,
                            storeAddInfoModel.openTime.substring(0, 2),
                            storeAddInfoModel.openTime.substring(2, 4),
                            storeAddInfoModel.closeTime.substring(0, 2),
                            storeAddInfoModel.closeTime.substring(2, 4)));
                }

                // 휴무일
                if (ObjectUtil.checkNotNull(storeAddInfoModel.holyDay)) {
                    TextView tvClosedDays = view.findViewById(R.id.tvClosedDays);
                    tvClosedDays.setText(storeAddInfoModel.holyDay);
                }

                // 부가 정보
                TextView tvAdditionalInformation = view.findViewById(R.id.tvAdditionalInformation);
                if (ObjectUtil.checkNotNull(storeAddInfoModel.addInfo)) {
                    tvAdditionalInformation.setText(storeAddInfoModel.addInfo);
                } else {
                    // 데이터가 없을 경우
                    tvAdditionalInformation.setText("부가 정보가 없습니다.");
                }

                // 배달 가능 지역
                TextView tvDeliveryArea = view.findViewById(R.id.tvDeliveryArea);
                if (ObjectUtil.checkNotNull(storeAddInfoModel.deliveryArea)) {
                    tvDeliveryArea.setText(storeAddInfoModel.deliveryArea);
                } else {
                    // 데이터가 없을 경우
                    tvDeliveryArea.setText("전화 문의");
                }

                // 배달비
                TextView tvDeliveryPrice = view.findViewById(R.id.tvDeliveryPrice);
                String deliveryPriceText;
                if (ObjectUtil.checkNotNull(storeAddInfoModel.deliveryPrice)) {
                    if (storeAddInfoModel.deliveryPrice.equals("0")) {
                        deliveryPriceText = getString(R.string.activity_store_detail_call_enquiry);
                    } else {
                        String deliveryPrice = StringUtil.convertCommaPrice(storeAddInfoModel.deliveryPrice);
                        deliveryPriceText = getString(R.string.store_product_total_money, deliveryPrice);
                    }
                    tvDeliveryPrice.setText(deliveryPriceText);
                }

                // 사업자 정보
                // 상호명
                if (ObjectUtil.checkNotNull(storeAddInfoModel.storeName)) {
                    TextView tvStoreName = view.findViewById(R.id.tvStoreName);
                    tvStoreName.setText(storeAddInfoModel.storeName);
                }

                // 사업자등로번호
                if (ObjectUtil.checkNotNull(storeAddInfoModel.businessLicenseNumber)) {
                    TextView tvBusinessLicenseNumber = view.findViewById(R.id.tvBusinessLicenseNumber);
                    tvBusinessLicenseNumber.setText(storeAddInfoModel.businessLicenseNumber);
                }

                // 원산지 정보
                TextView tvCountryOfOriginInformation = view.findViewById(R.id.tvCountryOfOriginInformation);
                if (ObjectUtil.checkNotNull(storeAddInfoModel.origin)) {
                    tvCountryOfOriginInformation.setText(storeAddInfoModel.origin);
                } else {
                    String countryOfOriginInformationText = getString(R.string.fragment_store_additional_information_store_country_of_origin_empty);
                    tvCountryOfOriginInformation.setText(countryOfOriginInformationText);
                }

                if (storeAddInfoModel.getStoreMapImg() == null) {
                    addressMapHide();
                } else {
                    // 위도 경도 세팅
                    lont = storeAddInfoModel.getLont();
                    lata = storeAddInfoModel.getLatu();
                    // 지도 표시
                    ImageView ivStoreMap = view.findViewById(R.id.ivStoreMap);
                    Glide.with(view)
                            .load(StringUtil.getImageUrl(storeAddInfoModel.getStoreMapImg()))
                            .into(ivStoreMap);
                    ivStoreMap.setOnClickListener(v -> {
                        EatOutStoreInfoModel eatOutStoreInfoModel = new EatOutStoreInfoModel();
                        eatOutStoreInfoModel.storNm = storeAddInfoModel.storeName;
                        eatOutStoreInfoModel.latu = storeAddInfoModel.getLatu();
                        eatOutStoreInfoModel.lont = storeAddInfoModel.getLont();
                        eatOutStoreInfoModel.roadNmDefltAddr = storeAddInfoModel.getRoeadNameAddress();
                        //지도 보기
                        Intent intentMap = new Intent(view.getContext(), EatOutStoreDaumMapActivity.class);
                        intentMap.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intentMap.putExtra(EXTRA_EATOUT_STORE_DAUM_MAP, App.getInstance().gson.toJson(eatOutStoreInfoModel));
                        intentMap.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intentMap);
                    });
                    // 주소 표시
                    String roadAddress = storeAddInfoModel.getRoeadNameAddress();
                    TextView tvAddress = view.findViewById(R.id.tvAddress);
                    tvAddress.setText(roadAddress);
                    // 길찾기 클릭 시
                    View rlEatOutStoreDetailSearchWalk = view.findViewById(R.id.rlEatOutStoreDetailSearchWalk);
                    rlEatOutStoreDetailSearchWalk.setOnClickListener(v -> {
                        //네비게이션
                        daumMapMoveType = EatOutConstants.SEARCH_MAP_TYPE.FOOT.getType();
                        locationPermissionCheck();
                    });
                    // 네비게이션 클릭 시
                    View rlEatOutStoreDetailSearchNavi = view.findViewById(R.id.rlEatOutStoreDetailSearchNavi);
                    rlEatOutStoreDetailSearchNavi.setOnClickListener(v -> {
                        daumMapMoveType = EatOutConstants.SEARCH_MAP_TYPE.CAR.getType();
                        locationPermissionCheck();
                    });
                    // 주소 복사 클릭 시
                    View rlEatOutStoreDetailSearchCopyAddress = view.findViewById(R.id.rlEatOutStoreDetailSearchCopyAddress);
                    rlEatOutStoreDetailSearchCopyAddress.setOnClickListener(v -> {
                        if (roadAddress != null) {
                            int sdk = android.os.Build.VERSION.SDK_INT;
                            if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                                clipboard.setText(roadAddress);
                            } else {
                                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("address", roadAddress);
                                clipboard.setPrimaryClip(clip);
                            }
                            Toast.makeText(view.getContext(), getString(R.string.copy_clip_baord), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void setReqStoreInfoModel(ReqStoreInfoModel reqStoreInfoModel) {
        this.reqStoreInfoModel = reqStoreInfoModel;
    }

    /**
     * 주소 지도 숨기기
     */
    void addressMapHide() {
        View viewTakeOutMap = view.findViewById(R.id.viewTakeOutMap);
        viewTakeOutMap.setVisibility(View.GONE);
    }

    /**
     * 위치 퍼미션 체크
     */
    private void locationPermissionCheck() {
        TedPermission.with(view.getContext()).setPermissionListener(new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                nowLocationSearchInit();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
            }
        }).setDeniedMessage(R.string.location_permission_denied)
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION).check();
    }

    /**
     * 현재 위치를 가져온다.
     */
    @SuppressLint("MissingPermission")
    private void nowLocationSearchInit() {

        locationManager = (LocationManager) view.getContext().getSystemService(Context.LOCATION_SERVICE);

        if ((locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1, this);
        } else {
            YesOrNoDialog gpsOnDialog = new YesOrNoDialog();
            gpsOnDialog.dialogContent("현재 위치 검색을 위해 위치 설정 기능이 필요합니다.");
            gpsOnDialog.yesTextChange("설정하러 가기");
            gpsOnDialog.showAllowingStateLoss(getFragmentManager(), "gpsOnDialog");
            gpsOnDialog.setYesOrNoDialogListener(new YesOrNoDialogFragmentListener() {
                @Override
                public void onDialogNo() {
                    gpsOnDialog.dismiss();
                }

                @Override
                public void onDialogYes() {
                    gpsOnDialog.dismiss();
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });


        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
        KakaoApiManager.getInstance().showDaumMap(view.getContext(), daumMapMoveType, location.getLatitude(), location.getLongitude()
                , lata, lont);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        if (!getActivity().isFinishing()) {
            // 위치 정보 가져왔을시 network 문제시에는 에러 토스를 띄우고 리스너 연결을 끊음
            if (provider.equals("network")) {
                Toast.makeText(view.getContext(), "연결 상태가 일시적으로 불안합니다. 다시 시도해 주세요 : " + provider, Toast.LENGTH_LONG).show();
                locationManager.removeUpdates(this);
            }
        }
    }

}
