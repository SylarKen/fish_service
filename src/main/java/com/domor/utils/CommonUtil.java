package com.domor.utils;

import java.util.List;
import java.util.Random;

/**
 * @desc: cron表达式转换工具类
 * @auther: liyuyang
 * @date: 2019/12/16
 **/
public class CommonUtil {

    /**
     * 获取cron表达式，每天定时（多个）
     * @param times hh:mm:ss
     * @return
     */
    public static String getCron(List<String> times) {
        String cron;
        String hs = "";
        String ms = "";
        String ss = "";
        for (String time : times) {
            String h = time.substring(0, 2);
            String m = time.substring(3, 5);
            String s = time.substring(6, 8);
            if(!hs.contains(h)) hs = hs.equals("") ? h : (hs + "," + h);
            if(!ms.contains(m)) ms = ms.equals("") ? m : (ms + "," + m);
            if(!ss.contains(s)) ss = ss.equals("") ? s : (ss + "," + s);
        }
        cron = ss + " " + ms + " " + hs + " * * ?";
        return cron;
    }

    /**
     * 获取cron表达式，每天定时(一个）
     * @param time hh:mm:ss
     * @return
     */
    public static String getCron(String time) {
        String h = time.substring(0, 2);
        String m = time.substring(3, 5);
        String s = time.substring(6, 8);
        String cron = s + " " + m + " " + h + " * * ?";
        return cron;
    }

    /**
     * 生成随机字符串
     * @param length
     * @return
     */
    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

}
