package com.moaPlatform.moa.auth.sign_up.model;

import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 사용자가 입력한 개인정보 관련 처리 모델
 * 이메일, 비밀번호
 */
public class PersonalInformationModel {

    /**
     * 이메일 데이터 체크
     * @param email
     * 이메일 입력창
     * @return
     * 이메일 형식이 아니라면 true, 이메일 형식이면 false 를 반환
     */
    public boolean emailCheck(EditText email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email.getText().toString());
        return !matcher.matches();
    }

    /**
     * 비밀번호 데이터 체크
     * @param password
     * 비밀번호 입력창
     * @return
     * 비밀번호 데이터가 공백이고 숫자 8미만 true 반환 그 외는 false 반환
     */
    public boolean passwordCheck(EditText password) {
        String passwordText = password.getText().toString();
        return passwordText.replace(" ","").equals("") || passwordText.length() < 8;
    }

    /**
     * 비밀번호 확인
     * @param password
     * 비밀번호 입력창
     * @param passwordCheck
     * 비밀번호 확인 입력창
     * @return
     * 비밀번호와 비밀번호 확인의 데이터가 일치하면 true 반환 그 외는 false 반환
     */
    public boolean passwordOneMoreCheck(EditText password, EditText passwordCheck) {
        return !password.getText().toString().equals(passwordCheck.getText().toString());
    }

}
