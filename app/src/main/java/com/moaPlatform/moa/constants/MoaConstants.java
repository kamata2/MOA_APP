package com.moaPlatform.moa.constants;

public class MoaConstants {


    /**
     * webView 관련 [Start]
     */
    public static final String WEBVIEW_USER_AGENT ="APP_ANDROID";
    public static final String WEBVIEW_ACTION_LOGIN_ACTIVITY ="loginView";      //로그인 화면으로 이동

    /**
     * webView 관련 [End]
     */

    public static final int REQUEST_CARD_LIST_RETRY_MAX_CNT = 3;        //카드 리스트 재요청 최대 횟수
    public static final int REQUEST_CARD_LIST_RETRY_TIME = 1500;        //요청 재시도 시간

    //extra type
    public static final String EXTRA_FROM_VIEW = "EXTRA_FROM_VIEW";
    public static final String EXTRA_TO_VIEW = "EXTRA_TO_VIEW";
    public static final String EXTRA_STORE_ID = "EXTRA_STORE_ID";
    public static final String EXTRA_STORE_LIST_POSITION = "EXTRA_STORE_LIST_POSITION";             //리스트 Adapter 포지션
    public static final String EXTRA_STORE_INFO_CHANGED_DATA = "EXTRA_STORE_INFO_CHANGED_DATA";     //상점 정보가 변경된것이 있으면 넘기는 data key값
    public static final String EXTRA_ORDER_ID = "EXTRA_ORDER_ID";
    public static final String EXTRA_STORE_NAME = "EXTRA_STORE_NAME";
    public static final String EXTRA_TITLE_NAME = "EXTRA_TITLE_NAME";
    public static final String EXTRA_WALLET_CARD_TERMS_AND_CONDITIONS_AGREEMENT = "EXTRA_WALLET_CARD_TERMS_AND_CONDITIONS_AGREEMENT";       //카드 이용약관 동의 여부
    public static final String EXTRA_WALLET_PASSWORD = "EXTRA_WALLET_PASSWORD";       //카드 비밀번호

    //onActivityResult requestCode ===============================[START]===============================
    public static final int REQUEST_USER_JOIN = 1000;           //회원가입
    public static final int REQUEST_USER_LOGIN = 2000;          //로그인

    public static final int REQUEST_STORE_DETAIL = 3000;       //스토어 리스트에서 요청

    public static final int REQUEST_GALLELY = 6001;
    public static final int REQUEST_CAMERA = 6002;

    public static final int REQUEST_REVIEW_MODIFY = 7000;       //리뷰 수정 요청

    public static final int REQUEST_WALLET_ACTIVITY = 8002;
    public static final int REQUEST_IDENTITY_ACTIVITY = 8003;
    public static final int REQUEST_WALLET_SETTING_PW_ACTIVITY = 8004;
    public static final int REQUEST_WALLET_PAY_ACTIVITY = 8005;
    public static final int REQUEST_WALLET_PASSWORD_INPUT_ACTIVITY = 8006;
    public static final int REQUEST_WALLET_SETTING_ACTIVITY = 8007;
    public static final int REQUEST_WALLET_POINT_EXCHANGE = 8008;
    public static final int REQUEST_WALLET_CARD_TERMS_AND_CONDITIONS_AGREEMENT = 8009;      //월렛 이용약관 동의
    public static final int REQUEST_WALLET_QR_CODE_MAIN_ACTIVITY = 8010;                    //QR CODE 촬영 화면
    public static final int REQUEST_WALLET_QR_CODE_INPUT_DATA_ACTIVITY = 8011;              //QR CODE 결제 화면 페이지

    public static final int REQUEST_REVIEW_WRITE = 9001;
    public static final int REQUEST_REVIEW_LIST = 9002;
    public static final int REQUEST_REVIEW_DETAIL = 9003;
    //onActivityResult requestCode ===============================[END]===============================

    //onActivityResult resultCode ===============================[START]===============================
    public static final int RESULT_USER_JOIN_SUCCESS = 1100;                //회원가입 성공
    public static final int RESULT_USER_JOIN_FAIL = 1200;                   //회원가입 실패

    public static final int RESULT_LOGIN_SUCCESS = 2100;                 //로그인 성공
    public static final int RESULT_LOGIN_FAIL = 2200;                    //로그인 실패

    public static final int RESULT_STORE_DETAIL_INFO_CHANGED = 3100;      //상점 상세 정보 변경됨

