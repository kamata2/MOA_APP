package com.moaPlatform.moa.util;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 숫자 소수점 앞 뒤 자리수 제한을 하는 InputFilter
 */
public class DecimalDigitsInputFilter implements InputFilter {

    Pattern mPattern;

    public DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
        mPattern = Pattern.compile("^\\d{0," + digitsBeforeZero + "}([\\.,](\\d{0," + digitsAfterZero + "})?)?$");
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        String newString =
                dest.toString().substring(0, dstart) + source.toString().substring(start, end)
                        + dest.toString().substring(dend, dest.toString().length());

        Matcher matcher = mPattern.matcher(newString);
        if (!matcher.matches()) {
            return "";
        }
        return null;
    }
}
