package com.moaPlatform.moa.auth.sign_up.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.moaPlatform.moa.BuildConfig;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.auth.sign_up.model.PersonalInformationModel;
import com.moaPlatform.moa.auth.sign_up.model.ReqResExistsCheckModel;
import com.moaPlatform.moa.activity.EventWebViewActivity;
import com.moaPlatform.moa.constants.MoaUrlConstants;
import com.moaPlatform.moa.util.Logger;
import com.moaPlatform.moa.util.StringUtil;
import com.moaPlatform.moa.util.dialog.yesOrNo.OneBtnDialog;
import com.moaPlatform.moa.util.singleton.RetrofitClient;

import java.util.Objects;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {
    private View view;
    // 이메일, 패스워드, 비밀번호 확인 입력창
    private EditText inputEmail, inputPw, inputCheckPw;
    // 입력창 에러시 표시할 에러 메시지
    private TextView inputEmailError, inputCheckPwError, inputPwError;
    // 모두 동의, 이용약관 동의, 개인정보 수집 및 이용 동의, 쿠폰/이벤트 알림, 만 14세 이상 동의 체크박스
    private CheckBox allAgreeCkBox, termsCndtnCkBox, prsnlInfrmCkBox, pushAgreeCkBox, ageAgreeCkBox;
    //    // 다음 페이지로 이동 시켜주는 버튼
    private Button signUpNext;
    // 사용자가 입력한 개인정보 관련 처리 모델
    private PersonalInformationModel prsnlModel = new PersonalInformationModel();

    private final int TERMS_CONDITIONS_ID = R.id.checkBoxTermsAgree;
    private final int PERSONAL_INFORMATION_ID = R.id.personalInfAgree;
    private final int PUSH_CHECk_ID = R.id.pushAgree;
    private final int AGE_ID = R.id.ageAgree;

    // 이메일 중복 체크 확인 false -> 미완료, true -> 완료
    private boolean emailCheckSuccess = false;
    // 데이터 존재 유무 체크 모델
    private ReqResExistsCheckModel reqResExistsCheckModel;
    private EditText etInputRecommenderNumber;

    // 추천인 등록 유무 = false -> 미등록, true -> 등록
    private boolean isRecommenderCheck = false;
    public boolean recommenderClear = false;
    private CheckBox cbAddRecommender;
    private TextView tvRecommenderError;
    private View toolTip;
    private ScrollView svSignUp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sign_up_fragment, container, false);
        init();
        inputPersonalInformationInit();
        termsCndtnInit();
        signUpNext = view.findViewById(R.id.signUpNext);
        signUpNext.setOnClickListener((View view) -> nextStep());
        signUpNextClickableInit(false);

        return view;
    }

    /**
     * 초기화
     */
    private void init() {

        tvRecommenderError = view.findViewById(R.id.tvRecommenderError);
        svSignUp = view.findViewById(R.id.svSignUp);

        // 아이디 중복 체크
        Button btnExitsEmail = view.findViewById(R.id.btnExitsEmail);
        btnExitsEmail.setOnClickListener(v -> {
            if (!emailCheckSuccess)
                emailExitsChecking();
        });
        etInputRecommenderNumber = view.findViewById(R.id.etInputRecommender);
        // 추처인 존재 유무 체크
        Button btnRecommenderCheck = view.findViewById(R.id.btnRecommenderCheck);
        btnRecommenderCheck.setOnClickListener(v -> RecommenderChecking());

        cbAddRecommender = view.findViewById(R.id.rpCheckBox);
        cbAddRecommender.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isRecommenderCheck = isChecked;
            if (isChecked) {
                etInputRecommenderNumber.setEnabled(true);
                btnRecommenderCheck.setEnabled(true);
                btnRecommenderCheck.setBackgroundResource(R.color.coral);
            } else {
                etInputRecommenderNumber.setEnabled(false);
                btnRecommenderCheck.setEnabled(false);
                btnRecommenderCheck.setBackgroundResource(R.color.nobel);
                if (tvRecommenderError.getVisibility() == View.VISIBLE)
                    tvRecommenderError.setVisibility(View.GONE);
            }
        });

        toolTip = view.findViewById(R.id.toolTip);
        ImageView rpQuestion = view.findViewById(R.id.rpQuestion);
        rpQuestion.setOnClickListener(v -> {
            OneBtnDialog recommendID = new OneBtnDialog();
            recommendID.dialogTitle(getString(R.string.recommend_id_explain));
            recommendID.dialogContent(getString(R.string.recommend_id_contents));
            recommendID.setShowCloseButton(true);
            recommendID.oneBtnDialogFragmentListener(() -> recommendID.dismiss());
            recommendID.show(getFragmentManager(), "recommendID");

        });

        View termsAgreeAllRead = view.findViewById(R.id.termsAgreeAllRead);
        termsAgreeAllRead.setOnClickListener(v -> {
            Intent intent = new Intent(view.getContext(), EventWebViewActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(EventWebViewActivity.EXTRA_TITLE_TYPE_KEY, EventWebViewActivity.EXTRA_TITLE_TYPE_TRANSPARENT_VALUE);
            intent.putExtra(EventWebViewActivity.EXTRA_EVENT_URL, BuildConfig.REST_API_BASE_URL + MoaUrlConstants.USE_TERMS_AND_CONDITIONS_URL);
            startActivity(intent);
        });

        View personalInfAllRead = view.findViewById(R.id.personalInfAllRead);
        personalInfAllRead.setOnClickListener(v -> {
            Intent intent = new Intent(view.getContext(), EventWebViewActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(EventWebViewActivity.EXTRA_EVENT_URL, BuildConfig.REST_API_BASE_URL + MoaUrlConstants.AGREE_TO_COLLECT_AND_USE_PERSONAL_INFOMATION_URL);
            intent.putExtra(EventWebViewActivity.EXTRA_TITLE_TYPE_KEY, BuildConfig.REST_API_BASE_URL + MoaUrlConstants.USE_TERMS_AND_CONDITIONS_URL);
            startActivity(intent);
        });

    }

    private void toolTipShow() {
        if (toolTip.getVisibility() == View.GONE) {
            toolTip.setVisibility(View.VISIBLE);
            new Handler().postDelayed(() -> toolTip.setVisibility(View.GONE), 3000);
        }
    }

    /**
     * 추처인 존재 유무 체크
     */
    private void RecommenderChecking() {
        final String recommenderNumber = etInputRecommenderNumber.getText().toString();
        if (!recommenderNumber.equals("")) {
            if (reqResExistsCheckModel == null)
                reqResExistsCheckModel = new ReqResExistsCheckModel();
            reqResExistsCheckModel.setInputPhoneNumber(recommenderNumber);
            RetrofitClient.getInstance().getMoaService().recommenderNumber(reqResExistsCheckModel).enqueue(new Callback<ReqResExistsCheckModel>() {
                @Override
                public void onResponse(@NonNull Call<ReqResExistsCheckModel> call, @NonNull Response<ReqResExistsCheckModel> response) {
                    ReqResExistsCheckModel reqResExistsCheckModel = response.body();
                    if (reqResExistsCheckModel == null)
                        return;
                    if (RetrofitClient.getInstance().hasReissuedAccessToken(reqResExistsCheckModel)) {
                        RecommenderChecking();
                        return;
                    }
                    if (reqResExistsCheckModel.isExistsPhoneNumber()) {
                        Logger.w("RecommenderChecking : 추천인 번호가 존재합니다.");
                        tvRecommenderError.setText("사용 가능한 추천인 번호입니다.");
                        tvRecommenderError.setVisibility(View.VISIBLE);
                        recommenderClear = true;
                    } else {
                        Logger.w("RecommenderChecking : 추천인 번호가 없습니다.");
                        tvRecommenderError.setText("휴대 전화번호를 다시 한 번 확인해 주세요.");
                        tvRecommenderError.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ReqResExistsCheckModel> call, @NonNull Throwable t) {
                    Logger.e("RecommenderChecking : 추천인 존재 유무 체크 실패");
                }
            });
        } else {
            tvRecommenderError.setText(getString(R.string.sign_up_phone_number_again_check));
            tvRecommenderError.setVisibility(View.VISIBLE);
            Logger.w("RecommenderChecking : 추천인 번호를 입력해 주세요");
        }
    }

    /**
     * 아이디 중복체크
     */
    private void emailExitsChecking() {
        // 사용할 이메일
        final String useEmail = inputEmail.getText().toString();
        if (useEmail.length() > 0) {

            // 이메일 형식 체크
            if (prsnlModel.emailCheck(inputEmail)) {
                inputEmailError.setText(getString(R.string.sign_up_input_email_error));
                inputEmailError.setVisibility(View.VISIBLE);
                return;
            }else if (!StringUtil.isEmail(useEmail)) {
                inputEmailError.setText(getString(R.string.sign_up_input_invalid_email_error));
                inputEmailError.setVisibility(View.VISIBLE);
                return;
            }
            else {
                inputEmailError.setVisibility(View.GONE);
            }

            if (reqResExistsCheckModel == null)
                reqResExistsCheckModel = new ReqResExistsCheckModel();
            reqResExistsCheckModel.setInputEmail(useEmail);
            RetrofitClient.getInstance().getMoaService().emailSearch(reqResExistsCheckModel).enqueue(new Callback<ReqResExistsCheckModel>() {
                @Override
                public void onResponse(@NonNull Call<ReqResExistsCheckModel> call, @NonNull Response<ReqResExistsCheckModel> response) {
                    ReqResExistsCheckModel reqResExistsCheckModel = response.body();
                    if (reqResExistsCheckModel == null)
                        return;
                    if (RetrofitClient.getInstance().hasReissuedAccessToken(reqResExistsCheckModel)) {
                        emailExitsChecking();
                        return;
                    }
                    if (reqResExistsCheckModel.isExistsEmail()) {
                        Logger.w("emailExitsChecking : 이미 사용 중인 아이디입니다.");
                        inputEmailError.setText(getString(R.string.sign_up_exits_email));
                        inputEmailError.setVisibility(View.VISIBLE);
                    } else {
                        Logger.w("emailExitsChecking : 사용 가능한 이메일입니다.");
                        inputEmailError.setText(getString(R.string.sign_up_email_available_to_use));
                        inputEmailError.setVisibility(View.VISIBLE);
                        emailCheckSuccess = true;
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ReqResExistsCheckModel> call, @NonNull Throwable t) {
                    Logger.e("emailExitsChecking : 아이디 중복 체크 실패");
                    t.printStackTrace();
                }
            });
        } else {
            Logger.w("emailExitsChecking : 이메일을 입력해 주세요");
        }
    }

    /**
     * 개인정보 입력 화면 세팅
     */
    private void inputPersonalInformationInit() {
        inputEmail = view.findViewById(R.id.etInputEmail);
        inputPw = view.findViewById(R.id.inputPw);
        inputCheckPw = view.findViewById(R.id.inputCheckPw);
        inputEmailError = view.findViewById(R.id.inputEmailError);
        inputCheckPwError = view.findViewById(R.id.inputCheckPwError);
        inputPwError = view.findViewById(R.id.inputPwError);

        inputEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                emailCheckSuccess = false;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() != s.toString().replace(" ", "").length()) {
                    inputEmail.setText(s.toString().replace(" ", ""));
                    inputEmail.setSelection(inputEmail.getText().toString().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputPw.setFilters(new InputFilter[]{filterNotKor, maxLenthFilter});
        inputPw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() != s.toString().replace(" ", "").length()) {
                    inputPw.setText(s.toString().replace(" ", ""));
                    inputPw.setSelection(inputPw.getText().toString().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputCheckPw.setFilters(new InputFilter[]{filterNotKor, maxLenthFilter});
        inputCheckPw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() != s.toString().replace(" ", "").length()) {
                    inputCheckPw.setText(s.toString().replace(" ", ""));
                    inputCheckPw.setSelection(inputCheckPw.getText().toString().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etInputRecommenderNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() != s.toString().replace(" ", "").length()) {
                    etInputRecommenderNumber.setText(s.toString().replace(" ", ""));
                    etInputRecommenderNumber.setSelection(etInputRecommenderNumber.getText().toString().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 이용약관 세팅
     */
    private void termsCndtnInit() {
        allAgreeCkBox = view.findViewById(R.id.checkBoxAllAgree);
        termsCndtnCkBox = view.findViewById(TERMS_CONDITIONS_ID);
        prsnlInfrmCkBox = view.findViewById(PERSONAL_INFORMATION_ID);
        pushAgreeCkBox = view.findViewById(PUSH_CHECk_ID);
        ageAgreeCkBox = view.findViewById(AGE_ID);

        allAgreeCkBox.setOnCheckedChangeListener(this);
        termsCndtnCkBox.setOnCheckedChangeListener(this);
        prsnlInfrmCkBox.setOnCheckedChangeListener(this);
        pushAgreeCkBox.setOnCheckedChangeListener(this);
        ageAgreeCkBox.setOnCheckedChangeListener(this);

        allAgreeCkBox.setOnClickListener((View v) -> allAgreeCheck(allAgreeCkBox.isChecked()));
    }

    /**
     * 전체체크 박스 이외의 버튼들을 모두 체크시 전체 체크 박스도 체크
     * 하나라도 체크 안했을시 전체체크 박스 비활성화
     */
    private void allAgreeCheck() {
        if (termsCndtnCkBox.isChecked() && prsnlInfrmCkBox.isChecked() && pushAgreeCkBox.isChecked() && ageAgreeCkBox.isChecked()) {
            allAgreeCkBox.setChecked(true);
            signUpNextClickableInit(true);
        } else {
            allAgreeCkBox.setChecked(false);
            if (termsCndtnCkBox.isChecked() && prsnlInfrmCkBox.isChecked() && ageAgreeCkBox.isChecked()) {
                signUpNextClickableInit(true);
            } else {
                signUpNextClickableInit(false);
            }
        }
    }

    /**
     * 전체 체크 버튼 클릭시 작업
     *
     * @param checkValue 체크 상테값
     */
    private void allAgreeCheck(boolean checkValue) {
        termsCndtnCkBox.setChecked(checkValue);
        prsnlInfrmCkBox.setChecked(checkValue);
        pushAgreeCkBox.setChecked(checkValue);
        ageAgreeCkBox.setChecked(checkValue);
    }

    /**
     * 체크박스 체크시 이벤트
     *
     * @param buttonView 체크박스
     * @param isChecked  체크 상태값
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case TERMS_CONDITIONS_ID:
                allAgreeCheck();
                break;
            case PERSONAL_INFORMATION_ID:
                allAgreeCheck();
                break;
            case PUSH_CHECk_ID:
                allAgreeCheck();
                break;
            case AGE_ID:
                allAgreeCheck();
                break;
        }
    }

    /**
     * 이메일, 비밀번호, 비밀번호 확인의 데이터가 제대로 들어갔는지 체크
     */
    private boolean inputPersonalInformationErrorCheck() {
        // 이메일 중복 체크 안했을때
        if (!emailCheckSuccess) {
            inputEmailError.setText(getString(R.string.sign_up_email_check_please));
            inputEmailError.setVisibility(View.VISIBLE);
            svSignUp.scrollTo(svSignUp.getBottom(), inputEmail.getTop());
            return false;
        }
        if (prsnlModel.passwordCheck(inputPw)) {
            inputPwError.setVisibility(View.VISIBLE);
            svSignUp.scrollTo(svSignUp.getBottom(), inputPw.getTop());
            return false;
        } else {
            inputPwError.setVisibility(View.GONE);
        }

        if (prsnlModel.passwordOneMoreCheck(inputPw, inputCheckPw)) {
            inputCheckPwError.setVisibility(View.VISIBLE);
            svSignUp.scrollTo(svSignUp.getBottom(), inputCheckPw.getTop());
            return false;
        } else {
            inputCheckPwError.setVisibility(View.GONE);
        }

        if (isRecommenderCheck && !recommenderClear) {
            tvRecommenderError.setText("휴대 전화번호를 다시 한 번 확인해 주세요.");
            tvRecommenderError.setVisibility(View.VISIBLE);
            svSignUp.scrollTo(svSignUp.getBottom(), etInputRecommenderNumber.getTop());
            return false;
        }

        return true;
    }

    /**
     * 회원가입 다음 페이지로 이동하는 버튼의 활성화 비활성화 세팅
     *
     * @param clickable 상태값
     */
    private void signUpNextClickableInit(boolean clickable) {
        signUpNext.setClickable(clickable);
        signUpNext.setBackgroundResource(clickable ? R.color.coral : R.color.nobel);
    }

    private void nextStep() {
        if (inputPersonalInformationErrorCheck()) {
            ((SignUpActivity) Objects.requireNonNull(getActivity())).walletAddressPwInit();
        }
    }

    public String getEmail() {
        return inputEmail.getText().toString();
    }

    String getPw() {
        return inputPw.getText().toString();
    }

    String getRecommenderNumber() {
        return etInputRecommenderNumber.getText().toString();
    }

    private InputFilter filterNotKor = (source, start, end, dest, dstart, dend) -> {

        Pattern ps = Pattern.compile("^[ㄱ-ㅎㅏ-ㅣ가-힣]*$");
        if (ps.matcher(source).matches()) {
            return "";
        }
        return null;
    };

    private InputFilter maxLenthFilter = new InputFilter.LengthFilter(20);


}
