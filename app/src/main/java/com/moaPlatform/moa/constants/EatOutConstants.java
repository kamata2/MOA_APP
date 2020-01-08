package com.moaPlatform.moa.constants;

public class EatOutConstants {

    /**
     * 메인 메뉴 [외식]
     * 리스트 요청시 Request Type 구분
     */
    public enum EATOUT_LIST_REQUEST_TYPE {

        REQUEST_TYPE_BANNER("0"),
        REQUEST_TYPE_THEME("1"),
        REQUEST_TYPE_STORE_LIST("2");

        private String type;

        EATOUT_LIST_REQUEST_TYPE(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    /**
     * 메인 메뉴 [외식]
     * 리스트 아이템 선택시 화면 이동 구분 정의
     * 웹뷰|상세화면|리스트 화면
     */
    public enum EATOUT_ITEM_ACTION_TYPE {

        ACTION_TYPE_WEBVIEW("01"),
        ACTION_TYPE_SHOP_DETAIL("02"),
        ACTION_TYPE_SHOP_LIST("03");

        private String type;

        EATOUT_ITEM_ACTION_TYPE(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    /**
     * 메인 메뉴 [외식]
     * Adapter에서 ClickListener 구분을 위한 타입 정의
     */
    public enum LIST_CLICK_LISTENER_TYPE {
        EATOUT_LIST_STORE(1),              //외식 상점
        EATOUT_LIST_THEME(2);              //외식 테마

        private int type;

        LIST_CLICK_LISTENER_TYPE(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }

    /**
     * 다음지도 이동 타입
     */
    public enum SEARCH_MAP_TYPE {

        FOOT("FOOT"),
        CAR("CAR");

        private String type;

        SEARCH_MAP_TYPE(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    /**
     * 외식 상세 화면
     * 즐겨 찾기 Y/N 아이콘 처리 0:N | 1:Y
     */
    public enum BOOK_MARK_TYPE {

        N("0"),
        Y("1");

        private String type;

        BOOK_MARK_TYPE(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

}
