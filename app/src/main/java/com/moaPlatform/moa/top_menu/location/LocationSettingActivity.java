package com.moaPlatform.moa.top_menu.location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.bottom_menu.main.MainActivity;
import com.moaPlatform.moa.intro.db.IntroCheckModel;
import com.moaPlatform.moa.top_menu.location.model.ResAddressSearchBaseModel;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.StringUtil;
import com.moaPlatform.moa.util.custom_view.CommonLoadingView;
import com.moaPlatform.moa.util.db.RealmController;
import com.moaPlatform.moa.util.dialog.yesOrNo.YesOrNoDialog;
import com.moaPlatform.moa.util.dialog.yesOrNo.YesOrNoDialogFragmentListener;
import com.moaPlatform.moa.util.interfaces.CodeListener;
import com.moaPlatform.moa.util.interfaces.HttpConnectionResult;
import com.moaPlatform.moa.util.interfaces.RestApiResult;
import com.moaPlatform.moa.util.interfaces.TextSettingHtmlFormat;
import com.moaPlatform.moa.util.manager.CodeTypeManager;
import com.moaPlatform.moa.util.models.BaseModel;
import com.moaPlatform.moa.util.toolbar.TopToolbarController;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import io.realm.Realm;

public class LocationSettingActivity extends AppCompatActivity implements HttpConnectionResult, TextSettingHtmlFormat, CodeListener, LocationListener, RestApiResult {

    private FragmentManager fragmentManager;
    // 기본 주소 및 최근주소, 주소 검색 리스트 표시해주는 뷰의 테그
    private final String TAG_ADDRESS_LIST = "addressListTag";
    // 상세 주소와 지도를 표시해주는 뷰의 테그
    private final String TAG_DETAIL_ADDRESS = "detailAddressTag";

    // 주소 입력 화면
    private EditText etAddressListKeywordInput;

    // 주소 리스트 나오는 프레그먼트
    AddressListFragment addressListFragment;
    // 상세 주소 설정하는 프레그먼트
    private DetailAddressFragment detailAddressSettingFragment;
    // 로딩뷰
    private CommonLoadingView viewLocationSettingLoading;
    // 상단 툴바 컨트롤러
    private TopToolbarController topToolbarController;

    // 0 : 기본 주소 나오는 화면, 1 : 주소 검색 리스트, 2 : 상세 주소 설정
    public int fragmentType = 0;

    private LocationManager locationManager;
    private Realm realm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_setting_activity);
