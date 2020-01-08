package com.moaPlatform.moa.bottom_menu.main.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.BaseModel;
import com.moaPlatform.moa.util.models.CommonModel;
import com.moaPlatform.moa.util.models.StoreInfoModel;

import java.io.Serializable;
import java.util.ArrayList;

public class ResMainServiceModel extends CommonModel {

    // 동 이름
    @SerializedName("dongNm")
    public String emdNm;
    // 카테고리 리스트 모델
    @SerializedName("homeCategoryList")
    public ArrayList<CategoryModel> categoryModels;
    // 유저 정보
    @SerializedName("userInfo")
    public UserInfoModel userInfoModel;
    // 배달 데이터
    @SerializedName("dlvryData")
    public MainDeliveryModel mainDeliveryModel;

    //외식 데이터
    @SerializedName("eatOutData")
    public EatOutMainModel eatOutMainModel;

    /**
     * 배달 정보가 담길 클래스
     */
    public class MainDeliveryModel {
        // 광고 등록한 가맹점 리스트 (이런 음식은 어때요?)
        @SerializedName("adStorList")
        public ArrayList<StoreInfoModel> adStoreList;
        // 서브 메뉴 리스트
        @SerializedName("subCategoryList")
        public ArrayList<SubMenuModel> subMenuModelList;
        // 우리동네 새로운 맛집
        @SerializedName("newStorBanner")
        public BannerInfoModel newStoreInfoBanner;
        // 우리동네 소문난 맛집
        @SerializedName("famousStorRankBanner")
        public BannerInfoModel famousStoreBanner;
    }

    /**
     * 배너 관련 모델
     */
    public class BannerInfoModel {
        // 베너 타이틀
        @SerializedName("bannerTitle")
        public String bannerTitle;
        // 베너 이미지
        @SerializedName("bannerImgUrl")
        public String bannerThumbnail;
    }
    /**
     * 카테고리 정보 저장하는 모델
     */
    public class CategoryModel {
        // 카테고리 코드
        @SerializedName("srvcCtgryCd")
        public String categoryCode;
        // 카테고리 이름
        @SerializedName("srvcCtgryNm")
        public String categoryName;
    }

    /**
     * 서브 메뉴 정보 저장하는 모델
     */
    public class SubMenuModel extends CommonModel implements Serializable {
        // 서브메뉴 코드
        @SerializedName("subMenuCd")
        public String subMenuCode;
        // 서브메뉴 이름
        @SerializedName("subMenuNm")
        public String subMenuName;
    }

    /**
     * 사용자 정보
     */
    public class UserInfoModel extends BaseModel {
        // 휴대 전화 번호
        @SerializedName("mobiNum")
        public String phoneNumber;
        // 우편 번호
        @SerializedName("postNum")
        public String postNum;
        // 도로명 기본 주소
        @SerializedName("roadNmDefltAddr")
        public String roadDefaultAddress;
        // 도로명 상세 주소
        @SerializedName("roadNmDtlAddr")
        public String roadDetailAddress;
        // 지번 주소
        @SerializedName("landNumAddr")
        public String jibunAddress;
        // 경도
        @SerializedName("lont")
        public String lnt;
        // 위도
        @SerializedName("latu")
        public String lat;
    }
}
