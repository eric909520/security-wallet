package com.backend.common.utils;

import io.sentry.Sentry;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 *
 * @author
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static SimpleDateFormat YYYYMMDD_HHMMSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static SimpleDateFormat YYYY_MM_DD_HH_MM_SS_SSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            Sentry.captureException(e);
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 获取毫秒时间戳
     *
     * @return
     * @throws Exception
     */
    public static long getTimeStamp() {
        String format = YYYY_MM_DD_HH_MM_SS_SSS.format(new Date());
        Date date = null;
        try {
            date = YYYY_MM_DD_HH_MM_SS_SSS.parse(format);
        } catch (ParseException e) {
            Sentry.captureException(e);
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 时间戳转换时间
     * @param timeStamp 时间戳
     * @return String date
     */
    public static String getSSSDateByTimeStamp(Long timeStamp) {
        return YYYY_MM_DD_HH_MM_SS_SSS.format(new Date(timeStamp));
    }

    /**
     * 时间戳转换时间
     * @param timeStamp 时间戳
     * @return String date
     */
    public static String getSSDateByTimeStamp(Long timeStamp) {
        return YYYYMMDD_HHMMSS.format(new Date(timeStamp));
    }

    /**
     * 获取时间戳
     * @param dateStr
     * @return
     * @throws Exception
     */
    public static long getSSSTimeStamp(String dateStr) throws Exception {
        Date date = YYYY_MM_DD_HH_MM_SS_SSS.parse(dateStr + ".000");
        return date.getTime();
    }

    /**
     * 获取时间戳
     * @param dateStr
     * @return
     * @throws Exception
     */
    public static long getSSTimeStamp(String dateStr) throws Exception {
        Date date = YYYYMMDD_HHMMSS.parse(dateStr);
        return date.getTime();
    }

    /**
     * 获取当前时间n天前x点y分z秒的时间戳
     * @param day,hours,minute,seconds
     * @return
     * @throws Exception
     */
    public static long getOnePointTimeStamp(int day, int hours, int minute, int seconds) throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, day);
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, seconds);
        return calendar.getTime().getTime();
    }
    public static long getOnePointTimeStamp(int day, int hours, int minute, int seconds, int millsSecond) throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, day);
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, seconds);
        calendar.set(Calendar.MILLISECOND, seconds);
        return calendar.getTime().getTime();
    }

    /**
     * 获取当前时间x小时前的时间戳
     * @param hours
     * @return
     * @throws Exception
     */
    public static long getOnePointTimeStamp(int hours) throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime().getTime();
    }

    /**
     * 获取前n个月前路径，格式如2020-08
     * @param n
     * @return
     * @throws Exception
     */
    public static String getSomeMonthAgoPath(int n) throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, n);
        Date m = calendar.getTime();
        String someMonthAgoPath = DateFormatUtils.format(m, "yyyy-MM-dd");
        return someMonthAgoPath.substring(0,someMonthAgoPath.length()-3);
    }

}
