package com.itheima.ssm.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 格式转换工具类
 * @auther itheima
 * @create 2018-08-31 16:17
 */
public class DateUtils {

    /**
     * 日期转字符串
     * @param date
     * @param patt
     * @return
     */
    public static String date2String(Date date,String patt){
        SimpleDateFormat sdf = new SimpleDateFormat();
        String format = sdf.format(date);
        return format;
    }

    /**
     * 字符串转日期
     * @param str
     * @param patt
     * @return
     * @throws ParseException
     */
    public static Date string2Date(String str,String patt) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat();
        Date date = sdf.parse(str);
        return date;
    }
}
