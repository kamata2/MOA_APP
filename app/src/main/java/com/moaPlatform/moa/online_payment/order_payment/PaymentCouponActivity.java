package com.moaPlatform.moa.online_payment.order_payment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.online_payment.order_payment.controller.OrderPaymentServerController;
import com.moaPlatform.moa.online_payment.order_payment.model.ReqUseCouponModel;
import com.moaPlatform.moa.online_payment.order_payment.model.ResUseCouponModel;
import com.moaPlatform.moa.side_menu.coupon.model.ReqCouponRegistration;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;
import com.moaPlatform.moa.util.dialog.yesOrNo.OneBtnDialog;
import com.moaPlatform.moa.util.interfaces.ListItemClickListener;
import com.moaPlatform.moa.util.interfaces.RestApiResult;
import com.moaPlatform.moa.util.interfaces.ServerSideMsg;
import com.moaPlatform.moa.util.manager.CodeTypeManager;
import com.moaPlatform.moa.util.models.CommonModel;


public class PaymentCouponActivity extends AppCompatActivity implements ListItemClickListener, RestApiResult {

    private PaymentUseCouponAdapter paymentUseCouponAdapter;
    private ResUseCouponModel.UseCouponModel couponModel;
    private OrderPaymentServerController couponServerController;
    private LinearLayout noCoupon;                       //리스트 데이터 없을시 노출처리
    private RecyclerView couponList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_coupon_activity);
        init();
        getCouponData();
    }

    private void init() {
        // 상단 툴바 세팅
        CommonTitleView topToolbar = findViewById(R.id.topToolbar);
        topToolbar.setTitleName(R.string.sale_coupon);
        topToolbar.setBackButtonClickListener(v -> finish());

        // 서버와 통신하기 위한 칸트롤러 초기화
        couponServerController = new OrderPaymentServerController(this);
        couponServerController.setRestApiResult(this);

        // 쿠폰 리스트 초기화
        RecyclerView rvCouponList = findViewById(R.id.rvCouponList);
        rvCouponList.setLayoutManager(new LinearLayoutManager(this));
        paymentUseCouponAdapter = new PaymentUseCouponAdapter();
        rvCouponList.setAdapter(paymentUseCouponAdapter);
        paymentUseCouponAdapter.setListItemClickListener(this);

        // 쿠폰 적용 버튼 초기화
        Button btApplyCoupon = findViewById(R.id.btApplyCoupon);
        btApplyCoupon.setOnClickListener(v -> {

            if (couponModel != null) {
                Intent intent = new Intent();
                intent.putExtra("couponData", new Gson().toJson(couponModel));
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(PaymentCouponActivity.this, "적용된 쿠폰이 없습니다.", Toast.LENGTH_SHORT).show();
            }

        });

        // 쿠폰 등록 버튼 초기화
        Button btAddCoupon = findViewById(R.id.btAddCoupon);
        EditText etUserInputCoupon = findViewById(R.id.etCouponNumber);
        ReqCouponRegistration reqCouponRegister = new ReqCouponRegistration();
        btAddCoupon.setOnClickListener(v -> {
            reqCouponRegister.setCouponNumber(etUserInputCoupon);
            couponServerController.registerCoupon(reqCouponRegister);
        });

        //쿠폰 리스트 없을시
        noCoupon = findViewById(R.id.payment_nocoupon);
        couponList = findViewById(R.id.rvCouponList);
    }

    private void getCouponData() {
        couponServerController.useCouponSearch(new ReqUseCouponModel());
        couponServerController.setHttpConnectionResult((type, baseModel) -> {
            paymentUseCouponAdapter.setCouponList(((ResUseCouponModel) baseModel).useCouponList);
            if (paymentUseCouponAdapter.getItemCount() == 0) {
                couponList.setVisibility(View.GONE);
                noCoupon.setVisibility(View.VISIBLE);
            } else {
                couponList.setVisibility(View.VISIBLE);
                noCoupon.setVisibility(View.GONE);
            }
            paymentUseCouponAdapter.notifyDataSetChanged();
        });

    }

    @Override
    public void clickItem(Object codeType, int position, Object object) {
        couponModel = (ResUseCouponModel.UseCouponModel) object;
    }

    @Override
    public void onRestApiSuccess(CodeTypeManager.RestApi type, Object resModel) {
        switch (type) {
            case REGISTER_COUPON:
                couponDialogShow((CommonModel) resModel);
                break;
        }
    }

    /**
     * 쿠폰 코드값에 따른 각종 다이얼로그 문구 표시
     *
     * @param commonModel 쿠폰 값이 담겨인는 model
     */
    private void couponDialogShow(CommonModel commonModel) {
        OneBtnDialog oneBtnDialog = new OneBtnDialog();
        String showText = null;
        switch (commonModel.resultValue) {
            case ServerSideMsg.SUCCESS:
                showText = getString(R.string.coupon_register_success);
                break;
            case ServerSideMsg.COUPON_NOT_EXIST:
                showText = getString(R.string.not_exist_coupon);
                break;
            case ServerSideMsg.COUPON_REGISTED:
                showText = getString(R.string.already_coupon_registered);
                break;
            case ServerSideMsg.COUPON_USED:
                showText = getString(R.string.already_used_coupon);
                break;
        }
        oneBtnDialog.dialogContent(showText);
        oneBtnDialog.show(getSupportFragmentManager(), "couponDialog");
        oneBtnDialog.oneBtnDialogFragmentListener(() -> {
            if (commonModel.resultValue.equals(ServerSideMsg.SUCCESS)) {
                getCouponData();
            }
            oneBtnDialog.dismiss();
        });
    }

    @Override
    public void onRestApiFail(CodeTypeManager.RestApi type) {
        Toast.makeText(this, getString(R.string.common_toast_msg_network_err), Toast.LENGTH_SHORT).show();
    }

}
