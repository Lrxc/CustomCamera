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

    //计算经纬度距离的固定值
    private final double EARTH_RADIUS = 6378137.0;

    // 计算两点距离
    public double gps2m(double lat_a, double lng_a, double lat_b, double lng_b) {
        double radLat1 = (lat_a * Math.PI / 180.0);
        double radLat2 = (lat_b * Math.PI / 180.0);
        double a = radLat1 - radLat2;
        double b = (lng_a - lng_b) * Math.PI / 180.0;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }
}
