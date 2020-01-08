package com.moaPlatform.moa.util;


import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class DeviceUtil {

    public static final String LDPI = "LDPI";
    public static final String MDPI = "MDPI";
    public static final String HDPI = "HDPI";
    public static final String XHDPI = "XHDPI";
    public static final String XXHDPI = "XXHDPI";
    public static final String XXXHDPI = "XXXHDPI";

    // 기기 모델 확인
    public static String getModel() {
        return Build.MODEL;
    }

    // 기기 OS version 확인
    public static String getOsVersion() {
        return Build.VERSION.RELEASE;
    }

    public static String getDeviceUniqueId(Context context) {
        if(context != null) {
            String baseId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID) + Build.SERIAL;
            try {
                return UUID.nameUUIDFromBytes(baseId.getBytes("utf8")).toString();
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }

    // 화면 너비 반환 (pixel)
    public static int getScreenWidth(Context context) {
        if(context == null){
            return 0;
        }
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        if(wm == null){
            return 0;
        }
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    // 화면 높이 반환 (pixel)
    public static int getScreenHeight(Context context) {
        if(context == null){
            return 0;
        }
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();

        if(wm == null){
            return 0;
        }
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    // 화면 밀도 반환
    public static int getScreenDensityDpi(Context context) {
        if(context == null){
            return 0;
        }
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();

        if(wm == null){
            return 0;
        }
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.densityDpi;
    }

    public static String getScreenDensity(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float dpi = metrics.densityDpi;

        if(dpi == 120){
            return LDPI;
        }else if(dpi > 120 && dpi <= 160){
            return MDPI;
        }else if(dpi > 120 &&dpi <= 240){
            return HDPI;
        }else if(dpi > 240 &&dpi <= 320){
            return XHDPI;
        }else if(dpi > 320 &&dpi <= 480){
            return XXHDPI;
        }else if(dpi > 480 &&dpi <= 640){
            return XXXHDPI;
        }else{
            return "";
        }
    }

    //스테이터스 바 제외한 높이 반환
    public static int getScreenHeightWithoutStatusBar(Context context) {
        if(context == null){
            return 0;
        }
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();

        if(wm == null){
            return 0;
        }
        wm.getDefaultDisplay().getMetrics(dm);

        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result =  dm.heightPixels - context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    // 화면이 켜져있는지 확인
    public static boolean isScreenOn(Context context) {
        if(context == null){
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            DisplayManager dm = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
            if(dm == null){
                return false;
            }
            boolean isScreenOn = false;
            for (Display _item : dm.getDisplays()) {
                if (_item.getState() != Display.STATE_OFF) {
                    isScreenOn = true;
                }
            }
            return isScreenOn;
        } else {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            if(pm == null){
                return false;
            }
            //noinspection deprecation
            return pm.isScreenOn();
        }
    }

    @SuppressLint("MissingPermission")
    public static String getPhoneNumber(Context context){
        String phoneNumber = "";
        TelephonyManager mgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            String tmpPhoneNumber = mgr.getLine1Number();
            phoneNumber = tmpPhoneNumber.replace("+82", "0");
        } catch (Exception e) {
            phoneNumber = "";
        }
        return phoneNumber;
    }
}