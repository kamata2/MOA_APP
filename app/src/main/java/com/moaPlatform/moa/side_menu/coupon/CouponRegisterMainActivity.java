package com.moaPlatform.moa.side_menu.coupon;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.side_menu.coupon.model.ReqCouponRegistration;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.custom_view.CommonTitleView;
import com.moaPlatform.moa.util.dialog.yesOrNo.OneBtnDialog;
import com.moaPlatform.moa.util.interfaces.ServerSideMsg;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CouponRegisterMainActivity extends AppCompatActivity {

    @BindView(R.id.incoupon)
    EditText incoupon;
    @BindView(R.id.commonTitleMyReview)
    CommonTitleView commont;


    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.side_coupon_register);
        ButterKnife.bind(this);

        commont.setBackButtonClickListener(v -> finish());
        commont.setTitleName(getString(R.string.side_coupon_register));
    }

    @OnClick(R.id.registerCoupon)
    void couponOnclick() {
        if (incoupon.getText().toString().trim().equals("")) {
            Toast.makeText(this, "쿠폰 번호를 입력하세요.", Toast.LENGTH_SHORT).show();
        } else
            couponServer();
    }

    private void couponServer() {
        ReqCouponRegistration reqCouponRegistration = new ReqCouponRegistration();
        reqCouponRegistration.cpNum = incoupon.getText().toString().trim();
        RetrofitClient.getInstance().getMoaService().couponRegisterList(reqCouponRegistration).enqueue(new Callback<CommonModel>() {
            @Override
            public void onResponse(@NonNull Call<CommonModel> call, @NonNull Response<CommonModel> response) {
                CommonModel commonModel = response.body();
                if (commonModel == null)
                    return;
                if (RetrofitClient.getInstance().hasReissuedAccessToken(commonModel)) {
                    couponServer();
                    return;
                }
                OneBtnDialog oneBtnDialog = new OneBtnDialog();
                String showText;
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
                    default:
                        showText = getString(R.string.already_used_coupon);
                        break;
                }
                oneBtnDialog.dialogContent(showText);
                oneBtnDialog.show((CouponRegisterMainActivity.this).getSupportFragmentManager(), "dialog");
                oneBtnDialog.oneBtnDialogFragmentListener(oneBtnDialog::dismiss);
            }

            @Override
            public void onFailure(@NonNull Call<CommonModel> call, @NonNull Throwable t) {
                Logger.d(t.getMessage());
                Toast.makeText(getApplicationContext(), getString(R.string.common_toast_msg_connection_fail), Toast.LENGTH_LONG).show();
            }
        });
    }
}
