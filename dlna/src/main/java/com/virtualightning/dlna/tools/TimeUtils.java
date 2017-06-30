package com.virtualightning.dlna.tools;


public class TimeUtils {
    public static String millis2Str(long millis) {
        millis = millis / 1000;
        long second = millis % 60;
        millis /= 60;
        long minute = millis % 60;
        long hour = millis / 60;
        String timeStr = "";
        if (hour < 10)
            timeStr += "0";
        timeStr += hour;
        timeStr += ":";
        if (minute < 10)
            timeStr += "0";
        timeStr += minute;
        timeStr += ":";
        if (second < 10)
            timeStr += "0";
        timeStr += second;

        return timeStr;
    }


    public static long str2Millis(String timeStr) {
        String timeArray[] = timeStr.split(":");

        if(timeArray.length != 3)
            return -1;

        long time = 0;
        //小时
        time += Integer.parseInt(timeArray[0]) * 3600;
        //分钟
        time += Integer.parseInt(timeArray[1]) * 60;
        //秒
        time += Integer.parseInt(timeArray[2]);

        time *= 1000;

        return time;
    }
}
