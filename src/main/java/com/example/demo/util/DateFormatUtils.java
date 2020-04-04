package com.example.demo.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2020/4/4 0018.
 * 日期工具类
 */
public class DateFormatUtils {


    public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取当前日期时间
     *
     * @return
     */
    public static String getDateTime() {
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = format.format(curDate);
        return str;
    }

    /**
     * 时间戳转时间
     *
     * @param time
     * @return
     */
    public static String stampToDate(Date time) {
        String res = format.format(time);
        return res;
    }
}
