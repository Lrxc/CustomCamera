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
        return dms;
    }

    //度分秒转经纬度
    public static Double convertToCoordinate(String stringDMS) {
//        double str = limit + minute / 60 + second / 3600;

        if (stringDMS == null) return null;
        String[] split = stringDMS.split(":", 3);
        return Double.parseDouble(split[0]) + Double.parseDouble(split[1]) / 60 + Double.parseDouble(split[2]) / 3600;
    }
}
