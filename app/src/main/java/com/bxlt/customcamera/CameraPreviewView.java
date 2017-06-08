package com.bxlt.customcamera;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * 自定义View相机
 * Created by Lrxc on 2017/6/8.
 */

public class CameraPreviewView extends SurfaceView {
    private Camera camera;

    // Preview类的构造方法
    public CameraPreviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 获得SurfaceHolder对象
        SurfaceHolder holder = getHolder();
        // 指定用于捕捉拍照事件的SurfaceHolder.Callback对象
        holder.addCallback(new SurfaceCallback());
        // 设置SurfaceHolder对象的类型
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    private class SurfaceCallback implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                camera = Camera.open();// 打开摄像头
                camera.setPreviewDisplay(holder);// 设置用于显示拍照影像的SurfaceHolder对象
                camera.startPreview();// 开始预览
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Camera.Parameters parameters = camera.getParameters();// 获取各项参数
            parameters.setPictureFormat(PixelFormat.JPEG); // 设置图片格式
            parameters.setPreviewSize(width, height); // 设置预览大小
            parameters.setPreviewFrameRate(5);  //设置每秒显示4帧
            parameters.setPictureSize(width, height); // 设置保存的图片尺寸
            parameters.setJpegQuality(100); // 设置照片质量
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (camera != null) {
                camera.release(); // 释放照相机
                camera = null;
            }
        }
    }

    private CameraCall listener;

    // 相机拍照事件
    public void setOnCameraListener(CameraCall listener) {
        this.listener = listener;
    }

    //开始拍照
    public void takePicture() {
        if (camera != null) {
            try {
                camera.takePicture(null, null, new MyPictureCallback());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class MyPictureCallback implements Camera.PictureCallback {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            camera.startPreview(); // 停止照片拍摄
            if (listener != null) {
                listener.onCameraData(data);
            }
        }
    }

    public void switchFrontCamera() {

    }
}
