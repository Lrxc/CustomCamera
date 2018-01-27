package com.bxlt.customcamera.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;

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
    public int oritation;//旋转角度

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

    /**
     * @param context
     * @param camera
     * @param cameraId 前置 后置摄像头
     */

    public void setCameraParams(Context context, Camera camera, int cameraId) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        float i = ((float) width) / height;

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
        setOrientation(context, camera, cameraId);
        camera.setParameters(parameters);
    }

    //保证预览方向正确
    private void setOrientation(Context context, Camera camera, int cameraId) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = ((Activity) context).getWindowManager().getDefaultDisplay().getRotation();

        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        oritation = result;
//        Log.i("ddms", "setOrientation: " + oritation);
        camera.setDisplayOrientation(result);
    }

    //从列表中选取合适的分辨率
    private Size getProperSize(List<Size> pictureSizeList) {
        //降序排序
        Collections.sort(pictureSizeList, new CameraSizeComparator());

        for (Size size : pictureSizeList) {
            float currentRatio = ((float) size.width) / size.height;
            //大于最小尺寸且比例相等
//            if (size.width > minSize && currentRatio - screenRatio <= 0.03) {
            if (size.width > minSize) {
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
