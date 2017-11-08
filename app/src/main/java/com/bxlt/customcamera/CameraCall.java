package com.bxlt.customcamera;

/**
 * Created by Lrxc on 2017/6/8.
 */

//一个类只能集成一个类，但可以实现很多个接口

//abstract class CameraCallBack {
//    // 相机拍照接口
//    abstract void onCameraData(byte[] data);
//}

interface CameraCall {
    // 相机拍照接口
    void onCameraData(byte[] data);
}
