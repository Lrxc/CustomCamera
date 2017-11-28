package com.bxlt.customcamera.utils;

import android.graphics.PixelFormat;
import android.hardware.Camera;
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
    private static CameraParams cameraParams;
    private final int minSize = 640;//最小尺寸

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
    public void setCameraParams(Camera camera, int width, int height) {
        Log.i(TAG, "setCameraParams  width=" + width + "  height=" + height);
        Camera.Parameters parameters = camera.getParameters();
        // 获取摄像头支持的PictureSize列表
        List<Camera.Size> pictureSizeList = parameters.getSupportedPictureSizes();
        //从列表中选取合适的分辨率
        Camera.Size picSize = getProperSize(pictureSizeList, ((float) height / width));
        if (null != picSize) {
            Log.i(TAG, "picSize.width=" + picSize.width + "  picSize.height=" + picSize.height);
            parameters.setPictureSize(picSize.width, picSize.height);
        }
        // 获取摄像头支持的PreviewSize列表
        List<Camera.Size> previewSizeList = parameters.getSupportedPreviewSizes();
        Camera.Size preSize = getProperSize(previewSizeList, ((float) height) / width);
        if (null != preSize) {
            Log.i(TAG, "preSize.width=" + preSize.width + "  preSize.height=" + preSize.height);
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

    /**
     * 从列表中选取合适的分辨率
     * 默认w:h = 4:3
     * <p>注意：这里的w对应屏幕的height
     * h对应屏幕的width<p/>
     */
    private Camera.Size getProperSize(List<Camera.Size> pictureSizeList, float screenRatio) {
        //顺序排序
//        Collections.sort(pictureSizeList, new Comparator<Camera.Size>() {
//            @Override
//            public int compare(Camera.Size lhs, Camera.Size rhs) {
//                return ((Integer) lhs.width).compareTo(rhs.width);
//            }
//        });

        Log.i(TAG, "screenRatio=" + screenRatio);
        for (Camera.Size s : pictureSizeList) {
            Log.i(TAG, "getProperSize: " + s.width + "  " + s.height+"  "+((float) s.width) / s.height);
        }
        Camera.Size result = null;
        for (Camera.Size size : pictureSizeList) {
            float currentRatio = ((float) size.width) / size.height;
            //大于最小尺寸且比例相等
            if (size.width > minSize && currentRatio == screenRatio) {
                result = size;
                break;
            }
        }
        return result;
    }
}
