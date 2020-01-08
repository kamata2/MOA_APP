package com.moaPlatform.moa.auth.sign_up.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

/**
 * Created by jiwun on 2019-06-05.
 *
 * 이메일 및 추천인 번호 존재 유무 체크시 사용할 req, res 모델
 */
public class ReqResExistsCheckModel extends CommonModel {

    // 사용할 이메일 및 추천인 번호
    @SerializedName("email")
    private String inputEmail;

    // 추천인 번호
    @SerializedName("mobiNum")
    private String recommenderNumber;

    // 이메일 존재 유무
    @SerializedName(value = "exitsResult", alternate = {"emailExists", "phoneNumExists"})
    private boolean dataExists;

    /**
     * @param inputEmail
     * 사용자가 사용할 이메일
     */
    public void setInputEmail(String inputEmail) {
        this.inputEmail = inputEmail;
    }

    /**
     * @param inputPhoneNumber
     * 추천인 핸드폰 번호
     */
    public void setInputPhoneNumber(String inputPhoneNumber) {
        this.recommenderNumber = inputPhoneNumber;
    }

    /**
     * 이메일 존재 유무 반환
     * @return
     * true -> 존재
     * false -> 미존재
     */
    public boolean isExistsEmail() {
        return dataExists;
    }

    /**
     * 추천인 핸드폰 번호 존재 유무
     * @return
     * true -> 존재
     * false -> 미존재
     */
    public boolean isExistsPhoneNumber() {
        return dataExists;
    }
}
