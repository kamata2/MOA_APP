package com.moaPlatform.moa.auth.sign_up.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.bottom_menu.main.model.ResMainServiceModel;
import com.moaPlatform.moa.util.models.CommonModel;

/**
 * Created by jiwun on 2019-07-11.
 */
public class ResLoginActivityUserInfoShellModel extends CommonModel {

    @SerializedName("resultUserInfo")
    public ResMainServiceModel.UserInfoModel userInfoModel;

}
