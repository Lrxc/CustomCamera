package com.bxlt.customcamera.utils;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 设置相机参数
 * Created by Lrxc on 2017/11/24.
 */

public class CameraParams {
    private final String TAG = "params";
    private volatile static CameraParams cameraParams;

    private final int minSize = 640;//最小尺寸
    private final double screenRatio = 1.33;//长宽比

    private CameraParams() {
    }

    public static CameraParams getInstance() {
        if (cameraParams == null) {
            synchronized (CameraParams.class) {
                if (cameraParams == null) {
                    cameraParams = new CameraParams();
                }
            }
        }
        return cameraParams;
    }

    //设置相机参数
    public void setCameraParams(Context context, Camera camera) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        float i = ((float) width) / height;
        Log.i(TAG, "setCameraParams  width=" + width + "  height=" + height+i);

        Camera.Parameters parameters = camera.getParameters();
        // 获取摄像头支持的PictureSize列表
        List<Size> pictureSizeList = parameters.getSupportedPictureSizes();
        //从列表中选取合适的分辨率
        Size picSize = getProperSize(pictureSizeList);
        if (null != picSize) {
            Log.i(TAG, "picSize.width==================" + picSize.width + "  picSize.height=" + picSize.height);
            parameters.setPictureSize(picSize.width, picSize.height);
        }

        // 获取摄像头支持的PreviewSize列表
        List<Size> previewSizeList = parameters.getSupportedPreviewSizes();
        Size preSize = getProperSize(previewSizeList);
        if (null != preSize) {
            Log.i(TAG, "preSize.width==================" + preSize.width + "  preSize.height=" + preSize.height);
            parameters.setPreviewSize(preSize.width, preSize.height);
        }
        // 连续对焦模式
        if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        parameters.setPictureFormat(PixelFormat.JPEG); // 设置图片格式
        parameters.setJpegQuality(100); // 设置照片质量
        parameters.set("orientation", "portrait");
        camera.setDisplayOrientation(90);// 设置PreviewDisplay的方向，效果就是将捕获的画面旋转多少度显示
        camera.setParameters(parameters);
    }

    //从列表中选取合适的分辨率
    private Size getProperSize(List<Size> pictureSizeList) {
        //降序排序
        Collections.sort(pictureSizeList, new CameraSizeComparator());

        for (Size size : pictureSizeList) {
            float currentRatio = ((float) size.width) / size.height;
            //大于最小尺寸且比例相等
            if (size.width > minSize && currentRatio - screenRatio <= 0.03) {
                return size;
            }
        }
        return null;
    }

    private class CameraSizeComparator implements Comparator<Size> {
        //降序排列
        public int compare(Size lhs, Size rhs) {
            return ((Integer) rhs.width).compareTo(lhs.width);
        }
    }
}
