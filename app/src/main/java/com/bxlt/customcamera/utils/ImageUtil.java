package com.bxlt.customcamera.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;

/**
 * 希望有一天可以开源出来 org.zhx
 *
 * @author zhx
 * @version 1.0, 2015-11-15 下午7:21:09
 */

public class ImageUtil {

    private static final String TAG = ImageUtil.class.getSimpleName();

    /**
     * @throws Exception
     * @author zhx
     */
    public static Bitmap getRectBmp(Rect rect, Bitmap bm) {
        int width = rect.right - rect.left;
        int hight = rect.bottom - rect.top;

        Log.i("ddms", "getRectBmp: " + rect.left + " " + rect.top + " " + width + " " + hight + " " + bm.getWidth() + " " + bm.getHeight());
        Log.i("ddms", "getRectBmp: " + CameraParams.getInstance().oritation);
        Bitmap rectbitmap = null;

        //判断屏幕旋转角度
        if (CameraParams.getInstance().oritation == 90) {
            rectbitmap = Bitmap.createBitmap(bm, 0, bm.getHeight() * rect.top / 1920, bm.getWidth(), bm.getHeight() * hight / 1920);
        } else if (CameraParams.getInstance().oritation == 0) {
            rectbitmap = Bitmap.createBitmap(bm, bm.getWidth() * rect.left / 1920, 0, bm.getWidth() * width / 1920, bm.getHeight());
        }
        return rectbitmap;
    }

    //使用Bitmap加Matrix来缩放
    public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                height, matrix, true);
        BitmapOrg.recycle();
        return resizedBitmap;
    }


    /**
     * @param
     * @return
     * @throws Exception
     * @author zhx
     */
    public static void recycleBitmap(Bitmap bitmap) {
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

}
