package com.bxlt.customcamera;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

/**
 * Created by Lrxc on 2017/6/8.
 */

public class TwoTakeActivity extends AppCompatActivity implements View.OnClickListener, CameraCall {
    private CameraPreviewView camePreview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camerapreview);

        initView();
    }

    private void initView() {
        camePreview = (CameraPreviewView) findViewById(R.id.camePreview);
        camePreview.setOnCameraListener(this);
        findViewById(R.id.camera_take).setOnClickListener(this);
        findViewById(R.id.camera_front).setOnClickListener(this);
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
        }

    }

    @Override
    public void onCameraData(byte[] data) {
        //拍照完成，弹窗并保存
        new ShowImgDialog(TwoTakeActivity.this, data, camePreview);
    }
}
