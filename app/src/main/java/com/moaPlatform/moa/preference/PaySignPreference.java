package com.moaPlatform.moa.preference;

import android.content.Context;

/**
 * Created by park chan
 * 카드 비밀번호 Preference
 */

public class PaySignPreference {

    private static final String PPREFERENCE_PAY_SIGN = "PPREFERENCE_PAY_SIGN";

    private static final String KEY_WALLET_PASSWORD = "KEY_WALLET_PASSWORD";

    private static class SingletonHolder {
        public static final PaySignPreference INSTANCE = new PaySignPreference();
    }

    public static PaySignPreference getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public String getWalletPassword(Context context) {
        return PreferenceUtils.getStringPreference(context, PPREFERENCE_PAY_SIGN, KEY_WALLET_PASSWORD);
    }

    public void setWalletPassword(Context context, String password) {
        try{
            PreferenceUtils.setPreference(context, PPREFERENCE_PAY_SIGN, KEY_WALLET_PASSWORD, password);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
