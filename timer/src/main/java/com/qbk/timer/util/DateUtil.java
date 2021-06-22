package com.qbk.timer.util;

/**
 * 日期工具类
 **/
public class DateUtil {
    private static String BASE_CRON_STR = "sec min hour day month ? ";

    /**
     * 替换cron 表达式
     */
    public static String getCron(String[]dateStr){
           return BASE_CRON_STR
                   .replace("sec",dateStr[5])
                   .replace("min",dateStr[4])
                   .replace("hour",dateStr[3])
                   .replace("day",dateStr[2])
                   .replace("month",dateStr[1]);
    }

}
