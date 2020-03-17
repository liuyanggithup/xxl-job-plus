package com.xxl.job.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * date util
 *
 * @author xuxueli 2018-08-19 01:24:11
 */
public class DateUtil {
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected synchronized SimpleDateFormat initialValue() {
            //return super.initialValue();
            return new SimpleDateFormat(DATE_FORMAT);
        }
    };

    public static String format(Date date) {
        return threadLocal.get().format(date);
    }

    public static Date parse(String textDate) throws ParseException {
        return threadLocal.get().parse(textDate);
    }

    /**
     * 给定的时间再加上指定分钟数
     */
    public static Date getAddMinuteDate(Date dt, int minutes) {
        if (dt == null) {
            dt = new Date(System.currentTimeMillis());
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.add(Calendar.MINUTE, minutes);

        return cal.getTime();
    }

    public static Date getMinusDayDate(Date dt, int days) {
        if (dt == null) {
            dt = new Date(System.currentTimeMillis());
        }

        int hours = days * 24;
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.add(Calendar.HOUR, -hours);

        return cal.getTime();
    }

    public static void main(String[] args) {
        Date minusDayDate = DateUtil.getMinusDayDate(new Date(), 7);
        System.out.println(minusDayDate);
    }

}