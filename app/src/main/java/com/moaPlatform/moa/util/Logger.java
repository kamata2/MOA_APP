package com.moaPlatform.moa.util;

import android.util.Log;

import com.moaPlatform.moa.BuildConfig;
import com.moaPlatform.moa.util.singleton.App;

public class Logger {
    static final String TAG = BuildConfig.APPLICATION_ID + "Log";

    /** Log Level Error **/
    public static void e(String message) {
        if (BuildConfig.DEBUG)Log.e(TAG, buildLogMsg(message));
    }
    /** Log Level Warning **/
    public static void w(String message) {
        if (BuildConfig.DEBUG)Log.w(TAG, buildLogMsg(message));
    }
    /** Log Level Information **/
    public static void i(String message) {
        if (BuildConfig.DEBUG)Log.i(TAG, buildLogMsg(message));
    }
    /** Log Level Debug **/
    public static void d(String message) {
        if (BuildConfig.DEBUG)Log.d(TAG, buildLogMsg(message));
    }
    /** Log Level Verbose **/
    public static void v(String message) {
        if (BuildConfig.DEBUG)Log.v(TAG, buildLogMsg(message));
    }
    /** Log Level Debug **/
    public static void toJson(String message, Object object) {
        if (BuildConfig.DEBUG)Log.d(TAG, message + " : \n" + buildLogMsg(App.getInstance().gsonBuilder.toJson(object)));
    }
    /** Log Level Debug **/
    public static void toJson(Object object) {
        if (BuildConfig.DEBUG)Log.d(TAG, buildLogMsg(App.getInstance().gsonBuilder.toJson(object)));
    }

    /**
     * max entry is 5120b, max payload is 4076b
     * @param message
     */
    public static void debug(String message){
        if(message != null){
            String temp = message;
            try {
                while (temp.length() > 0) {
                    if (temp.length() > 4000) {
                        d(temp.substring(0, 4000));
                        temp = temp.substring(4000);
                    } else {
                        d(temp);
                        break;
                    }
                }
            } catch (Exception e) {
                e("Exception" + e.toString());
            }
        }
    }

    private static String buildLogMsg(String message) {

        StackTraceElement ste = Thread.currentThread().getStackTrace()[4];

        StringBuilder sb = new StringBuilder();

        sb.append("[");
        sb.append(ste.getFileName().replace(".java", ""));
        sb.append("::");
        sb.append(ste.getMethodName());
        sb.append(" ");
        sb.append(ste.getLineNumber());
        sb.append("]");
        sb.append(message);

        return sb.toString();

    }

}
