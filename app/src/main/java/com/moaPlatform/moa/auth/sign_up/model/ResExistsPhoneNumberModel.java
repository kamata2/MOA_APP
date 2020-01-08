package com.moaPlatform.moa.auth.sign_up.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

public class ResExistsPhoneNumberModel extends CommonModel {

    @SerializedName("phoneNumExists")
    public boolean phoneNumExists;          //번호 존재유무

    @SerializedName("email")
    public String email;
}