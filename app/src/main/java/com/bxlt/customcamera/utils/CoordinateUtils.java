package com.bxlt.customcamera.utils;

/**
 * 坐标工具类
 * Created by Lrxc on 2017/11/15.
 */

public class CoordinateUtils {
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
