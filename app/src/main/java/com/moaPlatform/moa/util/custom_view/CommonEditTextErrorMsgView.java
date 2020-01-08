package com.moaPlatform.moa.util.custom_view;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

import com.moaPlatform.moa.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.res.ResourcesCompat;

import butterknife.BindView;

/**
 * Created by jiwun on 2019-06-13.
 * <p>
 * EditText 에 에러 베시지 포함 및 타이틀 포함
 */
public class CommonEditTextErrorMsgView extends BaseLinearLayoutView {
    // 입력하는 editText 창
    @BindView(R.id.etInputView)
    EditText etInputView;
    // 타이틀
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    // 에러 메시지
    @BindView(R.id.tvErrorMsg)
    TextView tvErrorMsg;

    // 공백 허용 여부 --> flase = 공백 허용, true = 공백 허용 안함
    private boolean notEmpty = false;

    private UserInputDataChecking userInputDataChecking;

    public CommonEditTextErrorMsgView(Context context) {
        super(context);
    }

    public CommonEditTextErrorMsgView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CommonEditTextErrorMsgView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    int layoutResourceId() {
        return R.layout.common_edittext_error_msg_view;
    }

    public void setUserInputDataChecking(UserInputDataChecking userInputDataChecking) {
        this.userInputDataChecking = userInputDataChecking;
    }

    /**
     * 초기화
     */
    @Override
    void initViews() {
        // 버전별로 Typeface 구하는 방법이 다름
        Typeface typeface;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            typeface = getResources().getFont(R.font.a_godic_13);
        } else {
            typeface = ResourcesCompat.getFont(getContext(), R.font.a_godic_13);
        }
        etInputView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력되는 텍스트 변화 있을때
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력이 끝났을 때
                // hint 만 있을경우에는 textStyle = normal, 텍스트를 적으면 textStyle = bold
                if (s.length() == 0) {
                    etInputView.setTypeface(typeface, Typeface.NORMAL);
                } else {
                    etInputView.setTypeface(typeface, Typeface.BOLD);
                }

                if (s.toString().length() != s.toString().trim().length() && notEmpty) {
                    etInputView.setText(s.toString().trim());
                    etInputView.setSelection(etInputView.getText().toString().length());
                }

                if (userInputDataChecking != null)
                    userInputDataChecking.onUserInputData(s.length());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 입력하기 전에
            }
        });
        errorMsgHidden();
    }

    // 타이틀 세팅 start

    /**
     * 타이틀 세팅
     *
     * @param title 적용할 타이틀
     */
    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    /**
     * string.xml 에 정의된 아이디로 타이틀 세팅
     *
     * @param titleResId string.xml 에 정의된 아이디
     */
    public void setTitle(@StringRes int titleResId) {
        setTitle(getResources().getString(titleResId));
    }
    // 타이틀 세팅 end

    // 에러 메시지 세팅 start

    /**
     * 에러 메시지 세팅
     *
     * @param errMsg 적용할 에러 메시지
     */
    public void setErrorMsg(String errMsg) {
        tvErrorMsg.setText(errMsg);
    }

    /**
     * 에러 메시지 string..xml 에 정의된 값으로 세팅
     *
     * @param errMsgResId string.xml 에 정의된 아이디
     */
    public void setErrorMsg(@StringRes int errMsgResId) {
        setErrorMsg(getResources().getString(errMsgResId));
    }

    /**
     * 에러 메시지 표시
     */
    public void errorMsgShow() {
        tvErrorMsg.setVisibility(VISIBLE);
    }

    /**
     * 에러 메시지 숨김
     */
    public void errorMsgHidden() {
        tvErrorMsg.setVisibility(GONE);
    }
    // 에러 메시지 세팅 end

    // editText start

    /**
     * editText hint 세팅
     *
     * @param hintMsg editText 표시할 hint 메시지
     */
    public void editTextInit(@NonNull String hintMsg) {
        etInputView.setHint(hintMsg);
    }

    /**
     * string.xml 값으로 hint editText 세팅
     *
     * @param hintMsgResId string.xml 에 정의된 아이디
     */
    public void editTextInit(@StringRes int hintMsgResId) {
        editTextInit(getResources().getString(hintMsgResId));
    }

    /**
     * editText 힌트와 input 타입 세팅
     *
     * @param hintMsg   힌트 메시지
     * @param inputType inputType 값
     */
    public void editTextInit(@NonNull String hintMsg, int inputType) {
        editTextInit(hintMsg);
        etInputView.setInputType(inputType);
    }

    /**
     * editText 힌트와 input 타입 세팅
     *
     * @param hintMsgResId string.xml 에 정의돈 hint 값
     * @param inputType    inputType 값
     */
    public void editTextInit(@StringRes int hintMsgResId, int inputType) {
        editTextInit(getResources().getString(hintMsgResId), inputType);
    }

    public String getInputText() {
        return etInputView.getText().toString();
    }

    /**
     * 최대 입력 가능한 개수 제한
     *
     * @param maxSize 최대 입력 가능한 개수
     */
    public void setInputTextMaxSize(int maxSize) {
        etInputView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxSize)});
    }

    public void setNotEmpty(boolean notEmpty) {
        this.notEmpty = notEmpty;
    }
    // editText end


    /**
     * etInputView (EditText) 에 입력이 발생됬을때 통신할 interface
     */
    public interface UserInputDataChecking {
        /**
         * 입력이 발생했을때
         *
         * @param inputCount 사용자가 입력한 데이터의 길이
         */
        void onUserInputData(int inputCount);
    }
}
