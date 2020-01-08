package com.moaPlatform.moa.util.interfaces;

import android.webkit.JavascriptInterface;

import com.moaPlatform.moa.util.Logger;

import org.json.JSONException;
import org.json.JSONObject;

public class JsBridge implements JsReceiver {
    private JsReceiver jsReceiver;

    public JsBridge(JsReceiver jsReceiver) {
        this.jsReceiver = jsReceiver;
    }

    @JavascriptInterface
    public void showHTML(String html) {
        if (html.indexOf('{') == -1) {
            onJsFail();
            return;
        }
        String resultJson = html.substring(html.indexOf('{'), html.indexOf('}') + 1);
        try {
            new JSONObject(resultJson);
        } catch (JSONException e) {
            Logger.d("Json Error\n" + e);
            return;
        }
        if (resultJson.length() == 0) {
            onJsFail();
            return;
        }
        onJsResultMsg(resultJson);
    }

    @Override
    public void onJsResultMsg(String resultMsg) {
        jsReceiver.onJsResultMsg(resultMsg);
    }

    @Override
    public void onJsFail() {
        jsReceiver.onJsFail();
    }
}
