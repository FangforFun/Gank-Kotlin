package com.gkzxhn.gank_kotlin.utils;

import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 日期时间工具类
 * Created by 方 on 2017/8/2.
 */
public class DateUtil {
    public static final String YMD_PATTERN = "yyyy-MM-dd";
    public static final String YMD_PATTERN2 = "yyyy/MM/dd";
    public static final String YMD_PATTERN3 = "yyyy/MM/dd HH:mm";
    public static final String YMD_HMS_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String YMD_HMS_PATTERN2 = "yyyy-MM-dd HH:mm";
    public static final String[] weeks = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
    private static final String TAG = DateUtil.class.getSimpleName();
    private static final SimpleDateFormat mDataFormat = new SimpleDateFormat(YMD_PATTERN);
    private static final SimpleDateFormat mTimeFormat = new SimpleDateFormat(YMD_HMS_PATTERN);


    //    private long mToday;
//
//    {
//        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd");
//        Date date = null;
//        try {
//            date = format.parse(format.format(new Date()));
//            mToday = date.getTime();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }

    private static final int ONEDAY = 24 * 60 * 60 * 1000;

    /**
     * 通过先将 当前时间 格式化成 年月日形式 ，然后又将所得到的字符串 解析成 Date类型 即 当天0点
     *
     * @return 当天 0 点 时的毫秒值
     * @throws ParseException
     */
    public static long getDateToTime(Date date) throws ParseException {
        return mDataFormat.parse(mDataFormat.format(date)).getTime();
    }

    /**
     * 通过毫秒值  获取 当天 0 点的毫秒值
     *
     * @param millis
     * @return 当天 0 点时的毫秒值
     * @throws ParseException
     */
    public static long getMillisToTime(long millis) throws ParseException {
        return getDateToTime(new Date(millis));
    }

    /**
     * 通过 秒值 获取当天 0 点的毫秒值
     *
     * @param seconds
     * @return 当天 0 点时的毫秒值
     * @throws ParseException
     */
    public static long getSecondsToTime(int seconds) throws ParseException {
        return getMillisToTime(seconds * 1000L);
    }

    /**
     * 根据毫秒值 与 传入的日期  显示与之相近的日期格式的显示
     *
     * @param millis
     * @param date
     * @return 具体 (日期自己传入)对应日期格式显示的字符串
     * @throws ParseException
     */
    public static String getMillisToDate(long millis, Date date) throws ParseException {

        Log.d("tag", mDataFormat.format(new Date(millis)));
        if (isToday(millis, date)) {
            return millisecondToDate(millis, "H:dd");
        } else if (isYesterday(millis, date)) {
            return "昨天" + millisecondToDate(millis, "H:dd");
        } else if ((millis + 3 * ONEDAY) >= getDateToTime(date)) {
            return millisecondToDate(millis, "E H:dd");
        } else {
            return millisecondToDate(millis, "yyyy年M月d日 H:dd");
        }
    }

    /**
     * 是否是今天
     * @param millis
     * @param date
     * @return
     * @throws ParseException
     */
    public static boolean isToday(long millis, Date date) throws ParseException {
        return millis > getDateToTime(date);
    }

    public static boolean isToday(long millis) throws ParseException {
        return isToday(millis, new Date(System.currentTimeMillis()));
    }

    /**
     * 是否是昨天
     * @param millis
     * @param date
     * @return
     * @throws ParseException
     */
    public static boolean isYesterday(long millis, Date date) throws ParseException {
        return (millis + 1 * ONEDAY) >= getDateToTime(date);
    }

    public static boolean isYesterday(long mills) throws ParseException {
        return isYesterday(mills, new Date(System.currentTimeMillis()));
    }

