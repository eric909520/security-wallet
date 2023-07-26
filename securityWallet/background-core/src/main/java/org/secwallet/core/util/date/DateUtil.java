package org.secwallet.core.util.date;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

/**
 * date tool
 */
@Slf4j
public class DateUtil {

    /**
     * yyyy-MM-dd HH:mm:ss date format
     */
    public static String DATE_FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
    /**
     * yyyy-MM-dd date format
     */
    public static String DATE_FORMAT_DATE = "yyyy-MM-dd";
    /**
     * yyyy-MM-dd HH:mm date format
     */
    public static String DATE_FORMAT_DATETIME_NOSECOND = "yyyy-MM-dd HH:mm";
    /**
     * HH:mm:ss date format
     */
    public static String DATE_FORMAT_TIME = "HH:mm:ss";
    /**
     * HH:mm date format
     */
    public static String DATE_FORMAT_TIME_NOSECOND = "HH:mm";
    /**
     * yyyy date format
     */
    public static String DATE_FORMAT_DATE_YEAR= "yyyy";
    /**
     * yyyy-MM-dd HH:mm:ss.SSS date format
     */
    public static String DATE_FORMAT_TIMESTAMP = "yyyy-MM-dd HH:mm:ss.SSS";
    /**
     * yyyyMMddHHmmssSSS date format
     */
    public static String DATE_FORMAT_TIMESTAMP_ID = "yyyyMMddHHmmssSSS";
    /**
     * yyyy-MM-dd date format
     */
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat(
            DATE_FORMAT_DATE);
    /**
     * yyyy-MM-dd HH:mm:ss date format
     */
    public static final DateFormat DATETIME_FORMAT = new SimpleDateFormat(
            DATE_FORMAT_DATETIME);
    /**
     * yyyy-MM-dd HH:mm date format
     */
    public static final DateFormat DATETIME_NOSECOND_FORMAT = new SimpleDateFormat(
            DATE_FORMAT_DATETIME_NOSECOND);
    /**
     * HH:mm:ss date format
     */
    public static final DateFormat TIME_FORMAT = new SimpleDateFormat(
            DATE_FORMAT_TIME);
    /**
     * HH:mm date format
     */
    public static final DateFormat TIME_NOSECOND_FORMAT = new SimpleDateFormat(
            DATE_FORMAT_TIME_NOSECOND);
    /**
     * yyyy-MM-dd HH:mm:ss.SSS date format
     */
    public static final DateFormat TIMESTAMP_FORMAT = new SimpleDateFormat(
            DATE_FORMAT_TIMESTAMP);

    /**
     * Returns the start time of the date
     *
     * @param startDate
     * @return
     */
    public static Date toStartDate(Date startDate) {
        if (startDate != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(startDate);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            startDate = c.getTime();
        }
        return startDate;
    }

    /**
     * Returns the end time of the date
     *
     * @param endDate
     * @return
     */
    public static Date toEndDate(Date endDate) {
        if (endDate != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(endDate);
            c.set(Calendar.HOUR_OF_DAY, 23);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.SECOND, 59);
            c.set(Calendar.MILLISECOND, 999);
            endDate = c.getTime();
        }
        return endDate;
    }

    /**
     * Get the first day of the month in which the date falls
     *
     * @param date
     * @return
     */
    public static Date firstDayInMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return toStartDate(c.getTime());
    }

    /**
     * Get the last day of the month in which the date falls
     *
     * @param date
     * @return
     */
    public static Date lastDayInMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return toEndDate(c.getTime());
    }

    /**
     * day increments
     *
     * @param date
     * @param days
     * @return
     */
    public static Date addDays(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);
        return c.getTime();
    }
    /**
     * Determine whether to convert to date or datetime or time according to whether the date string contains time or not
     *
     * @param dateString
     * @return
     * @throws ParseException
     */
    public static Date parse(String dateString) throws ParseException {
        if (dateString.trim().indexOf(" ") > 0
                && dateString.trim().indexOf(".") > 0) {
            return new java.sql.Timestamp(TIMESTAMP_FORMAT.parse(dateString)
                    .getTime());
        } else if (dateString.trim().indexOf(" ") > 0) {
            // If there are two:, then there are minutes and seconds, and a colon only has hours and minutes
            if (dateString.trim().indexOf(":") != dateString.trim()
                    .lastIndexOf(":")) {
                return new java.sql.Timestamp(DATETIME_FORMAT.parse(dateString)
                        .getTime());
            } else {
                return new java.sql.Timestamp(DATETIME_NOSECOND_FORMAT.parse(
                        dateString).getTime());
            }
        } else if (dateString.indexOf(":") > 0) {
            // If there are two:, then there are minutes and seconds, and a colon only has hours and minutes
            if (dateString.trim().indexOf(":") != dateString.trim()
                    .lastIndexOf(":")) {
                return new java.sql.Time(TIME_FORMAT.parse(dateString)
                        .getTime());
            } else {
                return new java.sql.Time(TIME_NOSECOND_FORMAT.parse(dateString)
                        .getTime());
            }
        }
        return new java.sql.Date(DATE_FORMAT.parse(dateString).getTime());
    }
    /**
     * Output string to date in specified format
     *
     * @param dateString
     * @param style
     * @return
     * @throws ParseException
     */
    public static Date parse(String dateString, String style)
            throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(style);
        return dateFormat.parse(dateString);
    }

    /**
     * formatted output date to string
     *
     * @param date
     * @param style
     * @return
     */
    public static String format(Date date, String style) {
        DateFormat dateFormat = new SimpleDateFormat(style);
        return dateFormat.format(date);
    }

    /**
     * Convert the format to yyyy-MM-dd HH:mm:ss; yyyy-MM-dd character type and convert it to date type
     * @param dateString
     * @return
     */
    public static Date parseDate(String dateString) {
        Date date = null;
        try {
            date = DateUtils.parseDate(dateString, new String[]{DATE_FORMAT_DATETIME,DATE_FORMAT_DATE});
        } catch (Exception ex) {
            log.error("Pase the Date(" + dateString + ") occur errors:"
                    + ex.getMessage());
        }
        return date;
    }
    /**
     * get the next day。
     *
     * @param date
     * @param days
     * @return
     */
    public static Date getNextDays(Date date, int days) {
        LocalDateTime localDate1 = dateToDateTime(date);
        return dateTimeToDate(localDate1.plusDays(days));
    }
    public  static LocalDateTime dateToDateTime(Date date) {
        Instant instant = date.toInstant();
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
    public static Date dateTimeToDate(LocalDateTime localDateTime) {
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        return date;
    }

    /**
     * Timestamp conversion time
     * @param timeStamp
     * @return String date
     */
    public static String getSSDateByTimeStamp(Long timeStamp) {
        return DATETIME_FORMAT.format(new Date(timeStamp));
    }
}