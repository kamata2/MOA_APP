package com.moaPlatform.moa.auth.sign_up;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.auth.sign_up.controller.SignUpActivity;
import com.moaPlatform.moa.util.custom_view.CommonEditTextErrorMsgView;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD;

/**
 * Created by jiwun on 2019-05-27.
 */
public class WalletAddressCreateFragment extends Fragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.wallet_address_create_fragment, container, false);

        Button btnJoin = view.findViewById(R.id.btnJoin);

        // 전자 지갑 비밀번호 세팅
        CommonEditTextErrorMsgView viewWalletPassword = view.findViewById(R.id.viewWalletPassword);
        viewWalletPassword.setTitle(R.string.sign_up_wallet_pw);
        viewWalletPassword.editTextInit("숫자 6자리를 입력해 주세요", TYPE_CLASS_NUMBER | TYPE_NUMBER_VARIATION_PASSWORD);
        viewWalletPassword.setInputTextMaxSize(6);
        viewWalletPassword.setNotEmpty(true);

        // 전자 지갑 비밀번호 확인 세팅
        CommonEditTextErrorMsgView viewWalletPasswordCheck = view.findViewById(R.id.viewWalletPasswordCheck);
        viewWalletPasswordCheck.setTitle(R.string.sign_up_wallet_pw_check);
        viewWalletPasswordCheck.editTextInit("비밀번호를 한 번 더 입력해 주세요.", TYPE_CLASS_NUMBER | TYPE_NUMBER_VARIATION_PASSWORD);
        viewWalletPasswordCheck.setInputTextMaxSize(6);
        viewWalletPasswordCheck.setNotEmpty(true);
        viewWalletPasswordCheck.setErrorMsg("전자지갑 비밀번호가 일치하지 않습니다.");

        // 전자 지갑 생년월일 입력 화면
        CommonEditTextErrorMsgView viewWalletBirthDay = view.findViewById(R.id.viewWalletBirthDay);
        viewWalletBirthDay.setTitle("생년월일 입력");
        viewWalletBirthDay.editTextInit("생년월일을 입력해 주세요.(YYYYMMDD)", TYPE_CLASS_NUMBER);
        viewWalletBirthDay.setInputTextMaxSize(8);
        viewWalletBirthDay.setNotEmpty(true);

        btnJoin.setOnClickListener(v -> {

            if (viewWalletPassword.getInputText().length() == 0) {
                viewWalletPassword.setErrorMsg("전자지갑 비밀번호를 입력해 주세요");
                viewWalletPassword.errorMsgShow();
                return;
            } else {
                viewWalletPassword.errorMsgHidden();
            }

            if (viewWalletPassword.getInputText().length() < 6) {
                viewWalletPassword.setErrorMsg("전자지갑 비밀번호를 6자리 이상 입력해 주세요");
                viewWalletPassword.errorMsgShow();
                return;
            } else {
                viewWalletPassword.errorMsgHidden();
            }

            if (viewWalletPassword.getInputText().length() != 6) {
                viewWalletPassword.setErrorMsg("전자지갑 비밀번호는 6자리 숫자만 사용 가능합니다.");
                viewWalletPassword.errorMsgShow();
                return;
            } else {
                viewWalletPassword.errorMsgHidden();
            }

            if (!viewWalletPasswordCheck.getInputText().equals(viewWalletPassword.getInputText())) {
                viewWalletPasswordCheck.setErrorMsg("전자지갑 비밀번호가 일치하지 않습니다.");
                viewWalletPasswordCheck.errorMsgShow();
                return;
            } else {
                viewWalletPasswordCheck.errorMsgHidden();
            }

            if (viewWalletBirthDay.getInputText().length() != 8) {
                viewWalletBirthDay.setErrorMsg("생년월일(예 : 19840102 )을 입력해 주세요.");
                viewWalletBirthDay.errorMsgShow();
                return;
            } else {
                viewWalletBirthDay.errorMsgHidden();
            }

            ((SignUpActivity) Objects.requireNonNull(getActivity())).inpuPrsnlInfrmSuccess();

//            if (viewWalletPassword.getInputText().equals(viewWalletPasswordCheck.getInputText()) &&
//                    viewWalletPassword.getInputText().trim().length() == 6) {
//                if (viewWalletBirthDay.getInputText().trim().equals("") && viewWalletBirthDay.getInputText().trim().length() < 8) {
//                    tvErrorMsg.setVisibility(View.VISIBLE);
//                } else {
//                    ((SignUpActivity) Objects.requireNonNull(getActivity())).inpuPrsnlInfrmSuccess();
//                }
//            }
        });

        return view;
    }

    public String getPw() {
        return ((CommonEditTextErrorMsgView) view.findViewById(R.id.viewWalletPassword)).getInputText();
    }

    public String getBirth() {
        return ((CommonEditTextErrorMsgView) view.findViewById(R.id.viewWalletBirthDay)).getInputText();
    }

}
