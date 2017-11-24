package com.bxlt.customcamera.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.Log;

/**
 * Created by Lrxc on 2017/11/24.
 */
public class MyCamPara {
    private String tag = "lrxc";
    private CameraSizeComparator sizeComparator = new CameraSizeComparator();
    private static MyCamPara myCamPara = null;

    private MyCamPara() {
    }

    public static MyCamPara getInstance() {
        if (myCamPara == null) {
            myCamPara = new MyCamPara();
            return myCamPara;
        } else {
            return myCamPara;
        }
    }

    public Size getPreviewSize(List<Camera.Size> list, int th) {
        Collections.sort(list, sizeComparator);
        for (Size s : list) {
            Log.i("lrxc", "getPreviewSize: " + s.width + "-" + s.height + "  " + ((float) s.width) / s.height);
        }

        int i = 0;
        for (Size s : list) {
            if ((s.width > th) && equalRate(s, 1.33f)) {
                Log.i(tag, "最终设置预览尺寸:w = " + s.width + "h = " + s.height);
                break;
            }
            i++;
        }

        return list.get(i);
    }

    public Size getPictureSize(List<Camera.Size> list, int th) {
        Collections.sort(list, sizeComparator);

        int i = 0;
        for (Size s : list) {
            if ((s.width > th) && equalRate(s, 1.33f)) {
                Log.i(tag, "最终设置图片尺寸:w = " + s.width + "h = " + s.height);
                break;
            }
            i++;
        }

        return list.get(i);
    }

    private boolean equalRate(Size s, float rate) {
        float r = (float) (s.width) / (float) (s.height);
        if (Math.abs(r - rate) <= 0.2) {
            return true;
        } else {
            return false;
        }
    }

    private class CameraSizeComparator implements Comparator<Camera.Size> {
        //按升序排列
        public int compare(Size lhs, Size rhs) {
            return ((Integer) lhs.width).compareTo(rhs.width);
        }
    }
}