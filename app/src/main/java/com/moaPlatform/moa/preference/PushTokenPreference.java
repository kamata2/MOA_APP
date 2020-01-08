package com.moaPlatform.moa.preference;

import android.content.Context;

/**
 * Created by park chan
 * 푸시 관련 Preference
 */

public class PushTokenPreference {

    private static final String PPREFERENCE_PUSH_TOKEN = "PPREFERENCE_PUSH_TOKEN";

    private static final String KEY_PUSH_TOKEN_IS_UPLOAD = "KEY_PUSH_TOKEN_IS_UPLOAD";

    private static class SingletonHolder {
        public static final PushTokenPreference INSTANCE = new PushTokenPreference();
    }

    public static PushTokenPreference getInstance() {
        return SingletonHolder.INSTANCE;
    }

    //서버로 푸시키를 업로드 성공하였는지 여부 체크
    public Boolean isUpload(Context context) {
        return PreferenceUtils.getBooleanPreference(context, PPREFERENCE_PUSH_TOKEN, KEY_PUSH_TOKEN_IS_UPLOAD);
    }

    public void setUpload(Context context, boolean isUpload) {
        PreferenceUtils.setPreference(context, PPREFERENCE_PUSH_TOKEN, KEY_PUSH_TOKEN_IS_UPLOAD, isUpload);
    }
}
