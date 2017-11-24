package com.bxlt.customcamera.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.bxlt.customcamera.utils.CameraParams;

import java.io.IOException;

/**
 * 自定义View相机
 * Created by Lrxc on 2017/6/8.
 */

public class CameraPreviewView extends SurfaceView {
    private Camera camera;
    private SurfaceHolder holder;
    public int cameraPosition = 1; //当前选用的摄像头，1后置 0前置

    // Preview类的构造方法
    public CameraPreviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 获得SurfaceHolder对象
        holder = getHolder();
        holder.addCallback(new SurfaceCallback());// 指定用于捕捉拍照事件的SurfaceHolder.Callback对象
        holder.setKeepScreenOn(true);// 屏幕常亮
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); // 设置SurfaceHolder对象的类型
    }

    private class SurfaceCallback implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            //打开预览
            reStartCamera(0);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (camera != null) {
                camera.release(); // 释放照相机
                camera = null;
            }
        }
    }


    // 自动对焦
    public void autoFocus() {
        camera.autoFocus(null);//自动对焦 不需要回调
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
//            new CameraTask(data,camera,getContext()).execute();
            if (listener != null) {
                listener.onCameraData(data);
            }
        }
    }

    //保存图片后重新开始
    public void start() {
        if (camera != null) {
            camera.startPreview();
        }
    }

    //放大
    public void zoomOut() {
        Camera.Parameters parameters = camera.getParameters();
        if (!parameters.isZoomSupported()) return;

        int zoom = parameters.getZoom() + 1;
        if (zoom < parameters.getMaxZoom()) {
            parameters.setZoom(zoom);
            camera.setParameters(parameters);
        }
    }

    //缩小
    public void zoomIn() {
        Camera.Parameters parameters = camera.getParameters();
        if (!parameters.isZoomSupported()) return;

        int zoom = parameters.getZoom() - 1;
        if (zoom >= 0) {
            parameters.setZoom(zoom);
            camera.setParameters(parameters);
        }
    }

    //设置开启闪光灯(重新预览)
    public void setIsOpenFlashMode(String mIsOpenFlashMode) {
        Camera.Parameters mParameters = camera.getParameters();
        //设置闪光灯模式
        mParameters.setFlashMode(mIsOpenFlashMode);
        camera.setParameters(mParameters);
    }

    //重新打开预览
    public void reStartCamera(int i) {
        if (camera != null) {
            camera.stopPreview();//停掉原来摄像头的预览
            camera.release();//释放资源
            camera = null;//取消原来摄像头
        }
        try {
            camera = Camera.open(i);//打开当前选中的摄像头
            camera.setPreviewDisplay(holder);//通过surfaceview显示取景画面
//            camera.setDisplayOrientation(90);// 屏幕方向
            camera.startPreview();//开始预览

            //设置相机参数
            DisplayMetrics dm = getResources().getDisplayMetrics();
            CameraParams.getInstance().setCameraParams(camera, dm.widthPixels, dm.heightPixels);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //切换摄像头
    public void switchFrontCamera() {
        int cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();

        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
            if (cameraPosition == 1) {
                //现在是后置，变更为前置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                    //重新打开
                    reStartCamera(i);
                    cameraPosition = 0;
                    break;
                }
            } else {
                //现在是前置， 变更为后置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                    reStartCamera(i);
                    cameraPosition = 1;
                    break;
                }
            }
        }
    }
}
