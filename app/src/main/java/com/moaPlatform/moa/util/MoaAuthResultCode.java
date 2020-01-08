package com.moaPlatform.moa.util;

import android.annotation.SuppressLint;

import com.moaPlatform.moa.R;

@SuppressLint("unused")
public enum MoaAuthResultCode {
    /**
     * 아이디 존재
     */
    COMMON_ID_EXIST("0x5011", R.string.msg_registered_id_exists),
    /**
     * 아이디 존재하지 않음
     */
    COMMON_ID_NOT_EXIST("0x5012", 0),
    /**
     * PIN 등록 성공
     */
    REGIST_PIN_SUCCESS("0x5021", R.string.msg_pin_register_success),
    /**
     * PIN 등록 실패
     */
    REGIST_PIN_FAIL("0x5022", R.string.msg_pin_register_fail),
    /**
     * 지문 등록 시, 인증 토큰 존재
     */
    REGIST_FINGER_AUTHTOKEN_EXIST("0x5031", R.string.msg_fingerprint_already_registered),
    /**
     * 지문 등록 시, 인증 토큰 존재하지 않음
     */
    REGIST_FINGER_AUTHTOKEN_NOT_EXIST("0x5032", 0),
    /**
     * 지문 등록 성공
     */
    REGIST_FINGER_SUCCESS("0x5033", R.string.msg_fingerprint_register_success),
    /**
     * 지문 등록 실패
     */
    REGIST_FINGER_FAIL("0x5034", R.string.msg_fingerprint_register_fail),
    /**
     * PIN 로그인 시, NONCE 검증 성공
     */
    LOGIN_PIN_NONCE_VERIFY("0x5041", 0),
    /**
     * PIN 로그인 시, NONCE 검증 실패
     */
    LOGIN_PIN_NONCE_NOT_VERIFY("0x5042", R.string.msg_nonce_verify_fail),
    /**
     * PIN 로그인 성공
     */
    LOGIN_PIN_SUCCESS("0x5043", R.string.msg_pin_login_success),
    /**
     * PIN 로그인 실패
     */
    LOGIN_PIN_FAIL("0x5044", R.string.msg_pin_login_fail),
    /**
     * 지문 로그인 성공
     */
    LOGIN_FINGER_SUCCESS("0x5051", R.string.msg_fingerprint_login_success),
    /**
     * 지문 로그인 실패
     */
    LOGIN_FINGER_FAIL("0x5052", R.string.msg_fingerprint_login_fail),
    /**
     * 회원탈퇴 성공
     */
    WITHDRAWAL_SUCCESS("0x5061", R.string.msg_unregister_member_success),
    /**
     * 회원탈퇴 실패
     */
    WITHDRAWAL_FAIL("0x5062", R.string.msg_unregister_member_fail),
    /**
     * 패스워드 재설정 성공
     */
    RESET_PW_SUCCESS("0x5071", R.string.msg_reset_pin_success),
    /**
     * 패스워드 재설정 실패
     */
    RESET_PW_FAIL("0x5072", R.string.msg_reset_pin_fail),
    /**
     * 패스워드 변경 시, 현재 비밀번호 검증 성공
     */
    CHANGE_PW_CURRENT_PW_VERIFY("0x5081", 0),
    /**
     * 패스워드 변경 시, 현재 비밀번호 검증 실패
     */
    CHANGE_PW_CURRENT_PW_NOT_VERIFY("0x5082", R.string.common_pw_not_match),
    /**
     * 패스워드 변경 성공
     */
    CHANGE_PW_SUCCESS("0x5083", R.string.msg_password_change_success),
    /**
     * 패스워드 변경 실패
     */
    CHANGE_PW_FAIL("0x5084", R.string.msg_password_change_fail),
    /**
     * 복원형 지갑 등록 성공
     */
    REGIST_RESTORE_WALLET_SUCCESS("0x5091", R.string.sign_up_msg_wallet_create_success),
    /**
     * 복원형 지갑 등록 실패
     */
    REGIST_RESTORE_WALLET_FAIL("0x5092", R.string.sign_up_msg_wallet_create_fail),
    /**
     * 복원형 지갑 등록 시, 서버에 등록된 지갑이 존재
     */
    REGIST_RESTORE_WALLET_EXIST("0x5093", R.string.sign_up_msg_wallet_already_exist),
    /**
     * 생성된 지갑 서명 검증 성공
     */
    REGIST_RESTORE_WALLET_VERIFY_SUCCESS("0x5095", 0),
    /**
     * 생성된 지갑 서명 검증 실패
     */
    REGIST_RESTORE_WALLET_VERIFY_FAIL("0x5096", 0),
    /**
     * 생성된 지갑 롤백 성공
     */
    REGIST_RESTORE_WALLET_ROLLBACK_SUCCESS("0x5097", 0),
    /**
     * 생성된 지갑 롤백 실패
     */
    REGIST_RESTORE_WALLET_ROLLBACK_FAIL("0x5098", 0),
    /**
     * 지갑 복원 시, 서버에 등록된 지갑 정보 가져오기 성공
     */
    RESTORE_WALLET_IMPORT_SUCCESS("0x509A", 0),
    /**
     * 지갑 복원 시, 서버에 등록된 지갑 정보 가져오기 실패
     */
    RESTORE_WALLET_IMPORT_FAIL("0x509B", 0),
    /**
     * 지갑 복원 성공
     */
    RESTORE_WALLET_SUCCESS("0x509C", R.string.sign_up_msg_wallet_restore_success),
    /**
     * 지갑 복원 실패
     */
    RESTORE_WALLET_FAIL("0x509D", R.string.sign_up_msg_wallet_restore_fail),
    /**
     * 지갑 복원 패스워드 검증 성공
     */
    RESTORE_WALLET_PSW_VERIFY_SUCCESS("0x509E", 0),
    /**
     * 지갑 복원 패스워드 검증 실패
     */
    RESTORE_WALLET_PSW_VERIFY_FAIL("0x509F", 0),
    /**
     * 지갑 패스워드 재설정을 위한 데이터 가져오기 성공
     */
    INIT_WALLET_PSW_DATA_IMPORT_SUCCESS("0x5111", 0),
    /**
     * 지갑 패스워드 재설정을 위한 데이터 가져오기 실패
     */
    INIT_WALLET_PSW_DATA_IMPORT_FAIL("0x5112", R.string.msg_wallet_init_password_fail),
    /**
     * 지갑 패스워드 재설정 관련 생년월일 유효성 검증 성공
     */
    INIT_WALLET_PSW_DATE_OF_BIRTH_VALIDATE("0x5113", 0),
    /**
     * 지갑 패스워드 재설정 관련 생년월일 유효성 검증 실패
     */
    INIT_WALLET_PSW_DATE_OF_BIRTH_NOT_VALIDATE("0x5114", R.string.msg_wallet_init_date_of_birth_not_validate),
    /**
     * 지갑 패스워드 재설정 서버에 업데이트 성공
     */
    INIT_WALLET_PSW_SERVER_UPDATE_SUCCESS("0x5115", 0),
    /**
     * 지갑 패스워드 재설정 서버에 업데이트 실패
     */
    INIT_WALLET_PSW_SERVER_UPDATE_FAIL("0x5116", R.string.msg_wallet_init_password_fail),
    /**
     * 패스워드 재설정된 지갑 생성 성공
     */
    INIT_WALLET_PSW_CREATE_SUCCESS("0x5117", 0),
    /**
     * 패스워드 재설정된 지갑 생성 실패
     */
    INIT_WALLET_PSW_CREATE_FAIL("0x5118", R.string.msg_wallet_init_password_fail),
    /**
     * 지갑 패스워드 재설정 관련 입력 패스워드 검증 성공
     */
    INIT_WALLET_PSW_INPUT_PSW_VALIDATE_SUCCESS("0x5119", 0),
    /**
     * 지갑 패스워드 재설정 관련 입력 패스워드 검증 실패
     */
    INIT_WALLET_PSW_INPUT_PSW_VALIDATE_FAIL("0x5120", 0),
    /**
     * 지갑 패스워드 재설정 관련 패스워드 동기화 성공
     */
    INIT_WALLET_PSW_SYNC_SUCCESS("0x5121", 0),
    /**
     * 지갑 패스워드 재설정 관련 패스워드 동기화 실패
     */
    INIT_WALLET_PSW_SYNC_FAIL("0x5122", R.string.msg_wallet_init_password_fail),
    /**
     * 지갑 패스워드 재설정 관련 서버에 등록된 지갑 롤백 성공
     */
    INIT_WALLET_PSW_ROLLBACK_SUCCESS("0x5123", R.string.msg_wallet_init_password_fail),
    /**
     * 지갑 패스워드 재설정 관련 서버에 등록된 지갑 롤백 실패
     */
    INIT_WALLET_PSW_ROLLBACK_FAIL("0x5124", 0),
    /**
     * 지갑 패스워드 재설정 성공
     */
    INIT_WALLET_PSW_SUCCESS("0x5125", R.string.msg_wallet_init_password_success);

    private String authCode;
    private int stringId;

    MoaAuthResultCode(
            String authCode,
            int stringId
    ) {
        this.authCode = authCode;
        this.stringId = stringId;
    }

    /**
     * 인증 결과 코드를 리턴한다.
     */
    public String getAuthCode() {
        return authCode;
    }

    /**
     * 인즐 결과에 따른 String ID를 리턴한다.
     */
    public int getStringId() {
        return stringId;
    }
}
