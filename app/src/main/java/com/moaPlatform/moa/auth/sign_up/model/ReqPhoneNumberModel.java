package com.moaPlatform.moa.auth.sign_up.model;

import com.google.gson.annotations.SerializedName;
import com.moaPlatform.moa.util.models.CommonModel;

public class ReqPhoneNumberModel extends CommonModel {

	@SerializedName("mobiNum")
	public String phoneNumber ;
}