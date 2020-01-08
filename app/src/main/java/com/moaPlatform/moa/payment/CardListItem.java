package com.moaPlatform.moa.payment;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CardListItem {

    @SerializedName("cardList")
    private List<CardListItem> cardList;

    @SerializedName("CNO")
    private String cNO;

    @SerializedName("BILL_KEY")
    private String bILLKEY;

    @SerializedName("USE_FLAG")
    private String uSEFLAG;

    @SerializedName("CARD_TOKEN")
    private String cARDTOKEN;

    @SerializedName("MEMBER_ID")
    private String mEMBERID;

    @SerializedName("SECRET_KEY")
    private String sECRETKEY;

    @SerializedName("ISSUER_NM")
    private String iSSUERNM;

    @SerializedName("SEQ")
    private int sEQ;

    @SerializedName("ISSUER_CD")
    private String iSSUERCD;

    @SerializedName("CARD_NICK")
    private String cARDNICK;

    public String getCARDTOKEN() {
        return cARDTOKEN;
    }

    public String getCARDNICK() {
        return cARDNICK;
    }

    public String getCARDNUMBER() {
        return cNO;
    }

    public String getiSSUERCD() {
        return iSSUERCD;
    }
}