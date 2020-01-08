package com.moaPlatform.moa.util.interfaces;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

public interface TextSettingHtmlFormat {

    /**
     * 버전별로 html.fromHtml 사용시 리턴 방식 변경
     * @param text
     * 적용할 text
     * @return
     * Html.fromHtml 형식으로 바뀐 값
     */
    default Spanned getChangeHtmlFormat(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(text);
        }
    }
}
