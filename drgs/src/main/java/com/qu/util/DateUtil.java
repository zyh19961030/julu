package com.qu.util;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期时间计算
 *
 * @author yanrj 2020-12-29
 */
@Slf4j
public class DateUtil {

    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    /**
     * String转换为Date
     *
     * @param dateStr
     * @param formatStr
     * @return
     */
    public static Date string2Date(String dateStr, String formatStr) {
        Date date = null;
        try {
            if (dateStr == null) {
                return null;
            }
            if (formatStr == null) {
                return null;
            }
            SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
            date = sdf.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * Date转换为String
     *
     * @param date
     * @param formatStr
     * @return
     */
    public static String date2String(Date date, String formatStr) {
        if (date == null) {
            return "";
        }
        if (formatStr == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        return sdf.format(date);
    }

    /**
     * 得到展会状态 1:未开始 2:进行中 3:已结束
     *
     * @param nowTime
     * @param startTime
     * @param endTime
     * @return
     */
    public static int dateComparable(Date nowTime, Date startTime, Date endTime) {
        int status = 1;
        if (nowTime.compareTo(startTime) < 0) {
            status = 1;
        }
        if (nowTime.compareTo(startTime) >= 0 && nowTime.compareTo(endTime) <= 0) {
            status = 2;
        }
        if (nowTime.compareTo(endTime) > 0) {
            status = 3;
        }
        return status;
    }


}
