package com.bxlt.customcamera.utils;

import android.view.ScaleGestureDetector;

import com.bxlt.customcamera.camera.CameraPreviewView;

/**
 * 手势识别--相机镜头缩放
 * Created by Lrxc on 2017/11/15.
 */

public class ScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {
    private CameraPreviewView camePreview;
    private float mScaleFactor;

    public ScaleGestureListener(CameraPreviewView camePreview) {
        this.camePreview = camePreview;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        if (detector.getCurrentSpan() > mScaleFactor) {
            camePreview.zoomOut();
        } else {
            camePreview.zoomIn();
        }
        mScaleFactor = detector.getCurrentSpan();
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        mScaleFactor = detector.getCurrentSpan();
        //一定要返回true才会进入onScale()这个函数
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        mScaleFactor = detector.getCurrentSpan();
    }
}
