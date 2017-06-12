package com.bxlt.customcamera;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * 自定义View相机
 * Created by Lrxc on 2017/6/8.
 */

public class CameraPreviewView extends SurfaceView {
    private String TAG = "lrxc";
    private Camera camera;
    private Activity activity;
    private static SurfaceHolder holder;
    private int cameraPosition = 1; //当前选用的摄像头，1后置 0前置


    // Preview类的构造方法
    public CameraPreviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        activity = (Activity) context;

        // 获得SurfaceHolder对象
        SurfaceHolder holder = getHolder();
        holder.addCallback(new SurfaceCallback());// 指定用于捕捉拍照事件的SurfaceHolder.Callback对象
        holder.setKeepScreenOn(true);// 屏幕常亮
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); // 设置SurfaceHolder对象的类型
    }

    private class SurfaceCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            CameraPreviewView.holder = holder;
            try {
                camera = Camera.open();// 打开摄像头
                camera.setPreviewDisplay(holder);// 设置用于显示拍照影像的SurfaceHolder对象
                camera.setDisplayOrientation(getPreviewDegree(activity));// 屏幕方向
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

    //根据手机方向获得相机预览画面旋转的角度
    private int getPreviewDegree(Activity activity) {
        // 获得手机的方向
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degree = 0;
        // 根据手机的方向计算相机预览画面应该选择的角度
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 90;
                break;
            case Surface.ROTATION_90:
                degree = 0;
                break;
            case Surface.ROTATION_180:
                degree = 270;
                break;
            case Surface.ROTATION_270:
                degree = 180;
                break;
        }
        return degree;
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
            camera.stopPreview(); // 停止照片拍摄

            if (listener != null) {
                listener.onCameraData(data);
            }
        }
    }

    public void start() {
        if (camera != null) {
            camera.startPreview();
        }
    }

    //switch photo camera
    public void switchFrontCamera() {
        //切换前后摄像头
        int cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();

        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
            if (cameraPosition == 1) {
                //现在是后置，变更为前置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                    camera.stopPreview();//停掉原来摄像头的预览
                    camera.release();//释放资源
                    camera = null;//取消原来摄像头

                    try {
                        camera = Camera.open(i);//打开当前选中的摄像头
                        camera.setPreviewDisplay(holder);//通过surfaceview显示取景画面
                        camera.startPreview();//开始预览
                        cameraPosition = 0;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            } else {
                //现在是前置， 变更为后置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                    camera.stopPreview();//停掉原来摄像头的预览
                    camera.release();//释放资源
                    camera = null;//取消原来摄像头

                    try {
                        camera = Camera.open(i);//打开当前选中的摄像头
                        camera.setPreviewDisplay(holder);//通过surfaceview显示取景画面
                        camera.startPreview();//开始预览
                        cameraPosition = 1;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }
}
