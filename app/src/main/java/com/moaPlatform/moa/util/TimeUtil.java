package com.moaPlatform.moa.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeUtil {

    public static final String DATE_FORMAT_SIMPLE = "yyyy-MM-dd";

    public static Calendar getTodayCalendar() {
        return new GregorianCalendar();
    }

    /**
     * string 으로 일자 변환
     */
    public static String getStringFormatDate(String toFormat, Calendar calendar) {
        SimpleDateFormat transToFormat = new SimpleDateFormat(toFormat);
        return transToFormat.format(calendar.getTime());
    }

    public static String getStringFormatDate(String toFormat, Calendar calendar, int day) {
        SimpleDateFormat transToFormat = new SimpleDateFormat(toFormat);
        calendar.add(Calendar.DATE, day);
        return transToFormat.format(calendar.getTime());
    }

    public static String getStringFormatDate(String toFormat, int year, int month, int day) {
        SimpleDateFormat formatter = new SimpleDateFormat(toFormat);
        Calendar calendar = new GregorianCalendar();
        calendar.set(year, month, day);
        return formatter.format(calendar.getTime());
    }

    /**
     * yyyy-MM-dd 형태의 문자열을 캘린더 객체로 변환
     */
    public static Calendar getCalendar(String toFormat, String date) {
        Calendar cal = new GregorianCalendar();
        try {
            SimpleDateFormat transToFormat = new SimpleDateFormat(toFormat);
            cal.setTime(transToFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }

    /**
     * yyyy-MM-dd 형태의 문자열을 캘린더 객체로 변환
     * [연]
     */
    public static int getCalendarYear(String toFormat, String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(toFormat);
        Calendar calendar = new GregorianCalendar();
        try {
            Date date = formatter.parse(strDate);
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar.get(Calendar.YEAR);
    }

    /**
     * yyyy-MM-dd 형태의 문자열을 캘린더 객체로 변환
     * [월]
     */
    public static int getCalendarMonth(String toFormat, String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(toFormat);
        Calendar calendar = new GregorianCalendar();
        try {
            Date date = formatter.parse(strDate);
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * yyyy-MM-dd 형태의 문자열을 캘린더 객체로 변환
     * [일]
     */
    public static int getCalendarDay(String toFormat, String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(toFormat);
        Calendar calendar = new GregorianCalendar();
        try {
            Date date = formatter.parse(strDate);
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar.get((Calendar.DATE));
    }

}
