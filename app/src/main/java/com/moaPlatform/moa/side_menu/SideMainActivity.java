package com.moaPlatform.moa.side_menu;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.moaPlatform.moa.BuildConfig;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.auth.sign_up.controller.LogInActivity;
import com.moaPlatform.moa.auth.sign_up.controller.SignUpActivity;
import com.moaPlatform.moa.side_menu.coupon.CouponListActivity;
import com.moaPlatform.moa.side_menu.customercenter.CustomerCenterMainActivity;
import com.moaPlatform.moa.side_menu.eventnotice.view.event.EventMainActivity;
import com.moaPlatform.moa.side_menu.favorite.BookmarkActivity;
import com.moaPlatform.moa.side_menu.order.OrderHistoryActivity;
import com.moaPlatform.moa.side_menu.profile.ProfileChangeActivity;
import com.moaPlatform.moa.side_menu.review_view.MyReviewActivity;
import com.moaPlatform.moa.side_menu.settings.account.AccountSettingActivity;
import com.moaPlatform.moa.side_menu.settings.noticeagree.NoticeAgreeSettingActivity;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import org.moa.auth.userauth.android.api.MoaAuthHelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SideMainActivity extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout orderMain, reservationMain, couponMain, eventMain, customerMain, settingMain;
    LinearLayout favoriteMain, reviewMain;

    TextView cntFavorite, cntReview, cntOrder, cntEvent, cntCustom, cntCoupon;

    LinearLayout sideFinish;

    RelativeLayout sideProfile;

    Button btnSign, btnLogin;

    LinearLayout infoLayout;
    RelativeLayout loginLayout, idEstablish;

    private ImageView imgProfile;
    private TextView tvNick, tvTotalReview, tvTotalSymp;

    // 중복 클릭 방지 시간 설정
    public final static long MIN_CLICK_INTERVAL = 1000;
    private long mLastClickTime;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.side_main);

        initView();
        initData();

        //예약내역 1차버전 제외.
        reservationMain.setVisibility(View.GONE);
    }

    private void showView() {
        String memberId = MoaAuthHelper.getInstance().getCurrentMemberID();
        if (memberId.contains("@")) {
            infoLayout.setVisibility(View.VISIBLE);
            loginLayout.setVisibility(View.GONE);
            idEstablish.setVisibility(View.VISIBLE);
        } else {
            infoLayout.setVisibility(View.GONE);
            loginLayout.setVisibility(View.VISIBLE);
            idEstablish.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        showView();
        initData();
    }

    private void initView() {
        favoriteMain = findViewById(R.id.side_favorite);
        reviewMain = findViewById(R.id.side_review_check);
        orderMain = findViewById(R.id.side_order);
        reservationMain = findViewById(R.id.side_reservation);
        couponMain = findViewById(R.id.side_coupon);
        eventMain = findViewById(R.id.side_event);
        customerMain = findViewById(R.id.side_customercenter);
        settingMain = findViewById(R.id.side_setting);
        sideProfile = findViewById(R.id.side_profile_click);

        cntFavorite = findViewById(R.id.cnt_favorite);
        cntReview = findViewById(R.id.cnt_review);
        cntOrder = findViewById(R.id.cnt_order);
        cntCoupon = findViewById(R.id.cnt_coupon);
        cntEvent = findViewById(R.id.cnt_event);
        cntCustom = findViewById(R.id.cnt_customer);
        sideFinish = findViewById(R.id.side_finish);

        btnSign = findViewById(R.id.btnSignUp);
        btnLogin = findViewById(R.id.btnlogin);
        infoLayout = findViewById(R.id.userInfoLayout);
        loginLayout = findViewById(R.id.loginOrSignUpLayout);
        idEstablish = findViewById(R.id.IDESTABLISH);

        imgProfile = findViewById(R.id.side_profile_img);
        tvNick = findViewById(R.id.profile_name_check);
        tvTotalReview = findViewById(R.id.profile_review_cnt);
        tvTotalSymp = findViewById(R.id.profile_agree_cnt);

        favoriteMain.setOnClickListener(this);
        reviewMain.setOnClickListener(this);
        orderMain.setOnClickListener(this);
        reservationMain.setOnClickListener(this);
        couponMain.setOnClickListener(this);
        eventMain.setOnClickListener(this);
        customerMain.setOnClickListener(this);
        settingMain.setOnClickListener(this);
        sideFinish.setOnClickListener(this);
        sideProfile.setOnClickListener(this);
        imgProfile.setOnClickListener(this);
        btnSign.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        idEstablish.setOnClickListener(this);
        idEstablish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < MIN_CLICK_INTERVAL) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        switch (v.getId()) {
            case R.id.side_favorite:

                activityMove(BookmarkActivity.class);
                break;
            case R.id.side_review_check:
                activityMove(MyReviewActivity.class);
                break;
            case R.id.side_order:
                activityMove(OrderHistoryActivity.class);
                break;
            case R.id.side_coupon:
                activityMove(CouponListActivity.class);
                break;
            case R.id.side_event:
                activityMove(EventMainActivity.class);
                break;
            case R.id.side_customercenter:
                activityMove(CustomerCenterMainActivity.class);
                break;
            case R.id.side_setting:
                activityMove(NoticeAgreeSettingActivity.class);
                break;
            case R.id.side_profile_click:
                activityMove(ProfileChangeActivity.class);
                break;
            case R.id.side_profile_img:
                activityMove(ProfileChangeActivity.class);
                break;
            case R.id.side_finish:
                finish();
                break;
            case R.id.btnSignUp:
                activityMove(SignUpActivity.class);
                break;
            case R.id.btnlogin:
                activityMove(LogInActivity.class);
                break;
            case R.id.IDESTABLISH:
                activityMove(AccountSettingActivity.class);
                break;
        }
    }

    private void initData() {
        CommonModel commonModel = new CommonModel();
        RetrofitClient.getInstance().getMoaService().getSide(commonModel).enqueue(new Callback<ResSideMainList>() {
            @Override
            public void onResponse(@NonNull Call<ResSideMainList> call, @NonNull Response<ResSideMainList> response) {
                ResSideMainList resSideMainList = response.body();
                if (resSideMainList == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(resSideMainList)) {
                    initData();
                    return;
                }
                if (resSideMainList.isDataSuccess()) {
                    setData(resSideMainList);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResSideMainList> call, @NonNull Throwable t) {
                Logger.d(t.getMessage());
            }
        });
    }

    private void setData(ResSideMainList cntData) {
        if (cntData == null || cntData.sideMenuCnt == null)
            return;
        cntFavorite.setText(String.valueOf(cntData.sideMenuCnt.bmarkCnt));
        cntReview.setText(String.valueOf(cntData.sideMenuCnt.revwCnt));
        if (cntData.sideMenuCnt.cpCnt > 0) {
            cntCoupon.setText(String.valueOf(cntData.sideMenuCnt.cpCnt));
        } else
            cntCoupon.setVisibility(View.INVISIBLE);
        if (cntData.sideMenuCnt.orderCnt > 0) {
            cntOrder.setText(String.valueOf(cntData.sideMenuCnt.orderCnt));
        } else
            cntOrder.setVisibility(View.INVISIBLE);
        if (cntData.sideMenuCnt.evntNoticeCnt > 0) {
            cntEvent.setText(String.valueOf(cntData.sideMenuCnt.evntNoticeCnt));
        } else
            cntEvent.setVisibility(View.INVISIBLE);
        if (cntData.sideMenuCnt.notiCnt > 0) {
            cntCustom.setText(String.valueOf(cntData.sideMenuCnt.notiCnt));
        } else
            cntCustom.setVisibility(View.INVISIBLE);

        glideImage(imgProfile, cntData.sideMenuCnt.profImg);
        tvNick.setText(cntData.sideMenuCnt.nick);
        tvTotalReview.setText(String.valueOf(cntData.sideMenuCnt.totRevwCnt));
        tvTotalSymp.setText(String.valueOf(cntData.sideMenuCnt.totSympCnt));
    }

    /**
     * 액티비티 이동
     *
     * @param moveClass 이동할 클래스
     */
    private void activityMove(Class moveClass) {
        Intent intent = new Intent(SideMainActivity.this, moveClass);
        startActivity(intent);
    }

    private void glideImage(ImageView imageview, String subUrl) {
        if (subUrl == null || subUrl.equals("")) {
            Glide.with(getApplicationContext())
                    .asBitmap()
                    .load(R.drawable.profile_1)
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .into(imageview);
        } else {
            Glide.with(getApplicationContext())
                    .asBitmap()
                    .load(BuildConfig.FILE_SERVER_BASE_URL + subUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageview);
        }
    }
}