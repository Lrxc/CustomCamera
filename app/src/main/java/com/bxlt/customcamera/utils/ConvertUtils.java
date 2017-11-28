package com.bxlt.customcamera.utils;

import android.location.Location;

/**
 * 经纬度 度分秒转换
 * Created by Lrxc on 2017/11/24.
 */

public class ConvertUtils {
    //经纬度转度分秒
    public static String convertToDegree(double gpsInfo) {
//        double limit = Double.parseDouble((gpsInfo + "").split("\\.")[0]);
//        double minute = (gpsInfo - limit) * 60;
//        double second = ((gpsInfo - limit) * 60 - (int) minute) * 60;

        String dms = Location.convert(gpsInfo, Location.FORMAT_SECONDS);
        String[] splits = dms.split(":");
        String[] secnds = (splits[2]).split("\\.");
        String seconds;
        //截取小数部分
        if (secnds.length == 0) {
            seconds = splits[2];
        } else {
            seconds = secnds[0];
        }
        //必须这种格式,不能有小数 否则Android6.0写入照片属性失败
        String s = splits[0] + "/1," + splits[1] + "/1," + seconds + "/1";
        return s;
    }

    //度分秒转经纬度
    public static Double convertToCoordinate(String stringDMS) {
//        double str = limit + minute / 60 + second / 3600;

        if (stringDMS == null) return null;
        String[] split = stringDMS.split(":", 3);
        return Double.parseDouble(split[0]) + Double.parseDouble(split[1]) / 60 + Double.parseDouble(split[2]) / 3600;
    }
}
