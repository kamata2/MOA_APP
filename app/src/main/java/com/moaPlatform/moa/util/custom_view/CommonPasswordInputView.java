package com.moaPlatform.moa.util.custom_view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chaos.view.PinView;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.util.ObjectUtil;

import butterknife.BindView;

/**
 * 비밀번호 입력 공통 뷰
 */
public class CommonPasswordInputView extends BaseLinearLayoutView{

    @BindView(R.id.tvPasswordInputTitle)
    TextView tvPasswordInputTitle;

    @BindView(R.id.pinViewPasswordInput)
    PinView pinViewPasswordInput;

    public CommonPasswordInputView(Context context) {
        super(context);
    }

    public CommonPasswordInputView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CommonPasswordInputView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    int layoutResourceId() {
        return R.layout.common_password_input_view;
    }

    @Override
    void initViews() {
        pinViewPasswordInput.setAnimationEnable(true);
    }


    public void setTitleName(String title) {
        if (tvPasswordInputTitle != null) {
            if (ObjectUtil.checkNotNull(title)) {
                tvPasswordInputTitle.setText(title);
            }
        }
    }

    /**
     * @return 입력한 패스워드의 문자를 리턴한다.
     */
    public String getInputText(){
        StringBuilder builder = new StringBuilder();
        builder.append(pinViewPasswordInput.getText().toString());
        return ObjectUtil.checkNotNull(builder.toString()) ? builder.toString(): "";
    }


    /**
     * 타이틀 노출여부
     */
    public void isShowTitle(boolean isShow){

        if(tvPasswordInputTitle != null){
            if(isShow){
                tvPasswordInputTitle.setVisibility(View.VISIBLE);
            }else{
                tvPasswordInputTitle.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 키보드 노출 처리
     */
    public void showKeyboard() {
        if (pinViewPasswordInput != null) {
            pinViewPasswordInput.requestFocus();
            new Handler().postDelayed(() -> {
                //키보드 올리기
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(pinViewPasswordInput, 0);
            }, 200);
        }

    }

    public EditText getPinViewPasswordInput() {
        return pinViewPasswordInput;
    }
}
