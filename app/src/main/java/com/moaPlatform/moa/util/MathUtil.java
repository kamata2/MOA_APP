package com.moaPlatform.moa.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;

public class MathUtil {

    /**
     * 숫자에 천단위마다 콤마 넣기
     *
     * @param number
     * @return
     */
    public static String toNumFormat(BigInteger number) {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(number);
    }

    public static String toNumFormat(Double number) {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(number);
    }

    /**
     * 코인 표시 단위
     * @param money
     * @param integerLength 정수영역 최대 길이 : 0이 들어오면 제한 없음
     * @param decimalLength 소수점 영역 최대 길이 : 0이 들어오면 제한 없음
     *                      소수점 자릿수 맨끝의 0은 소거 하도록한다.
     * @return
     */
    public static String moaCoinFormat(String money, int integerLength, int decimalLength) {

        try {
            if (money.contains(".")) {

                String temp[] = money.split("\\.");

                String tempHeader = temp[0];
                String tempFooter = temp[1];

                if (tempHeader.length() > integerLength && integerLength != 0) {
                    tempHeader = tempHeader.substring(0, integerLength - 1);
                }

                if (tempFooter.length() > decimalLength && decimalLength != 0) {
                    tempFooter = tempFooter.substring(0, decimalLength - 1);
                }

                String header = toNumFormat(new BigInteger(tempHeader));

                StringBuilder builder = new StringBuilder();
                builder.append(header);
                builder.append(".");
                builder.append(tempFooter);

                BigDecimal result = new BigDecimal(builder.toString()).stripTrailingZeros();        //소수점 자리수에서 오른쪽의 0 부분을 제거한 값을 반환

                return result.toString();
            } else {
                return String.format(toNumFormat(new BigInteger(money)));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "0";
        }
    }

}
