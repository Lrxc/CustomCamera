package com.bxlt.customcamera.camera;

/**
 * Created by Lrxc on 2017/6/8.
 */

//一个类只能集成一个类，但可以实现很多个接口

public interface CameraCall {
    // 相机拍照接口
    void onCameraData(byte[] data);
}
