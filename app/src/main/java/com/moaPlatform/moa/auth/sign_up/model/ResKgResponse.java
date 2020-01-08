package com.moaPlatform.moa.auth.sign_up.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class ResKgResponse {

    @SerializedName("siteurl")
    private String siteurl;

    @SerializedName("payMode")
    private String payMode;

    @SerializedName("keygb")
    private String keygb;

    @SerializedName("ciSvcid")
    private String ciSvcid;

    @SerializedName("cryptyn")
    private String cryptyn;

    @SerializedName("callType")
    private String callType;

    @SerializedName("cashGb")
    private String cashGb;

    @SerializedName("closeurl")
    private String closeurl;

    @SerializedName("ciMode")
    private String ciMode;

    @SerializedName("mstr")
    private String mstr;

    @SerializedName("logoYn")
    private String logoYn;

    @SerializedName("okurl")
    private String okurl;

    @SerializedName("tradeid")
    private String tradeid;

    public String getMstr() {
        return mstr;
    }

    public void setMstr(String mstr) {
        this.mstr = mstr;
    }

    public String toJsonString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("siteurl", siteurl);
            jsonObject.put("payMode", payMode);
            jsonObject.put("keygb", keygb);
            jsonObject.put("ciSvcid", ciSvcid);
            jsonObject.put("cryptyn", cryptyn);
            jsonObject.put("callType", callType);
            jsonObject.put("cashGb", cashGb);
            jsonObject.put("closeurl", closeurl);
            jsonObject.put("ciMode", ciMode);
            jsonObject.put("mstr", mstr);
            jsonObject.put("logoYn", logoYn);
            jsonObject.put("okurl", okurl);
            jsonObject.put("tradeid", tradeid);
        } catch (JSONException e) {
            throw new RuntimeException("Json Exception : " + e.getMessage());
        }
        return jsonObject.toString();
    }

}