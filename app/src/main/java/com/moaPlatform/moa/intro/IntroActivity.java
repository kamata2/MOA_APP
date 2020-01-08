package com.moaPlatform.moa.intro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.moaPlatform.moa.BuildConfig;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.bottom_menu.main.MainActivity;
import com.moaPlatform.moa.constants.MoaConstants;
import com.moaPlatform.moa.delivery_menu.eatout_store_detail.EatOutStoreDetailActivity;
import com.moaPlatform.moa.delivery_menu.store_detail.StoreDetailActivity;
import com.moaPlatform.moa.intro.controller.IntroServerController;
import com.moaPlatform.moa.intro.db.IntroCheckModel;
import com.moaPlatform.moa.intro.model.ReqAgreementInfoData;
import com.moaPlatform.moa.intro.model.ResVersionModel;
import com.moaPlatform.moa.intro.model.VersionModel;
import com.moaPlatform.moa.notification.MyFirebaseMessagingService;
import com.moaPlatform.moa.top_menu.location.LocationSettingActivity;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.ObjectUtil;
import com.moaPlatform.moa.util.custom_view.NonScrollViewPager;
import com.moaPlatform.moa.util.db.RealmController;
import com.moaPlatform.moa.util.dialog.yesOrNo.YesOrNoDialog;
import com.moaPlatform.moa.util.dialog.yesOrNo.YesOrNoDialogFragmentListener;
import com.moaPlatform.moa.util.interfaces.ClassConnectInterface;
import com.moaPlatform.moa.util.interfaces.CodeListener;
import com.moaPlatform.moa.util.interfaces.RetrofitConnectionResult;
import com.moaPlatform.moa.util.interfaces.ServerSideMsg;
import com.moaPlatform.moa.util.manager.CodeTypeManager;
import com.moaPlatform.moa.util.models.BaseModel;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.singleton.App;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IntroActivity extends AppCompatActivity implements CodeListener, ClassConnectInterface, RetrofitConnectionResult {
    // 뷰 페이저
    @BindView(R.id.introViewPager)
    NonScrollViewPager introViewPager;
    private Realm realm;
    private IntroCheckModel introCheckModel;
    private ClassConnectInterface classConnectInterface;
    int userEntryType = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.intro_activity);
        ButterKnife.bind(this);

        IntroServerController introServerController = new IntroServerController(this);
        introServerController.setRetrofitConnectionResult(this);
        introServerController.getStartData();
    }

    /**
     * 카카오 스킴 링크 화면 이동 처리
     */
    private void kakaoLinkScheme() {

        //TODO CHAN : MainActivity로 데이터를 넘겨서 처리할지 고려
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = getIntent();
                if (intent != null && intent.ACTION_VIEW.equals(intent.getAction())) {
                    String data = intent.getDataString();
                    if (ObjectUtil.checkNotNull(data) && data.contains(getString(R.string.kakao_app_key))) {
                        //카카오 링크이면..
                        Uri uri = intent.getData();
                        String actionView = uri.getQueryParameter(MoaConstants.PARAM_KAKAO_LINK_ACTION_VIEW);
                        String storeId = uri.getQueryParameter(MoaConstants.PARAM_KAKAO_LINK_STORE_ID);
                        Logger.d("IntroActivity data is >>> actionView is " + actionView + " storeId is " + storeId);

                        if (ObjectUtil.checkNotNull(actionView)) {

                            if (actionView.equals(MoaConstants.SCHEME_ACTION_EATOUT_DEATIL)) {
                                //외식 상세화면으로 이동
                                Intent eatOutStoreDetailIntent = new Intent(IntroActivity.this, EatOutStoreDetailActivity.class);
                                eatOutStoreDetailIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                eatOutStoreDetailIntent.putExtra(MoaConstants.EXTRA_STORE_ID, Integer.valueOf(storeId));
                                startActivity(eatOutStoreDetailIntent);

                            } else if (actionView.equals(MoaConstants.SCHEME_ACTION_STORE_DEATIL)) {
                                //배달 상세화면으로 이동
                                Intent storeDetailIntent = new Intent(IntroActivity.this, StoreDetailActivity.class);
                                storeDetailIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                storeDetailIntent.putExtra(CodeTypeManager.StoreInfo.STORE_ID.toString(), Integer.valueOf(storeId));
                                startActivity(storeDetailIntent);
                            }
                        }
                    }

                }
            }
        }, 1000);
    }


    /**
     * 초기 세팅
     */
    private void init() {
        Realm.init(this);
        RealmController realmController = new RealmController();
        realm = realmController.realmInstance();
        // IntroCheckModel 의 상태값에 따라 보여질 화면이 다름
        introCheckModel = realm.where(IntroCheckModel.class).findFirst();
        // introCheckModel 가 null 이 아니라는것은 선택적 이용약관 다이얼로그를 허용했다는 의미입니다.
        if (introCheckModel != null) {
            userEntryType = introCheckModel.type;
            Logger.i("realm data : " + introCheckModel.type);
            viewPagerInit(true);
            return;
        }
        viewPagerInit(false);
    }

    /**
     * 뷰 페이저 세팅
     *
     * @param dialogSuccess 선택적 접근 권한 다이얼로그 표시 유무
     *                      true : 표시안함, false : 표시
     */
    private void viewPagerInit(boolean dialogSuccess) {
        IntroViewPagerAdapter introViewPagerAdapter = new IntroViewPagerAdapter(getSupportFragmentManager());
        // 로그인 및 선택적 접근권한 관련
        IntroFragment introFragment = new IntroFragment();
        introFragment.setIntroActivityConnectInterface(this);
        introFragment.init(dialogSuccess);
        // 이용약관 동의 화면
        AgreementToTermsOfUseFragment agreementToTermsOfUseFragment = new AgreementToTermsOfUseFragment();
        agreementToTermsOfUseFragment.setClassConnectInterface(this);
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(introFragment);
        fragments.add(agreementToTermsOfUseFragment);
        introViewPagerAdapter.setFragments(fragments);
        introViewPager.setAdapter(introViewPagerAdapter);
    }

    @Override
    public void resultCode(int code) {
        if (code == CodeTypeManager.IntroManager.LOGIN_SUCCESS.getType()) {
            if (introCheckModel.type == CodeTypeManager.IntroManager.TERMS_AND_CONDITIONS_AGREEMENT.getType())
                locationSetting();
            else if (introCheckModel.type == CodeTypeManager.IntroManager.LOCATION.getType())
                mainActivitySetting();
            else {
                SharedPreferences sf = getSharedPreferences("haveiduser", MODE_PRIVATE);
                String sIdHave = sf.getString("logout", "no");
                if (sIdHave.equals("yes")) {
                    setAgreement();
                } else if (sIdHave.equals("no")) {
                    introViewPager.setCurrentItem(1);
                }
            }
        } else if (code == CodeTypeManager.IntroManager.DIALOG_SUCCESS.getType()) {
            introModelItemAdd(code);
        } else if (code == CodeTypeManager.IntroManager.TERMS_AND_CONDITIONS_AGREEMENT.getType()) {
            introModelUpdate(code);
        }
    }

    private void setAgreement() {

        RetrofitClient.getInstance().getMoaService().postAgreementData(getReqAgreementModel()).enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {
                CommonModel commonModel = response.body();
                if (commonModel == null || classConnectInterface == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(commonModel)) {
                    setAgreement();
                    return;
                }
                if (commonModel.resultValue.equals(ServerSideMsg.SUCCESS)) {
                    Logger.d("btnDoneStart res data : " + App.getInstance().gson.toJson(commonModel));
                    mainActivitySetting();
//                    classConnectInterface.onActType(CodeTypeManager.ClassCodeManager.INTRO_USE_AGREEMENT);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * @return 약관 동의시 서버에 전송할 req 모델 반환
     */
    private ReqAgreementInfoData getReqAgreementModel() {
        ReqAgreementInfoData reqModel = new ReqAgreementInfoData();
        reqModel.setData(true, true);
        return reqModel;
    }

    /**
     * 위치 지,동 세팅하는곳으로 이동
     */
    private void locationSetting() {
        realmClose();
        Intent intent = new Intent(this, LocationSettingActivity.class);
        startActivity(intent);
        finish();
    }

    private void mainActivitySetting() {
        Intent intent = new Intent(this, MainActivity.class);

        String data = getIntent().getStringExtra(MyFirebaseMessagingService.NOTIFICATION_DATA_KEY);
        if (data != null) {
            intent.putExtra(MyFirebaseMessagingService.NOTIFICATION_DATA_KEY, data);
        }
        startActivity(intent);
        finish();

        kakaoLinkScheme();

    }

    /**
     * IntroCheckModel 모델의 데이터 추가
     *
     * @param code 코드값
     */
    private void introModelItemAdd(int code) {
        realm.beginTransaction();
        IntroCheckModel introCheckModel = realm.createObject(IntroCheckModel.class);
        introCheckModel.type = code;
        userEntryType = code;
        this.introCheckModel = introCheckModel;
        realm.commitTransaction();
    }

    /**
     * introCheckModel 데이터 업데이트
     * update는 비동기 작업
     *
     * @param code 업데이트할 코드값
     */
    private void introModelUpdate(int code) {
        realm.executeTransactionAsync(realm -> {
            IntroCheckModel introCheckModel = realm.where(IntroCheckModel.class).findFirst();
            if (introCheckModel != null) {
                introCheckModel.type = code;
                userEntryType = code;
            }
        }, this::locationSetting);
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
    public void onActType(CodeTypeManager.ClassCodeManager type) {
        switch (type) {
            case LOGIN_SUCCESS:
                if (userEntryType == CodeTypeManager.RealmCodeManager.INTRO_USE_AGREEMENT_SUCCESS.getCode())
                    locationSetting();
                else if (userEntryType == CodeTypeManager.RealmCodeManager.LOCATION_SAVE_SUCCESS.getCode())
                    mainActivitySetting();
                else
                    introViewPager.setCurrentItem(1);
                break;
            case INTRO_USE_AGREEMENT:
                introModelUpdate(CodeTypeManager.RealmCodeManager.INTRO_USE_AGREEMENT_SUCCESS.getCode());
                break;
            case SELECT_PERMISSION_DIALOG:
                introModelItemAdd(CodeTypeManager.RealmCodeManager.INTRO_SELECT_PERMISSION_SUCCESS.getCode());
                break;
        }
    }

    @Override
    public void onRetrofitSuccess(int type, BaseModel baseModel) {

        VersionModel versionModel = ((ResVersionModel) baseModel).versionModel;

        if (versionModel != null) {
            int moaAppVersionCode = BuildConfig.VERSION_CODE;
            //String versionName = BuildConfig.VERSION_NAME;

            if (moaAppVersionCode < versionModel.min) {

                Logger.d("update test >>>>>>> 강제업데이트");

                YesOrNoDialog forceUpdateDialog = new YesOrNoDialog();
                forceUpdateDialog.setVisibleNoButton(false);
                forceUpdateDialog.dialogContent(getString(R.string.yes_or_no_dialog_content_force_update));
                forceUpdateDialog.setYesOrNoDialogListener(new YesOrNoDialogFragmentListener() {
                    @Override
                    public void onDialogNo() {

                    }

                    @Override
                    public void onDialogYes() {
                        forceUpdateDialog.dismiss();
                        finish();
                        //강제 업데이트
                        goPlayStoreMoaApp();
                    }
                });
                forceUpdateDialog.setDismissListener(new YesOrNoDialog.DismissListener() {
                    @Override
                    public void onDismiss() {
                        finish();
                    }
                });
                forceUpdateDialog.show(getSupportFragmentManager(), "forceUpdateDialog");

            } else if (moaAppVersionCode >= versionModel.min && moaAppVersionCode < versionModel.max) {
                Logger.d("update test >>>>>>> 선택업데이트");
                //선택 업데이트
                YesOrNoDialog selectUpdateDialog = new YesOrNoDialog();
                selectUpdateDialog.dialogContent(getString(R.string.yes_or_no_dialog_content_select_update));
                selectUpdateDialog.setYesOrNoDialogListener(new YesOrNoDialogFragmentListener() {
                    @Override
                    public void onDialogNo() {
                        init();
                    }

                    @Override
                    public void onDialogYes() {
                        finish();
                        goPlayStoreMoaApp();
                    }
                });
                selectUpdateDialog.setDismissListener(new YesOrNoDialog.DismissListener() {
                    @Override
                    public void onDismiss() {
                        init();
                    }
                });
                selectUpdateDialog.show(getSupportFragmentManager(), "selectUpdateDialog");
            } else {
                Logger.d("update test >>>>>>> 최신버전입니다.");
                //최신버전인 경우
                init();
            }
        }
    }

    @Override
    public void onRetrofitFail(int type, String msg) {
        Toast.makeText(this, getString(R.string.common_toast_msg_connection_fail), Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * 플레이스토어로 이동
     */
    private void goPlayStoreMoaApp() {
        Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://play.google.com/store/apps/details?id=com.moaPlatform.moa"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