    /**
     * 是否是今天或者昨天
     * @param mills
     * @return
     * @throws ParseException
     */
    public static boolean isToOrYes(long mills) {
        boolean b = false;
        try {
             b = isToday(mills) || isYesterday(mills);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return b;
    }

    /**
     * 根据  秒值 与 传入的日期  显示与之相近的日期格式的显示
     *
     * @param seconds
     * @param date
     * @return 具体 (日期自己传入) 对应日期格式显示的字符串
     * @throws ParseException
     */
    public static String getSecondsToDate(int seconds, Date date) throws ParseException {
        return getMillisToDate(seconds * 1000L, date);
    }

    /**
     * 根据毫秒值  显示与之   当前日期   相近的日期格式的显示
     *
     * @param millis
     * @return 具体（当前日期） 对应日期格式显示的字符串
     * @throws ParseException
     */
    public static String getMillisToDate(long millis) throws ParseException {
        Long time = System.currentTimeMillis();
        return getMillisToDate(millis, new Date(time));
    }

    /**
     * 根据秒值  显示与之   当前日期   相近的日期格式的显示
     *
     * @param seconds
     * @return 具体（当前日期） 对应日期格式显示的字符串
     * @throws ParseException
     */
    public static String getSecondsToDate(int seconds) throws ParseException {
        return getMillisToDate(seconds * 1000L);
    }

    public static long getStringToMills(String dateStr, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = format.parse(dateStr);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 计算两个日期相差的天数，是否取绝对值
     *
     * @param date1 第一个日期
     * @param date2 第二个日期
     * @param isAbs 是否去绝对值
     * @return 相差天数
     */
    public static int getDaysUnAbs(String date1, String date2, boolean isAbs) {
        int day = 0;
        if (TextUtils.isEmpty(date1) || TextUtils.isEmpty(date2))
            return 0;
        try {
            Date date = mDataFormat.parse(date1);
            Date myDate = mDataFormat.parse(date2);
            day = (int) ((date.getTime() - myDate.getTime()) / (24 * 60 * 60 * 1000));
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        return isAbs ? Math.abs(day) : day;
    }

    /**
     * 计算两个日期相差的天数，是否取绝对值
     *
     * @param date1 第一个日期
     * @param date2 第二个日期
     * @return 相差天数
     */
    public static int getDaysUnAbs(String date1, String date2) {
        return getDaysUnAbs(date1, date2, true);
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     *
     * @param date 日期
     * @return 长时间格式日期
     */
    public static Date strToDateLong(String date) {
        ParsePosition pos = new ParsePosition(0);
        return mTimeFormat.parse(date, pos);
    }

    /**
     * 获取当前的日期时间
     *
     * @return 当前的日期时间
     */
    public static String getCurrentTime() {
        return mTimeFormat.format(new Date());
    }

    /**
     * 获取当前的日期
     *
     * @return 当前的日期
     */
    public static String getCurrentDate() {
        return mDataFormat.format(new Date());
    }

    /**
     * 获取输入日期是星期几
     *
     * @param date 日期
     * @return 星期几
     */
    public static String getWeekDate(Date date) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTime(date);
        return weeks[calendar.get(calendar.DAY_OF_WEEK) - 1];
    }

    /**
     * 获取输入日期是星期几
     *
     * @param date 日期
     * @return 星期几
     */
    public static String getWeekDate(String date) {
        try {
            Date d = mDataFormat.parse(date);
            return getWeekDate(d);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        return "星期数未知";
    }

    /**
     * 毫秒转日期
     *
     * @param millisecond 毫秒数
     * @param pattern     正则式
     * @return 日期
     */
    public static String millisecondToDate(long millisecond, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date date = new Date(millisecond);
        return format.format(date);
    }

    /**
     * 秒转日期
     *
     * @param second  秒数
     * @param pattern 正则式
     * @return 日期
     */
    public static String secondToDate(int second, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date date = new Date(second * 1000L);
        return format.format(date);
    }

    /**
     * 时间计算
     *
     * @param millisecond 毫秒数
     * @return 计算后显示的文字
     */
    public static String timeCalculate(long millisecond) {
        String result = "";
        long diff = System.currentTimeMillis() - millisecond;//时间差
        if (diff < 1000 * 60/* && diff > 0*/)
            return "刚刚";
        else if (diff >= 1000 * 60 && diff < 1000 * 60 * 60)
            return diff / (1000 * 60) + "分钟以前";
        else if (diff >= 1000 * 60 * 60 && diff < 1000 * 60 * 60 * 24)
            return diff / (1000 * 60 * 60) + "小时以前";
        else if (diff >= 1000 * 60 * 60 * 24)
            return diff / (1000 * 60 * 60 * 24) + "天以前";
//        else if (diff < 0)
//            return "输入时间在当前时间之后,不可以计算";
        return result;
    }

    /**
     * 时间计算
     *
     * @param second 秒数
     * @return 计算后显示的文字
     */
    public static String timeCalculate(int second) {
        return timeCalculate(second * 1000L);
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Date date = mTimeFormat.parse("2015-12-8 18:08:00");
        System.out.print(timeCalculate((int) (date.getTime() / 1000)));
    }

    /**
     * 把long时间转换成时间格式字符串
     *
     * @param time 时间
     * @return
     */
    public static String generateTime(long time) {
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * 获取年
     *
     * @param mills
     * @return
     */
    public static int getYearInMills(long mills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mills);
        return calendar.get(Calendar.YEAR);
    }


    /**
     * 获取月份
     *
     * @param mills
     * @return
     */
    public static int getMonthInMills(long mills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mills);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取天数
     *
     * @param mills
     * @return
     */
    public static int getDayInMills(long mills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mills);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取月份字符串
     * @param mills
     * @return
     */
    public static String getMonthString(long mills) {
        return getMonthInMills(mills) + "月";
    }

    /**
     * 获取天数字符串
     * @param mills
     * @return
     */
    public static String getDayString(long mills) {
        int dayInMills = getDayInMills(mills);
        return dayInMills / 10 == 0 ? "0" + dayInMills : String.valueOf(dayInMills);
    }

    public static String getToOrYesString(long mills) {
        String day = "";
        try {
            if (isToday(mills)) {
                day =  "今天";
            }else if(isYesterday(mills)) {
                    day = "昨天";
                }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day;
    }
}