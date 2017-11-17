package com.bxlt.customcamera.activity;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bxlt.customcamera.camera.CameraCall;
import com.bxlt.customcamera.camera.CameraPreviewView;
import com.bxlt.customcamera.R;
import com.bxlt.customcamera.camera.ShowImgDialog;

/**
 * 拍照界面
 * Created by Lrxc on 2017/6/8.
 */

public class TwoTakeActivity extends AppCompatActivity implements View.OnClickListener, CameraCall {
    private CameraPreviewView camePreview;
    private int mSgType = 2;//1 不开启闪光灯 2自动 3长亮
    private ImageView mIvFlash;// 闪光灯按钮
    private EditText edtCommInfo;//备注信息
    private ScaleGestureDetector gestureDetector;//缩放手势

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camerapreview);

        initView();
    }

    private void initView() {
        camePreview = (CameraPreviewView) findViewById(R.id.camePreview);
        mIvFlash = (ImageView) findViewById(R.id.img_left_flash);
        edtCommInfo = (EditText) findViewById(R.id.edtCommInfo);
        camePreview.setOnCameraListener(this);
        mIvFlash.setOnClickListener(this);
        findViewById(R.id.camera_take).setOnClickListener(this);
        findViewById(R.id.camera_front).setOnClickListener(this);
        findViewById(R.id.imageView1).setOnClickListener(this);
        findViewById(R.id.img_focus).setOnClickListener(this);

        //缩放手势
        gestureDetector = new ScaleGestureDetector(this, new ScaleGestureListener());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera_take:
                //take photo button
                camePreview.takePicture();
                break;
            case R.id.camera_front:
                //switch front camera
                camePreview.switchFrontCamera();
                break;
            case R.id.img_left_flash:
                //闪光灯模式
                switchFlashMode();
                break;
            case R.id.img_focus:
                //自动对焦
                camePreview.autoFocus();
                break;
            case R.id.imageView1:
                finish();
                break;
        }
    }

    //切换闪关灯模式
    private void switchFlashMode() {
        switch (mSgType) {
            case 1:
                mIvFlash.setImageResource(R.drawable.ic_camera_top_bar_flash_auto_normal);
                camePreview.setIsOpenFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                mSgType = 2;
                break;
            case 2:
                mIvFlash.setImageResource(R.drawable.ic_camera_top_bar_flash_torch_normal);
                camePreview.setIsOpenFlashMode(Camera.Parameters.FLASH_MODE_ON);
                mSgType = 3;
                break;
            case 3:
                mIvFlash.setImageResource(R.drawable.ic_camera_top_bar_flash_off_normal);
                camePreview.setIsOpenFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                mSgType = 1;
                break;
        }
    }

    @Override
    public void onCameraData(byte[] data) {
        //备注信息
        String s = edtCommInfo.getText().toString();
        //拍照完成，弹窗并保存
        new ShowImgDialog(this, data, camePreview);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //识别手势
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class ScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {
        float mScaleFactor;

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
}