    public static final int RESULT_REVIEW_MODIFY_SUCCESS = 7100;            //리뷰 수정 요청 성공
    public static final int RESULT_REVIEW_MODIFY_FAIL = 7300;               //리뷰 수정 요청 실패

    public static final int RESULT_CARDREGISTER_SUCCESS = 8102;             //카드 등록 성공
    public static final int RESULT_CARDREGISTER_FAIL = 8202;                //카드 등록 실패
    public static final int RESULT_CARDREGISTER_CANCEL = 8302;              //카드 등록 취소
    public static final int RESULT_WALLET_PASSWORD_INPUT_ACTIVITY = 8106;
    public static final int RESILT_WALLET_QR_CODE_INPUT_DATA_ACTIVITY_CARD_LIST_CHANGED = 8111;         //QR CODE 결제 화면 페이지 카드리스트 변경 여부(추가,삭제)

    public static final int RESULT_REVIEW_WRITE_SUCCESS = 9101;             //리뷰 쓰기 성공
    public static final int RESULT_REVIEW_WRITE_FAIL = 9201;                //리뷰 쓰기 실패
    public static final int RESULT_REVIEW_LIST_NOTIFYCHANGED = 9302;        //리뷰 리스트 항목 변경
    //onActivityResult resultCode ===============================[END]===============================


    //request params key
    public static final String PARAM_STORE_ID = "storId";                   //가맹점 id
    public static final String PARAM_USER_ID = "userId";
    public static final String PARAM_SERVICE_SCORE = "srvcEvalScor";        //서비스 평점
    public static final String PARAM_DELIVERY_SCORE = "dlvryEvalScor";      //배달 평점
    public static final String PARAM_ORDER_ID = "orderId";
    public static final String PARAM_TASTE_SCORE = "tastEvalScor";          //맛 평점
    public static final String PARAM_AMOUNT_SCORE = "volumEvalScor";        //양 평점
    public static final String PARAM_CONTENT = "cntnt";                     //내용
    public static final String PARAM_KAKAO_LINK_ACTION_VIEW = "actionView";
    public static final String PARAM_KAKAO_LINK_STORE_ID = "storeId";
    public static final String PARAM_KAKAO_LINK_URL = "url";
    public static final String PARAM_SAVE = "save";                         //적립
    public static final String PARAM_USE = "use";                           //사용
    //request params key 리뷰 수정
    public static final String PARAM_SEPAVALUE = "sepaValue";               //외식,배달 Type 구분
    public static final String PARAM_USERREVWID = "userRevwId";
    public static final String PARAM_FILE_1 = "file";
    public static final String PARAM_FILE_2 = "file2";
    public static final String PARAM_FILE_3 = "file3";
    public static final String PARAM_FILE_1_UPDATE = "fileKey";
    public static final String PARAM_FILE_2_UPDATE = "file2Key";
    public static final String PARAM_FILE_3_UPDATE = "file3Key";


    //request params value
    public static final String PARAM_VALUE_DELIVERY = "delivery";       //배달
    public static final String PARAM_VALUE_EATOUT = "eatOut";           //외식

    //scheme
    public static final String SCHEME_ACTION_EATOUT_DEATIL = "SCHEME_ACTION_EATOUT_DEATIL";
    public static final String SCHEME_ACTION_STORE_DEATIL = "SCHEME_ACTION_STORE_DEATIL";

    public static final int LOADING_BAR_VISIBLE_TIME = 1000;

    public static final int MOA_COIN_DECIMAL_LENGTH = 19;       //모아 코인 소수점 제한 18자리

    //리스트 검색 필터 타입
    public enum ListFilter {

        PARAM_VALUE_LIST_FILTER_DISTANCE("1"),
        PARAM_VALUE_LIST_FILTER_GRADE("2"),
        PARAM_VALUE_LIST_FILTER_SAVING("3"),
        PARAM_VALUE_LIST_FILTER_EVENT("4"),
        PARAM_VALUE_LIST_FILTER_REVIEW("8"),
        PARAM_VALUE_LIST_FILTER_FAVOR("5"),
        PARAM_VALUE_LIST_FILTER_TAKE_OUT("6"),
        PARAM_VALUE_LIST_FILTER_OWNER_REPLY("7");

        private String order;

        ListFilter(String order) {
            this.order = order;
        }

        public String getOrder() {
            return order;
        }
    }
}
