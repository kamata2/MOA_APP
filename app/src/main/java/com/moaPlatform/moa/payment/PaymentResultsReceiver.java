package com.moaPlatform.moa.payment;

import java.util.List;

public interface PaymentResultsReceiver {
    void onLoadCompleteCardList(List<CardListItem> cardListItems);

    void onReadyJsonData(String jsonData);

    void onSuccessPayment();

    void onFailPayment(String msg);

    void onForwardInitPswNonce(String nonce);

    void onNotMatchedPw();
}