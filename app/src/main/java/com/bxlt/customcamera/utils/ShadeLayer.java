package com.bxlt.customcamera.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.util.DisplayMetrics;

import com.bxlt.customcamera.camera.OverlayerView;

/**
 * Created by Lrxc on 2018/1/28.
 * <p>
 * 测量遮罩Padding
 */

public class ShadeLayer {
    private Context context;
    //屏幕的宽高
    private final int width, height;

    public ShadeLayer(Context context) {
        this.context = context;

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        width = dm.widthPixels;
        height = dm.heightPixels;
    }

    /**
     * @param shadeView 遮罩的View
     */
    public Rect measurePadding(OverlayerView shadeView) {
        Rect shadeRect;
        //遮罩--竖屏
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            int measureHigth = width * 3 / 4;
            int i = (height - measureHigth) / 2;
            shadeRect = new Rect(0, i, width, i + measureHigth);
        } else {//横屏
            int measureWidth = height * 4 / 3;
            int i = (width - measureWidth) / 2;
            shadeRect = new Rect(i, 0, i + measureWidth, height);
        }
        shadeView.setmCenterRect(shadeRect);
        return shadeRect;
    }
}
