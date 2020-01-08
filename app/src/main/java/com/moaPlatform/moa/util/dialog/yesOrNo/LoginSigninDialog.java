package com.moaPlatform.moa.util.dialog.yesOrNo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moaPlatform.moa.R;

import androidx.annotation.NonNull;

public class LoginSigninDialog extends Dialog {

    private YesOrNoDialogFragmentListener yesOrNoDialogListener;
    private String dialogContentString = "";
    private String yesText = "";
    public void setSignLoginListener (YesOrNoDialogFragmentListener yesOrNoDialogListener) {
        this.yesOrNoDialogListener = yesOrNoDialogListener;
    }
    public void dialogContent(String dialogContentString) {
        this.dialogContentString = dialogContentString;
    }
    private RelativeLayout btnClose;
    private TextView mPositiveButton;
    private TextView mNegativeButton;
    private View.OnClickListener mPositiveListener;
    private View.OnClickListener mNegativeListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //다이얼로그 밖의 화면은 흐리게 만들어줌
//        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
//        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//        layoutParams.dimAmount = 0.8f;
//        getWindow().setAttributes(layoutParams);

        setContentView(R.layout.signlogin_dialog);

        btnClose =findViewById(R.id.btnClose);
        //셋팅
        mPositiveButton=findViewById(R.id.no);
        mNegativeButton=findViewById(R.id.yes);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        //클릭 리스너 셋팅 (클릭버튼이 동작하도록 만들어줌.)
        mPositiveButton.setOnClickListener(mPositiveListener);
        mNegativeButton.setOnClickListener(mNegativeListener);
    }

    //생성자 생성
    public LoginSigninDialog(@NonNull Context context, View.OnClickListener positiveListener, View.OnClickListener negativeListener) {
        super(context);
        this.mPositiveListener = positiveListener;
        this.mNegativeListener = negativeListener;
    }
}
