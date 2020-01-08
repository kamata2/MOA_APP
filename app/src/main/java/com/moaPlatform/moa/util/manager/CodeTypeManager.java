package com.moaPlatform.moa.util.manager;

import com.moaPlatform.moa.R;

public class CodeTypeManager {
    private CodeTypeManager() {
    }

    /**
     * 툴바에서 사용할 enum
     */
    public enum Toolbar {
        // 하단 툴바의 홈화면 클릭시
        BOTTOM_TOOLBAR_MAIN
    }

    /**
     * 서버 컨트롤러에서 서버와 통신후 반환값을 class 로 넘길때 사용
     */
    public enum RestApi {
        // 비회원 정보 생성
        REGISTER_NON_MEMBER,
        // 로그인 성공
        LOGIN_SUCCESS,
        // 로그아웃
        USER_LOGOUT,
        // 회원가입
        USER_SIGN_UP,
        // 회원 탈퇴
        MEMBERSHIP_WITHDRAWAL_SUCCESS,
        // 비회원 로그인
        NON_MEMBER_LOGIN,
        // 회원 로그인
        USER_LOGIN,
        // 지문 인식 로그인
        FINGER_LOGIN,
        // 비밀번호 변경 성공
        PASSWORD_CHANGE_SUCCESS,
        // 주소 저장
        ADDRESS_SAVE,
        // 위치 좌표 변환
        LOCATION_COORDINATE_CHANGE,
        // 주소 검색
        ADDRESS_SEARCH,
        // 최근 주소 검색 기록 조회
        LOAD_ADDRESS_HISTORY,
        // 장바구니 상세 정보
        SHOPPING_CART_DETAIL_INFO,
        // 쿠폰 등록
        REGISTER_COUPON

    }

    /**
     * class 끼리 통신시 사용할 코드 매니저
     */
    public enum ClassCodeManager {
        // 선택적 접근 권한
        SELECT_PERMISSION_DIALOG,
        @Deprecated
        // 로그인 성공
        LOGIN_SUCCESS,
        // 약관동의
        INTRO_USE_AGREEMENT,
        // 회원 탈퇴 성공
        MEMBERSHIP_WITHDRAWAL_SUCCESS,
        // 로그인 실패
        LOGIN_FAIL,
        // 회원 로그아웃
        USER_LOGOUT,
        // 비회원 로그인
        NON_MEMBER_LOGIN,
        // 회원 로그인
        USER_LOGIN,
        // 지갑 생성 완료
        WALLET_CREATE_SUCCESS,
        // 지갑 복원용 본인인증 화면 이동
        MOVE_WALLET_RESTORE_SELF_AUTH,
        //지문인증 로그인 성공
        FINGER_LOGIN,
        // 패스워드 변경 완료
        PASSWORD_CHANGE_SUCCESS,
        // 복원 실패
        WALLET_RESTORE_FAIL
    }

    /**
     * realm 에 데이터 입력시 사용할 코드값
     */
    public enum RealmCodeManager {
        // 인트로 화면 선택적 접근권한 다이얼로그 허용
        INTRO_SELECT_PERMISSION_SUCCESS(0),
        // 인트로 이용약관 동의 완료
        INTRO_USE_AGREEMENT_SUCCESS(1),
        // 주소 저장 완료
        LOCATION_SAVE_SUCCESS(2);

        private int type;

        RealmCodeManager(int type) {
            this.type = type;
        }

        public int getCode() {
            return type;
        }
    }


