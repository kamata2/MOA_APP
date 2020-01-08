package com.moaPlatform.moa.util.singleton;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.moaPlatform.moa.bottom_menu.main.model.ResMainServiceModel;
import com.moaPlatform.moa.top_menu.location.model.ResAddressSearchBaseModel;

public class App {
    // 회원가입
    // 회원가입 성공
    public final int SIGN_UP_SUCCESS = 0;
    public String SUB_MENU_CODE = "1";
    public Gson gson = new Gson();
    public Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();
    // 유저 패스워드
    public String userPw = "";

    public ResMainServiceModel.UserInfoModel userInfoModel;

    public static App getInstance() {
        return LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static final App INSTANCE = new App();
    }

    public void defaultAddressChange(ResAddressSearchBaseModel.AddressHistoryModel addressHistoryModel) {
        userInfoModel.jibunAddress = addressHistoryModel.jibunAddress;
        userInfoModel.lnt = addressHistoryModel.lnt;
        userInfoModel.lat = addressHistoryModel.lat;
        userInfoModel.postNum = addressHistoryModel.postNumber;
        userInfoModel.roadDefaultAddress = addressHistoryModel.roadDefaultAddress;
        userInfoModel.roadDetailAddress = addressHistoryModel.roadDetailAddress;
    }
}
