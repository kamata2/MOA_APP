package com.moaPlatform.moa.util.interfaces;

public class ServerSideMsg {
    private ServerSideMsg(){}

    public static final String SUCCESS = "RESULT_CD_001";
    public static final String FAIL = "RESULT_CD_002";
    public static final String RELOGIN = "RESULT_CD_003";
    public static final String LOGOUT = "RESULT_CD_004";
    public static final String SUCCESS_MOAPAY = "200";
    public static final String FAIL_MOAPAY_NOT_MATCH_PW = "401";
    public static final String SUCCESS_EASYPAY = "0000";
    public static final String SUCCESS_KG = "0000";
    public static final String RESULT_MSG_KG = "resultMsg";

    // 쿠폰 번호가 존재하지 않음
    public static final String COUPON_NOT_EXIST = "EVNT_RET_CD_001";
    // 이미 등록된 쿠폰
    public static final String COUPON_REGISTED = "EVNT_RET_CD_002";
    // 이미 사용된 쿠폰
    public static final String COUPON_USED = "EVNT_RET_CD_003";
    // 지갑 복원할 필요가 없는 경우 [지갑주소가 일치 (Server == Client)]
    public static final String RESULT_WALLET_NO_NEED_TO_RESTORE = "RESULT_WALLET_NO_NEED_TO_RESTORE";
}
