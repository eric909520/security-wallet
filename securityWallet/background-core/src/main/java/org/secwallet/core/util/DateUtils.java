package org.secwallet.core.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * date tool
 */
public class DateUtils {

    public static SimpleDateFormat YYYY_MM_DD_HH_MM_SS_SSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public static SimpleDateFormat YYYY_MM_DD_HH_MM_SS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");

    public static SimpleDateFormat YYYYMMDD = new SimpleDateFormat("yyyyMMdd");

    public static SimpleDateFormat YYYY_MM_DD_00 = new SimpleDateFormat("yyyy-MM-dd 00:00:00.000");

    public static SimpleDateFormat YYYY_MM_DD_HH = new SimpleDateFormat("yyyy-MM-dd HH");

    public static SimpleDateFormat YYYYMMDDHHMMSS = new SimpleDateFormat("yyyyMMddHHmmss");


    public static String getCurrentTime() {
        return YYYY_MM_DD_HH_MM_SS_SSS.format(new Date());
    }

    public static String getCurrentTimeNumber() {
        return YYYYMMDDHHMMSS.format(new Date());
    }

    public static Date getDayBegin() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getDayEnd() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    public static Date getBeginDayOfYesterday() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getDayBegin());
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    public static Date getBeginDayOfYesterdays() {
        Date date = new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, -2);//把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime(); //这个时间就是日期往后推一天的结果
        return date;
    }

    public static Date getEndDayOfYesterDay() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getDayEnd());
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    public static Date getBeginDayOfTomorrow() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getDayBegin());
        cal.add(Calendar.DAY_OF_MONTH, 1);

        return cal.getTime();
    }

    public static Date getEndDayOfTomorrow() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(getDayEnd());
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    @SuppressWarnings("unused")
    public static Date getBeginDayOfWeek() {
        Date date = new Date();
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            dayOfWeek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayOfWeek);
        return getDayStartTime(cal.getTime());
    }

    public static Date getEndDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginDayOfWeek());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        return getDayEndTime(weekEndSta);
    }

    @SuppressWarnings("unused")
    public static Date getBeginDayOfLastWeek() {
        Date date = new Date();
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek - 7);
        return getDayStartTime(cal.getTime());
    }

    public static Date getEndDayOfLastWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginDayOfLastWeek());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        return getDayEndTime(weekEndSta);
    }

    public static Date getBeginDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        return getDayStartTime(calendar.getTime());
    }

    public static Date getEndDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        int day = calendar.getActualMaximum(5);
        calendar.set(getNowYear(), getNowMonth() - 1, day);
        return getDayEndTime(calendar.getTime());
    }

    public static Date getBeginDayOfLastMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 2, 1);
        return getDayStartTime(calendar.getTime());
    }

    public static Date getEndDayOfLastMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 2, 1);
        int day = calendar.getActualMaximum(5);
        calendar.set(getNowYear(), getNowMonth() - 2, day);
        return getDayEndTime(calendar.getTime());
    }

    public static Date getBeginDayOfYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, getNowYear());
        // cal.set
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);

        return getDayStartTime(cal.getTime());
    }

    public static Date getEndDayOfYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, getNowYear());
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DATE, 31);
        return getDayEndTime(cal.getTime());
    }

    //Get the start time of a date
    public static Timestamp getDayStartTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) calendar.setTime(d);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }

    //Get the end time of a date
    public static Timestamp getDayEndTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) calendar.setTime(d);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return new Timestamp(calendar.getTimeInMillis());
    }

    //Get what year it is
    public static Integer getNowYear() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return Integer.valueOf(gc.get(1));
    }

    //Get the month of the month
    public static int getNowMonth() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return gc.get(2) + 1;
    }

    //The number of days obtained by subtracting two dates
    public static int getDiffDays(Date beginDate, Date endDate) {

        if (beginDate == null || endDate == null) {
            throw new IllegalArgumentException("getDiffDays param is null!");
        }

        long diff = (endDate.getTime() - beginDate.getTime())
                / (1000 * 60 * 60 * 24);

        int days = new Long(diff).intValue();

        return days;
    }

    //The number of milliseconds obtained by subtracting two dates
    public static long dateDiff(Date beginDate, Date endDate) {
        long date1ms = beginDate.getTime();
        long date2ms = endDate.getTime();
        return date2ms - date1ms;
    }

    //Get the largest of two dates
    public static Date max(Date beginDate, Date endDate) {
        if (beginDate == null) {
            return endDate;
        }
        if (endDate == null) {
            return beginDate;
        }
        if (beginDate.after(endDate)) {
            return beginDate;
        }
        return endDate;
    }

    //Get the smallest of two dates
    public static Date min(Date beginDate, Date endDate) {
        if (beginDate == null) {
            return endDate;
        }
        if (endDate == null) {
            return beginDate;
        }
        if (beginDate.after(endDate)) {
            return endDate;
        }
        return beginDate;
    }

    /**
     * Timestamp conversion time
     * @param timeStamp
     * @return String date
     */
    public static String getDateByTimeStamp(Long timeStamp) {
        return YYYY_MM_DD_HH_MM_SS_SSS.format(new Date(timeStamp));
    }

    /**
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
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * @param dateStr
     * @return
     * @throws Exception
     */
    public static long getTimeStamp(String dateStr) throws Exception {
        Date date = YYYY_MM_DD_HH_MM_SS_SSS.parse(dateStr + ".000");
        return date.getTime();
    }

    /**
     * @param dateStr
     * @return
     * @throws Exception
     */
    public static long getTimeStamp1(String dateStr) throws Exception {
        Date date = YYYY_MM_DD_HH_MM_SS.parse(dateStr);
        return date.getTime();
    }

    /**
     * @param dateStr
     * @return
     * @throws Exception
     */
    public static long getTimeStampByYYYYMMDD(String dateStr) throws Exception {
        Date date = YYYY_MM_DD_HH_MM_SS_SSS.parse(dateStr + " 00:00:00.000");
        return date.getTime();
    }

    /**
     * @param dateStr
     * @return
     */
    public static int getHours(String dateStr) throws Exception {
        Date date = YYYY_MM_DD_HH.parse(dateStr);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * @param millis
     * @return
     */
    public static String getCurrTimeAddMilli(int millis) {
        return getDateByTimeStamp(System.currentTimeMillis() + millis);
    }

    /**
     * @return
     * @throws Exception
     */
    public static Long getYestadyStartTime() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date date = calendar.getTime();
        String time = YYYY_MM_DD_00.format(date);
        return getTimeStamp(time);
    }

    /**
     * @return
     * @throws Exception
     */
    public static String getYestadyStartTimeYYYYMMDD() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date date = calendar.getTime();
        String time = YYYY_MM_DD.format(date);
        return time;
    }

    /**
     * @return
     * @throws Exception
     */
    public static Long getTodayStartTime() throws Exception {
        String time = YYYY_MM_DD_00.format(new Date());
        return getTimeStamp(time);
    }

    /**
     * @return
     * @throws Exception
     */
    public static String getStartTimeStrYYYYMMDD(int day){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -day);
        Date date = calendar.getTime();
        String time = YYYYMMDD.format(date);
        return time;
    }

    /**
     * @param stamp
     * @return
     */
    public static String stampToDate(long stamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(stamp);
        return simpleDateFormat.format(date);
    }

    /**
     */
    public static Long getBeforeHourStartTime(int hour) throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - hour);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:00:00.000");
        String time = format.format(calendar.getTime());
        return getTimeStamp(time);
    }

    /**
     */
    public static Long getCurrentHourStartTime() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:00:00.000");
        String time = format.format(new Date());
        return getTimeStamp(time);
    }
    /**
     * @return String date
     */
    public static String getCurrentDate() {
        return YYYY_MM_DD.format(new Date());
    }
    /**
     * @return String date
     */
    public static String getCurrentDate(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH,day);
        return YYYY_MM_DD.format(calendar.getTime());
    }

    public static long getCurrentTimeStamp() {
        String format = YYYY_MM_DD_HH_MM_SS_SSS.format(new Date());
        Date date = null;
        try {
            date = YYYY_MM_DD_HH_MM_SS_SSS.parse(format);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * @return
     */
    public static int getTodayEndNum() {
        String date = YYYY_MM_DD.format(new Date());
        char[] dateChar = date.toCharArray();
        return Integer.parseInt(String.valueOf(dateChar[dateChar.length-1]));
    }

}