//        ButterKnife.bind(this);
//        setLocationToolbar();
        init();
    }

    /**
     * 초기회
     */
    private void init() {

        // 상단 툴바 세팅
        View topToolbar = findViewById(R.id.viewLocationSettingToolbar);
        topToolbarController = new TopToolbarController(topToolbar);
        topToolbarController.setTopToolbarClickListener(this);
        topToolbarController.defaultAddressToolbarInit();

        fragmentInit();

        // 주소 입력 창
        etAddressListKeywordInput = findViewById(R.id.etAddressListKeywordInput);
        etAddressListKeywordInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                onAddressSearch(etAddressListKeywordInput.getText().toString());
            }
            return false;
        });

        // 주소 검색 뷰
        View clAddressListKeywordSearchGroup = findViewById(R.id.clAddressListKeywordSearchGroup);
        clAddressListKeywordSearchGroup.setOnClickListener(v -> onAddressSearch(etAddressListKeywordInput.getText().toString()));

        Realm.init(this);
        RealmController realmController = new RealmController();
        realm = realmController.realmInstance();
        IntroCheckModel introCheckModel = realm.where(IntroCheckModel.class).findFirst();

        viewLocationSettingLoading = findViewById(R.id.viewLocationSettingLoading);

    }

    /**
     * 주소 검색
     *
     * @param addressKeyword
     */
    private void onAddressSearch(String addressKeyword) {

        // 공백인지 체크
        if (addressKeyword.trim().equals("")) {
            return;
        }

        //특수문자 입력시 토스트 노출 처리
        if (StringUtil.isContainSpecialCharacter(addressKeyword)) {
            Toast.makeText(this, getString(R.string.common_toast_msg_cannot_contain_special_characters), Toast.LENGTH_SHORT).show();
            return;
        }

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etAddressListKeywordInput.getWindowToken(), 0);

        if (addressListFragment != null) {
            fragmentType = 1;
            addressListFragment.addressSearch(addressKeyword);
        }

    }

    /**
     * 프레그먼트 초기화
     */
    private void fragmentInit() {
        fragmentManager = getSupportFragmentManager();
        addressListFragment = new AddressListFragment();
        // 주세 설정하는 프레그먼트
        detailAddressSettingFragment = new DetailAddressFragment();
        fragmentManager.beginTransaction()
                .add(R.id.flLocationSetting, addressListFragment, TAG_ADDRESS_LIST)
                .add(R.id.flLocationSetting, detailAddressSettingFragment, TAG_DETAIL_ADDRESS)
                .show(addressListFragment)
                .hide(detailAddressSettingFragment)
                .commit();
    }

    public void detailLocationFragmentShow(ResAddressSearchBaseModel.LocationCoordinate locationCoordinateModel) {
        if (detailAddressSettingFragment != null) {
            fragmentType = 2;
            View viewAddressInputGroup = findViewById(R.id.viewAddressInputGroup);
            viewAddressInputGroup.setVisibility(View.GONE);
            detailAddressSettingFragment.setLocationCoordinateModel(locationCoordinateModel);
            fragmentManager.beginTransaction()
                    .show(detailAddressSettingFragment)
                    .hide(addressListFragment)
                    .commit();
            topToolbarController.detailAddressToolbarInit();
        }
    }

    public void addressListFragmentShow() {
        if (detailAddressSettingFragment != null) {
            View viewAddressInputGroup = findViewById(R.id.viewAddressInputGroup);
            viewAddressInputGroup.setVisibility(View.VISIBLE);
            fragmentManager.beginTransaction()
                    .show(addressListFragment)
                    .hide(detailAddressSettingFragment)
                    .commit();
            topToolbarController.defaultAddressToolbarInit();
        }
    }

    /**
     * 뒤로가기 클릭시 이벤트
     * 상단 툴바의 뒤로가기 이벤트 처리는
     * onBackPressed 로 처리할수 없기에
     * 뒤로가기 이벤트를 처리할 backPressEvent 메소드를 생성
     */
    @Override
    public void onBackPressed() {
        backPressEvent();
    }


    /**
     * 위치 퍼미션 체크
     */
    private void locationPermissionCheck() {
        TedPermission.with(this)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        nowLocationSearchInit();
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        animationStop();
                    }
                }).setDeniedMessage(R.string.location_permission_denied)
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }

    /**
     * 현제 위치 검색 작업전 gps가 켜져있는지 체크후 좌표 검색을 위한 req 요청을 보냄
     */
    @SuppressLint("MissingPermission")
    private void nowLocationSearchInit() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if ((locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1, this);
        } else {
            YesOrNoDialog gpsOnDialog = new YesOrNoDialog();
            gpsOnDialog.dialogContent("현재 위치 검색을 위해 위치 설정 기능이 필요합니다.");
            gpsOnDialog.yesTextChange("설정하러 가기");
            gpsOnDialog.showAllowingStateLoss(getSupportFragmentManager(), "gpsOnDialog");
            gpsOnDialog.setYesOrNoDialogListener(new YesOrNoDialogFragmentListener() {
                @Override
                public void onDialogNo() {
                    animationStop();
                    gpsOnDialog.dismiss();
                }

                @Override
                public void onDialogYes() {
                    animationStop();
                    gpsOnDialog.dismiss();
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
        }
    }

    /**
     * 뒤로가기 버튼 클릭시 이벤트
     */
    private void backPressEvent() {
        switch (fragmentType) {
            case 0:
                finish();
                break;
            case 1:
                fragmentType = 0;
                addressListFragment.addressHistoryAdapterConnet();
                topToolbarController.defaultAddressToolbarInit();
                break;
            case 2:
                fragmentType = 1;
                addressListFragmentShow();
                break;
        }
    }

    @Override
    public void resultCode(int code) {
        if (code == CodeTypeManager.TopToolbarManager.BACK_BUTTON_PRESS.getType()) {
            backPressEvent();
        } else if (code == CodeTypeManager.TopToolbarManager.NOW_LOCATION_SEARCH.getType()) {
            animationStart();
            locationPermissionCheck();
        }
    }

    //
    @Override
    public void onHttpConnectionSuccess(int type, BaseModel baseModel) {
    }


    public void addressSaveInit() {
        realm.executeTransactionAsync(realm1 -> {
            IntroCheckModel introCheckModel = realm1.where(IntroCheckModel.class).findFirst();
            if (introCheckModel != null)
                introCheckModel.type = CodeTypeManager.RealmCodeManager.LOCATION_SAVE_SUCCESS.getCode();
        }, this::mainActivityMove);
    }

    private void mainActivityMove() {
        realm.close();
        Toast.makeText(this, "위치가 설정되었습니다.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


    public void animationStop() {
        viewLocationSettingLoading.animationStop(this);
    }

    public void animationStart() {
        viewLocationSettingLoading.loadingAnimationPlay(this);
    }

    /**
     * 위치 정보를 받아옴
     */
    @Override
    public void onLocationChanged(Location location) {
//        // 위치 정보를 받아왔을시 리스너를 끊어버림
//        // 연결 유지시 일정 간격으로 계속하여 위치정보를 수신하기 때문
        locationManager.removeUpdates(this);
        ResAddressSearchBaseModel.LocationCoordinate locationCoordinate = new ResAddressSearchBaseModel().getlocationCoordinate();
        locationCoordinate.lat = location.getLatitude();
        locationCoordinate.lnt = location.getLongitude();
        Logger.i("lat : " + locationCoordinate.lat + " lnt : " + locationCoordinate.lnt);
        detailLocationFragmentShow(locationCoordinate);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        locationManager.removeUpdates(this);
        animationStop();
    }

    //
    @Override
    public void onProviderEnabled(String provider) {
        locationManager.removeUpdates(this);
        animationStop();
    }

    //
    @Override
    public void onProviderDisabled(String provider) {
        // 위치 정보 가져왔을시 network 문제시에는 에러 토스를 띄우고 리스너 연결을 끊음
        if (provider.equals("network")) {
            Toast.makeText(this, "연결 상태가 일시적으로 불안합니다. 다시 시도해 주세요 : " + provider, Toast.LENGTH_LONG).show();
            locationManager.removeUpdates(this);
        }
        animationStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realmClose();
    }

        private void realmClose() {
        if (realm != null) {
            if (!realm.isClosed())
                realm.close();
        }
    }

    @Override
    public void onRestApiSuccess(CodeTypeManager.RestApi type, Object resModel) {

    }

    @Override
    public void onRestApiFail(CodeTypeManager.RestApi type) {

    }

}