    /**
     * 상점 관련
     */
    public enum StoreInfo {
        STORE_ID(-1), // 가맹점 아이디
        AD_STORE_INFO(1),   // 가맹점 광고 ( MainActivity 에서 이런 음식은 어때요? 에서 사용)
        STORE_MENU_SELECT(2),
        STORE_PRODUCT_CODE(-1),
        GET_STORE_INFO(0),
        STORE_PRODUCT_ADD_OPTION_ADD(3),    // 메뉴 상세 페이지에서 추가옵션 추가할때 타입
        STORE_PRODUCT_ADD_OPTION_REMOVE(4), // 메뉴 상세 페이지에서 추가옵션 삭제할때 타입
        STORE_PRODUCT_DEFAULT_OPTION_CHANGE(5), // 메뉴 상세 페이지에서 기본 옵션 변경할때
        STORE_PRODUCT_COUNT(-1), // 상품 개수
        STORE_PRODUCT_DEFAULT_OPTION_ID(-1),
        // 장바구니에 담기 성공
        INPUT_SHOPPING_CART(2),
        // 장바구니 아이디
        SHOPPING_CART_ID(-1),
        // 즐겨찾기 추가
        STORE_ADD_BOOKMARK(6),
        // 즐겨찾기 삭제
        STORE_REMOVE_BOOKMARK(7),
        // 총 결제 금액
        STORE_TOTAL_PRICE(-1),
        // 배달비
        STORE_DELIVERY_PRICE(-1),
        // 주문 예택 적립 예정 포인트
        STORE_SAVE_RATE_POINT_TEXT(-1);

        private int type;

        StoreInfo(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }

    /**
     * 장바구니 관련 메니저
     */
    public enum ShoppingCart {
        // 장바구니 화면으로 이동
        INTENT_SHOPPING_CART_DETAIL_ACTIVITY(1),
        // 장바구니 삭제
        SHOPPING_CART_REMOVE_DIALOG_SHOW(2),
        // 장바구니 상세 정보 리스트
        SHOPPING_CART_DETAIL_INFO_LIST(3),
        // 장바구니 전체 삭제
        SHOPPING_CART_DETAIL_REMOVE_ALL(4),
        // 장바구니 개수 증가
        SHOPPING_CART_PRODUCT_PLUS(5),
        // 장바구니 개수 감소
        SHOPPING_CART_PRODUCT_MINUS(6),
        // 장바구니 상품 삭제
        SHOPPING_CART_ITEM_REMOVE(7),
        // 장바구니 개수
        SHOPPING_CART_COUNT(8);

        private int type;

        ShoppingCart(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }

    public enum ProductInfo {
        CNT,
        PRODUCT_CODE,
        PRODUCT_OPTION_ID,
        PRODUCT_ADD_OPTION
    }

    /**
     * 인트로 관련 매니저
     */
    public enum IntroManager {
        // 선택적 접근 권한 안내 다이얼로그 허용
        DIALOG_SUCCESS(0),
        // 이용약관 동의 완료
        TERMS_AND_CONDITIONS_AGREEMENT(1),
        // 위치 저장 완료
        LOCATION(2),
        // 로그인 완료
        LOGIN_SUCCESS(3);

        private int type;

        IntroManager(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }

    public enum StoreProductManager {
        SOTORE_PRODUCT_OPTION(0),
        STORE_PRODUCT_ADD_OPTION(1);

        private int type;

        StoreProductManager(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }

    /**
     * 주소 관련
     */
    public enum AddressManager {

        LOCATION_COORDINATE(0),
        ADDRESS_SEARCH(1),
        ADDRESS_SAVE(2),
        // 검색할 주소
        //SEARCH_ADDRESS_KEYWORD(-1),
        // 기본 주소 및 최근 주소 불러오기
        LOAD_ADDRESS_HISTORY(3),
        // 주소 삭제
        ADDRESS_REMOVE(4),
        // 기본 주소 변경
        DEFAULT_ADDRESS_CHANGE(5);

        private int type;

        AddressManager(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }

    /**
     * 상단 툴바 관련
     */
    public enum TopToolbarManager {
        // 뒤로 가기
        BACK_BUTTON_PRESS(0),
        // 주소 검색
        NOW_LOCATION_SEARCH(1),
        // 사이드 메뉴로 이동
        SUB_MENU_MOVE(2),
        // 가맹점 검색
        STORE_SEARCH(3),
        // 최근 검색어 조회
        RECENTLY_STORE_SEARCH(4),
        // 최근 검색어 단일 삭제
        RECENTLY_SEARCH_HISTORY_REMOVE(5),
        // 최근 검색어 전체 삭제
        RECENTLY_SEARCH_HISTORY_ALL_REMOVE(6),
        // 가맹점 키워드로 검색
        STORE_KEYWORD_SEARCH(7),
        // 가맹점 상세 페이지로 이동
        STORE_DETAIL_MOVE(8);

