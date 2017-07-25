package com.quanmai.yiqu.common.util;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * cjy
 */
@SuppressLint("SimpleDateFormat")
public class DateUtil {
    public static String getTuanStart(long timestamp) {
        if ((timestamp + "").length() < 13)
            timestamp = timestamp * 1000;
        Date date = new Date(timestamp);
        SimpleDateFormat df = new SimpleDateFormat("MM月dd日 HH:mm");
        df.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return df.format(date);
    }

    public static String getTuanRest(String timestamp) {
        long l = Long.parseLong(timestamp);
        if (l / 3600 >= 24)
            return l / 86400 + "天" + l % 86400 / 3600 + "小时" + l % 86400 % 3600 / 60
                    + "分钟";
        else
            return l / 3600 + "小时" + l % 3600 / 60 + "分钟";
    }

    public static String getTryRest(String timestamp) {
        int hour, min, sec;
        String result = "";
        long l = Long.parseLong(timestamp);
        int day = (int) (l / (60 * 60 * 24));
        if (day > 0) {
            result = day + "天 ";
            hour = (int) (l % (60 * 60 * 24) / (60 * 60));
            min = (int) (l % (60 * 60 * 24) % (60 * 60) / 60);
            sec = (int) (l % (60 * 60 * 24) % (60 * 60) % 60);
        } else {
            hour = (int) (l / (60 * 60));
            min = (int) (l % (60 * 60) / 60);
            sec = (int) (l % (60 * 60) % 60);
        }
        String[] time = new String[]{hour + "", min + "", sec + ""};
        if (hour < 10) {
            time[0] = "0" + hour;
        }
        if (min < 10) {
            time[1] = "0" + min;
        }
        if (sec < 10) {
            time[2] = "0" + sec;
        }
        return result + time[0] + ":" + time[1] + ":" + time[2];
    }

    @SuppressWarnings("unused")
    public static String getTryRest2(String timestamp) {
        int hour, min, sec;
        String result = "";
        long l = Long.parseLong(timestamp);
        int day = (int) (l / (60 * 60 * 24));
        if (day > 0) {
            result = day + "天 ";
            hour = (int) (l % (60 * 60 * 24) / (60 * 60));
            min = (int) (l % (60 * 60 * 24) % (60 * 60) / 60);
            sec = (int) (l % (60 * 60 * 24) % (60 * 60) % 60);
        } else {
            hour = (int) (l / (60 * 60));
            min = (int) (l % (60 * 60) / 60);
            sec = (int) (l % (60 * 60) % 60);
        }
        String[] time = new String[]{hour + "", min + "", sec + ""};
        if (hour < 10) {
            time[0] = "0" + hour;
        }
        if (min < 10) {
            time[1] = "0" + min;
        }
        if (sec < 10) {
            time[2] = "0" + sec;
        }
        return day + time[0] + ":" + time[1] + ":" + time[2];
    }

    public static String timestampToDatetime(String timestamp) {
        Date date = new Date(Long.parseLong(timestamp) * 1000);
        SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm"); // yyyy年MM月dd日
        // HH时mm分ss秒
        df.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return df.format(date);
    }

    public static String timestampToDate(String timestamp) {
        Date date = new Date(Long.parseLong(timestamp) * 1000);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); // yyyy年MM月dd日
        // HH时mm分ss秒
        df.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return df.format(date);
    }

    /**
     * 将时间从精确到秒改为精确到分钟
     *
     * @param time
     * @return
     */
    public static String timeFormatToMinnue(String time) {
        String formatTime = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = dateFormat.parse(time);
            SimpleDateFormat sf = new SimpleDateFormat("yyyy.MM.dd  HH:mm");
            formatTime = sf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatTime;
    }

    /**
     * 将指定格式的时间字符串转化为Date
     *
     * @param strDate
     * @param strFormat
     * @return
     */
    public static Date parseToDate(String strDate, String strFormat) {
        Date date = null;
        if (TextUtils.isEmpty(strDate) || TextUtils.isEmpty(strFormat)) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(strFormat);
        try {
            date = dateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 将Date转换为指定的时间格式字符串
     *
     * @param date
     * @param strFormat
     * @return
     */
    public static String formatToString(Date date, String strFormat) {
        String strDate = "";
        if (date == null || TextUtils.isEmpty(strFormat)) {
            return strDate;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(strFormat);
        strDate = dateFormat.format(date);
        return strDate;
    }

    /**
     * 将指定的时间格式字符串转换为其他时间格式的字符串
     *
     * @param strDate       日期字符串
     * @param strOldFormat  旧日期格式
     * @param strNewFormat  新日期格式
     * @return
     */
    public static String formatToOther(String strDate, String strOldFormat, String strNewFormat) {
        if (TextUtils.isEmpty(strDate) || TextUtils.isEmpty(strOldFormat) || TextUtils.isEmpty(strNewFormat)) {
            return null;
        }

        Date date = parseToDate(strDate, strOldFormat);
        strDate = formatToString(date, strNewFormat);
        return strDate;
    }

    public static long getDatebyString(String dateTime) {
        Calendar c = Calendar.getInstance();

        try {
            c.setTime(new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").parse(dateTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return c.getTimeInMillis();
    }

    /**
     * 判断两个日期的大小
     *
     * @param dayStart 第一个日期
     * @param dayEnd   第二个日期
     */
    public static boolean DateCompare(String dayStart, String dayEnd) {
        dayStart = formatToOther(dayStart, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd");
        dayEnd = formatToOther(dayEnd, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd");
        //设定时间的模板
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //得到指定模范的时间
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = sdf.parse(dayStart);
            d2 = sdf.parse(dayEnd);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        //比较
        if ((d1.getTime() - d2.getTime()) > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断两个日期的大小（精确到日）
     *
     * @param oldDay     第一个日期
     * @param currentDay 第二个日期
     */
    public static boolean DateDayCompare(String oldDay, String currentDay) {
        if (TextUtils.isEmpty(oldDay) || TextUtils.isEmpty(currentDay)) {
            return false;
        }
        //设定时间的模板
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //得到指定模范的时间
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = sdf.parse(oldDay);
            d2 = sdf.parse(currentDay);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        //比较
        if (d1.compareTo(d2) == 0) { //是否同一天
            return true;
        } else {
            return false;
        }

    }
}
