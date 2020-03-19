package com.supermap.zq.main.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 时间工具类
 *
 * @author GZQ
 */
public class DateUtil {

    /**
     * 获取时间字符串  yyyyMMddHH
     * @return
     */
    public static String getYMDHMString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHH");
        Date date = new Date();
        return formatter.format(date);
    }

    /**
     * 获取通用时间格式字符串
     * @return
     */
    public static String getGeneralString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);
    }

    public static String getYMDString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHH");
        Date date = new Date();
        return formatter.format(date);
    }

    /**
     * 从字符串获取数字
     * @param string
     * @return
     */
    public static String getNumber(String string) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(string);
        return m.replaceAll("").trim();
    }

    /**
     * 返回五小时前时间的毫秒数
     * @return
     */
    public static long get5hDateTimes(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -5);
        return calendar.getTimeInMillis();
    }

    /**
     * 返回7天前时间的毫秒数
     * @return
     */
    public static long get7dDateTimes(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -7);
        return calendar.getTimeInMillis();
    }

    /**
     * 返回3天前时间的毫秒数
     * @return
     */
    public static long get3dDateTimes(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -3);
        return calendar.getTimeInMillis();
    }

    /**
     * 返回3天前时间的毫秒数
     * @return
     */
    public static long getNdDateTimes(int n){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -n);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取指定时间，指定格式字符串的毫秒数
     * @param string
     * @return
     */
    public static long getCurrDataTimes(String string){
        Calendar calendar = Calendar.getInstance();//此时打印它获取的是系统当前时间
        try {
            SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMddHH");
            Date date = sdf.parse(string);
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar.getTimeInMillis();
    }

  /*  public static void main(String[] args) {
        System.out.println(DateUtil.getCurrDataTimes(getNumber("l2019031310351").substring(0, 10)));
        System.out.println(get7dDateTimes());
    }*/
}