        private int type;

        TopToolbarManager(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }

    /**
     * 메인 화면에서 사용할 매니저
     */
    public enum MainActivityManager {
        // 메인 화면의 배달 카테고리 데이터
        MAIN_DELIVERY_CATAGORY_DATA(-1),
        MAIN_SERVICE_INFO(0),
        SUB_MENU_KOREAN(1),
        DELIVERY_SUB_MENU_KOREAN_IC(R.drawable.menuicon_3),
        DELIVERY_SUB_MENU_KOREAN_IC_KO(R.drawable.circle_sub_menu_edge_type_five),
        SUB_MENU_CHINESE(2),
        DELIVERY_SUB_MENU_CHINESE_IC(R.drawable.menuicon_4),
        SUB_MENU_JP(4),
        DELIVERY_SUB_MENU_JP_IC(R.drawable.menuicon_5),
        SUB_MENU_WESTERN(8),
        DELIVERY_SUB_MENU_WESTERN_IC(R.drawable.menuicon_6),
        SUB_MENU_CHICKEN(16),
        DELIVERY_SUB_MENU_CHICKEN_IC(R.drawable.menuicon_7),
        SUB_MENU_PIG_HOCKS(32),
        DELIVERY_SUB_MENU_PIG_HOCKS_IC(R.drawable.menuicon_8),
        SUB_MENU_LATE_NIGHT_SNACK(64),
        DELIVERY_SUB_MENU_LATE_NIGHT_SNACK_IC(R.drawable.menuicon_11),
        SUB_MENU_INSTANT_FOOD(128),
        DELIVERY_SUB_MENU_INSTANT_FOOD_IC(R.drawable.menuicon_14),
        SUB_MENU_FAST_FOOD(256),
        DELIVERY_SUB_MENU_FAST_FOOD_IC(R.drawable.menuicon_9),
        SUB_MENU_CAFE(512),
        DELIVERY_SUB_MENU_CAFE_IC(R.drawable.menuicon_2),
        SUB_MENU_FRANCHISE(1024),
        DELIVERY_SUB_MENU_FRANCHISE_IC(R.drawable.menuicon_10),
        CATEGORY_DELIVERY(1),
        CATEGORY_EAT_OUT(2),
        CATEGORY_COMMUNITY(3);

        private int type;

        MainActivityManager(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }

    public enum DeliveryStoreManager {

        SUB_MENUS(-1);

        private int type;

        DeliveryStoreManager(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

    }

    public enum UtilManager {
        ITEM_CLICK_POSITION
    }

    /**
     * 결제 관련 코드
     */
    public enum PaymentCode {
        PAYMENT_ORDER_ID("-1"),
        ORDER_SEPA_CD_DELIVERY("01"), // 배달타입
        ORDER_SEPA_CD_TAKE_OUT("09"), // 방문수령 타입
        DEAL_SEPA_CD_RIGHT_ORDER("01"), // 바로 주문
        DEAL_SEPA_CD_RESERVATION_ORDER("02"), // 예약주문
        PAY_MTHD_CD_RIGHT_PAYMENT("01"), // 바로 결제
        PAY_MTHD_CD_MEET_PAYMENT("02"), // 현장 결제
        PAY_MTHD_SEPA_CD_MOA_PAY("05"), // 모아 페이
        PAY_MTHD_SEPA_CD_CARD("01"), // 신용 카드
        PAY_MTHD_SEPA_CD_CASH("03"), // 현금 결제
        EASY_PAY_TYPE("-1"),
        // 신용카드 결제
        EASY_PAY_TYPE_CREDIT_CARD("11");

        private String type;

        PaymentCode(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

}
