package com.moaPlatform.moa.util;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;

import com.moaPlatform.moa.BuildConfig;

import java.util.Locale;
import java.util.regex.Pattern;

import androidx.annotation.StringRes;

public class StringUtil {

    public static String getImageUrl(String imageUrl) {

        if (imageUrl != null) {
            return BuildConfig.FILE_SERVER_BASE_URL + imageUrl;
        } else {
            return "";
        }
    }

    /**
     * 특수문자 포함여부 체크
     */
    public static boolean isContainSpecialCharacter(String text) {
        if (text != null && text.matches("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝| ]*")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 이메일 유효성 체크
     */
    public static boolean isEmail(String email) {
        Pattern pattern = android.util.Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    /**
     * 핸드폰번호 유효성 체크
     */
    public static boolean isCellPhoneNumber(String phoneNumber) {

        if (phoneNumber.contains("-")) {
            return Pattern.matches("^01(?:0|1|[6-9]) - (?:\\d{3}|\\d{4}) - \\d{4}$", phoneNumber);
        } else {
            return Pattern.matches("^01(?:0|1|[6-9])  (?:\\d{3}|\\d{4})  \\d{4}$", phoneNumber);
        }
    }

    /**
     * 전화번호 유효성 체크
     */
    public static boolean isPhoneNumber(String phoneNumber) {
        if (phoneNumber.contains("-")) {
            return Pattern.matches("^\\d{2,3}-\\d{3,4}-\\d{4}$", phoneNumber);
        } else {
            return Pattern.matches("^\\d{2,3}\\d{3,4}\\d{4}$", phoneNumber);
        }
    }

    /**
     * @param text 변환할 text
     * @return String  문자를 html 형식으로 반환
     */
    public static Spanned convertHtmlFormat(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(text);
        }
    }

    /**
     * @param convertPrice 단위 표시 해줄 가격
     * @return 가격을 단위를 표시해서 반환
     */
    public static String convertCommaPrice(int convertPrice) {
        return String.format(Locale.getDefault(), "%,d", convertPrice);
    }

    /**
     * @param convertPrice 단위 표시 해줄 가격
     * @return 가격을 단위를 표시해서 반환
     */
    public static String convertCommaPrice(String convertPrice) {
        return String.format(Locale.getDefault(), "%,d", Integer.valueOf(convertPrice));
    }

    /**
     * @param context      getString 을 하기 위한 context
     * @param resId        string.xml 에 정의 된 id
     * @param convertPrice 단위를 표시해줄 가격
     * @return 가격 단위를 표시해서 반환
     */
    public static String convertCommaPrice(Context context, @StringRes int resId, int convertPrice) {
        return context.getString(resId, convertCommaPrice(convertPrice));
    }

    public static SpannableStringBuilder convertFontColor(String str, int color) {
        SpannableStringBuilder sp = new SpannableStringBuilder(str);
        sp.setSpan(new ForegroundColorSpan(color), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
    }

    public static SpannableStringBuilder convertFontColorBold(String str, int color) {
        SpannableStringBuilder sp = new SpannableStringBuilder(str);
        sp.setSpan(new ForegroundColorSpan(color), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, str.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return sp;
    }

    public static SpannableStringBuilder convertFontPixelSize(String str, int size) {
        SpannableStringBuilder sp = new SpannableStringBuilder(str);
        sp.setSpan(new AbsoluteSizeSpan(size, false), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
    }

    public static SpannableStringBuilder convertFontColorSize(String str, int color, int size) {
        SpannableStringBuilder sp = new SpannableStringBuilder(str);
        sp.setSpan(new AbsoluteSizeSpan(size, false), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(color), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
    }

    public static SpannableStringBuilder convertFontColorPixelSize(String str, int color, int size) {
        SpannableStringBuilder sp = new SpannableStringBuilder(str);
        sp.setSpan(new ForegroundColorSpan(color), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new AbsoluteSizeSpan(size, false), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
    }

    public static SpannableStringBuilder convertFontColorPixelSizeBold(String str, int color, int size) {
        SpannableStringBuilder sp = new SpannableStringBuilder(str);
        sp.setSpan(new ForegroundColorSpan(color), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new AbsoluteSizeSpan(size, false), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, str.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return sp;
    }

    public static SpannableStringBuilder convertFontBold(String str) {
        SpannableStringBuilder sp = new SpannableStringBuilder(str);
        sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, str.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return sp;
    }

    public static SpannableStringBuilder convertFontSizeBold(String str, int size) {
        SpannableStringBuilder sp = new SpannableStringBuilder(str);
        sp.setSpan(new AbsoluteSizeSpan(size, false), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, str.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return sp;
    }

    public static SpannableStringBuilder convertFontUnderLine(String str) {
        SpannableStringBuilder sp = new SpannableStringBuilder(str);
        sp.setSpan(new UnderlineSpan(), 0, str.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return sp;
    }

    public static SpannableStringBuilder convertFontStrike(String str) {
        SpannableStringBuilder sp = new SpannableStringBuilder(str);
        sp.setSpan(new StrikethroughSpan(), 0, str.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return sp;
    }

    public static SpannableStringBuilder convertFontColorStrike(String str, int color) {
        SpannableStringBuilder sp = new SpannableStringBuilder(str);
        sp.setSpan(new ForegroundColorSpan(color), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new StrikethroughSpan(), 0, str.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return sp;
    }

    public static SpannableStringBuilder convertFontColorSizeStrike(String str, int color, int size) {
        SpannableStringBuilder sp = new SpannableStringBuilder(str);
        sp.setSpan(new ForegroundColorSpan(color), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new AbsoluteSizeSpan(size, false), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new StrikethroughSpan(), 0, str.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return sp;
    }

}
