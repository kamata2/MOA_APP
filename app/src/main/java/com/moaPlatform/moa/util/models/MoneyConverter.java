package com.moaPlatform.moa.util.models;

import android.view.View;

import java.util.Locale;

/**
 * 돈 단위 변환해주는 클래스
 */
@Deprecated
public interface MoneyConverter {
    /**
     * 숫자 천 단위 콤마
     * @param money
     * 변환할 금액
     * @return
     * 천화위가 찍힌 금액
     */
    @Deprecated
    default String commaUnitChange(int money) {
        return String.format(Locale.getDefault(),"%,d", money);
    }

    /**
     * 숫자 천 단위 콤마
     * @param money
     * 가격
     * @param stringId
     * string.xml 에 있는 string id
     * @param view
     * view
     * @return
     * 변환된 금액
     */
    @Deprecated
    default String commaUnitChange(int money, int stringId, View view) {
        return String.format(view.getResources().getString(stringId), commaUnitChange(money));
    }
}
