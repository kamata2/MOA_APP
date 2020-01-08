package com.moaPlatform.moa.wallet;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.util.custom_view.CommonEditTextErrorMsgView;

import org.moa.auth.userauth.android.api.MoaAuthHelper;
import org.moa.wallet.android.api.MoaWalletHelper;

import java.util.HashMap;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD;

public class WalletPswInitFragment extends Fragment {
    private CommonEditTextErrorMsgView viewWalletPassword;
    private CommonEditTextErrorMsgView viewWalletBirthDay;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet_psw_init, container, false);

        Button btnJoin = view.findViewById(R.id.btnJoin);

        // 전자 지갑 비밀번호 세팅
        viewWalletPassword = view.findViewById(R.id.viewWalletPassword);
        viewWalletPassword.setTitle(R.string.sign_up_wallet_pw);
        viewWalletPassword.editTextInit("숫자 6자리를 입력해 주세요", TYPE_CLASS_NUMBER | TYPE_NUMBER_VARIATION_PASSWORD);
        viewWalletPassword.setInputTextMaxSize(6);
        viewWalletPassword.setNotEmpty(true);

        // 전자 지갑 생년월일 입력 화면
        viewWalletBirthDay = view.findViewById(R.id.viewWalletBirthDay);
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

            if (viewWalletBirthDay.getInputText().length() != 8) {
                viewWalletBirthDay.setErrorMsg("생년월일(예 : 19840102 )을 입력해 주세요.");
                viewWalletBirthDay.errorMsgShow();
                return;
            } else {
                viewWalletBirthDay.errorMsgHidden();
            }
            doInitWalletPsw();
        });
        return view;
    }

    private void doInitWalletPsw() {
        HashMap<String, String> walletData = new HashMap<>();
        if (getArguments() != null) {
            walletData.put("encryptedHmacPsw", getArguments().getString("encryptedHmacPsw"));
            walletData.put("encPrk", getArguments().getString("encPrk"));
            walletData.put("encPukSalt", getArguments().getString("encPukSalt"));
        }
        walletData.put("id", MoaAuthHelper.getInstance().getCurrentMemberID());
        walletData.put("psw", viewWalletPassword.getInputText());
        walletData.put("dateOfBirth", viewWalletBirthDay.getInputText());
        MoaWalletHelper.getInstance().generateWalletInitMsg(walletData);
        // 10.8 서버에 MoaPayPsw, EncryptedHmacPsw, E(HmacPuk), E(Prk)$E(Puk)$Salt 전달

        // 10.14 서명 생성 및 검증

        // 10.15 서버에 서명 검증 결과 및 salt 동기화 체크

        // 최종 통과 시 해당 fragment 종료
    }
}
