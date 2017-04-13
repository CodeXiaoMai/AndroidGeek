
package com.xiaomai.geek.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by XiaoMai on 2016/12/13 10:55.
 */
public class TimeUtils {
    public static String getTime() {
        return getDate(new Date(), "HH:mm");
    }

    /**
     * 根据毫秒时间戳返回时间
     *
     * @param millionSecond
     * @return
     */
    public static String getTime(long millionSecond) {
        return getDate(new Date(millionSecond), "HH:mm");
    }

    public static String getDate() {
        return getDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    public static String getDate(Date date) {
        return getDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 按指定的格式返回日期
     *
     * @param date
     * @param format
     * @return
     */
    public static String getDate(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    /**
     * 获取days天的日期，并按"yyyy-MM-dd HH:mm:ss"格式返回
     *
     * @param date
     * @param days
     * @return
     */
    public static String getDateBefore(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return getDate(calendar.getTime());
    }

    /**
     * 获取days天的日期，并按"yyyy-MM-dd HH:mm:ss"格式返回
     *
     * @param days
     * @return
     */
    public static String getDateBefore(int days) {
        return getDateBefore(new Date(), days);
    }

    /**
     * 获取months个月之前的日期，并按"yyyy-MM-dd HH:mm:ss"格式返回
     *
     * @param date
     * @param months
     * @return
     */
    public static String getMonthBefore(Date date, int months) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, months);
        return getDate(calendar.getTime());
    }

    /**
     * 获取months个月之前的日期，并按"yyyy-MM-dd HH:mm:ss"格式返回
     *
     * @param months
     * @return
     */
    public static String getMonthBefore(int months) {
        return getMonthBefore(new Date(), months);
    }

    /**
     * 获取years年之前的日期，并按"yyyy-MM-dd HH:mm:ss"格式返回
     *
     * @param date
     * @param years
     * @return
     */
    public static String getYearBefore(Date date, int years) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, years);
        return getDate(calendar.getTime());
    }

    /**
     * 获取years年之前的日期，并按"yyyy-MM-dd HH:mm:ss"格式返回
     *
     * @param years
     * @return
     */
    public static String getYearBefore(int years) {
        return getYearBefore(new Date(), years);
    }

    /**
     * 获取短时间：eg：刚刚、n分钟前，n小时前，n天前。。。
     *
     * @param date
     * @return
     */
    public static String getShortTime(Date date) {
        return getShortTime(getDate(date));
    }

    /**
     * 获取短时间：eg：刚刚、n分钟前，n小时前，n天前。。。
     *
     * @param date
     * @return
     */
    public static String getShortTime(String date) {
        Date descDate = new Date();
        return getShortTime(date, getDate(descDate));
    }

    /**
     * 获取短时间：eg：刚刚、n分钟前，n小时前，n天前。。。
     *
     * @param srcDateStr
     * @param descDateStr
     * @return
     */
    public static String getShortTime(String srcDateStr, String descDateStr) {
        // 时间差单位分钟
        long timeDiff = -1;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            Date srcDate = dateFormat.parse(srcDateStr);
            Date descDate = dateFormat.parse(descDateStr);
            timeDiff = (descDate.getTime() - srcDate.getTime()) / (1000 * 60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (timeDiff < 0) {
            return srcDateStr;
        } else if (timeDiff == 0) {
            return "刚刚";
        } else if (timeDiff <= 59) {
            return timeDiff + "分钟前";
        } else if (timeDiff < (60 * 24)) {
            return timeDiff / 60 + "小时前";
        } else if (timeDiff < (60 * 24 * 2)) {
            return "昨天";
        } else if (timeDiff < (60 * 24 * 3)) {
            return "前天";
        } else if (timeDiff < (60 * 24 * 30)) {
            return timeDiff / 60 / 24 + "天前";
        } else {
            return srcDateStr;
        }
    }

    /**
     * 将时间戳装换为时间
     *
     * @param milliSecond
     * @return
     */
    public static String parseTimeStamp(String milliSecond) {
        return parseTimeStamp(ParseUtils.parseToLong(milliSecond));
    }

    /**
     * 将时间戳装换为时间
     *
     * @param milliSecond
     * @return
     */
    public static String parseTimeStamp(long milliSecond) {
        if (milliSecond <= 0) {
            return "milliSecond不能小于0";
        }
        Date date = new Date(milliSecond * 1000);
        return getDate(date);
    }

}
