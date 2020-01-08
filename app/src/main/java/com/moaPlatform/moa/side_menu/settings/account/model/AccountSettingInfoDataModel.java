package com.moaPlatform.moa.side_menu.settings.account.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

public class AccountSettingInfoDataModel extends CommonModel {
    private final String RECEIVE_YES = "Y";
    private final String RECEIVE_NO = "N";
    @SerializedName("acntStngInfo")
    private AccountSettingInfoModel accountSettingInfoModel;

    /**
     * 사용자의 이메일 정보 불러오기
     *
     * @return 이메일 정보 리턴
     */
    public String getEmail() {
        return accountSettingInfoModel.email;
    }

    /**
     * 사용자의 휴대전화 정보 불러오기
     *
     * @return 사용자의 휴대전화 정보 리턴
     */
    public String getPhoneNumber() {
        return accountSettingInfoModel.phoneNumber;
    }

    /**
     * 사용자의 이메일 수신 동의 여부 불러오기
     *
     * @return 사용자의 이메일 수신 여부 정보 리터 true : 수신동의, false : 미동의
     */
    public boolean isEmailReceive() {
        return accountSettingInfoModel.isEmailReceive();
    }

    /**
     * 사용자의 이메일 수신 동의 여부 불러오기
     *
     * @return 사용자의 SMS 수신 여부 정보 리터 true : 수신동의, false : 미동의
     */
    public boolean isSmsReceive() {
        return accountSettingInfoModel.isSmsReceive();
    }

    // 계정 설정 정보가 담긴 보델
    private class AccountSettingInfoModel extends AccountSettingReceiveModel {
        // 이메일
        @SerializedName("email")
        private String email;
        // 전화번호
        @SerializedName("mobiNum")
        private String phoneNumber;
    }

    // 계정 설정의 이메일 및 SMS 수신 동의 여부 정보가 담겨있는 모델
    public class AccountSettingReceiveModel extends CommonModel {
        // 이메일 수신 동의 여부
        @SerializedName("emailRecvAgrmntYn")
        String isEmailReceive;
        // SMS 수신 동의 여부
        @SerializedName("smsRecvAgrmntYn")
        String isSmsReceive;

        /**
         * SMS 및 이메일 수신 동의 여부 초기화
         *
         * @param emailReceive 이메일 수신 여부
         * @param smsReceive   SMS 수신 여부
         */
        public void init(boolean emailReceive, boolean smsReceive) {
            setIsEmailReceive(emailReceive);
            setIsSmsReceive(smsReceive);
        }

        /**
         * 이메일 수신 동의 여부 모델를 초기화
         *
         * @param emailReceive 이메일 수신 여부
         */
        public void setIsEmailReceive(boolean emailReceive) {
            this.isEmailReceive = emailReceive ? RECEIVE_YES : RECEIVE_NO;
        }

        /**
         * SMS 수신 동의 여부 모델를 초기화
         *
         * @param smsReceive SMS 수신 여부
         */
        public void setIsSmsReceive(boolean smsReceive) {
            this.isSmsReceive = smsReceive ? RECEIVE_YES : RECEIVE_NO;
        }

        /**
         * 사용자의 이메일 수신 동의 여부 불러오기
         *
         * @return 사용자의 이메일 수신 여부 정보 리터 true : 수신동의, false : 미동의
         */
        public boolean isEmailReceive() {
            return isEmailReceive.equals(RECEIVE_YES);
        }

        /**
         * 사용자의 이메일 수신 동의 여부 불러오기
         *
         * @return 사용자의 SMS 수신 여부 정보 리터 true : 수신동의, false : 미동의
         */
        public boolean isSmsReceive() {
            return isSmsReceive.equals(RECEIVE_YES);
        }
    }

}
