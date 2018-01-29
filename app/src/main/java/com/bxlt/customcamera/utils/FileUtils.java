package com.bxlt.customcamera.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;

import com.bxlt.customcamera.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

/**
 * 文件工具类
 * Created by Lrxc on 2017/11/15.
 */

public class FileUtils {
    private Context context;
    private final DisplayMetrics dm;

    public FileUtils(Context context) {
        this.context = context;
        dm = context.getResources().getDisplayMetrics();
    }

    // 保存图片到sd卡中
    public File saveToSDCard(byte[] data, Rect rect) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        Bitmap waterMarkBitmap = createWaterMarkBitmap(bitmap, rect);
        File fileFolder = new File(Environment.getExternalStorageDirectory() + "/Pictures/");

        if (!fileFolder.exists()) // 如果目录不存在，则创建一个名为"finger"的目录
            fileFolder.mkdirs();

        File jpgFile = new File(fileFolder, "signin_temp.jpeg");
        if (jpgFile.exists()) jpgFile.delete();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(jpgFile);
            waterMarkBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("lrxc", "saveToSDCard: " + e.getMessage());
        } finally {
            try {
                if (fos != null) fos.close(); // 关闭输出流
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jpgFile;
    }

    //添加水印
    private Bitmap createWaterMarkBitmap(Bitmap src, Rect rect) {
        //设置bitmap旋转
        Matrix matrix = new Matrix();
        matrix.setRotate(CameraParams.getInstance().oritation);
        src = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);

        if (rect != null) {
            //获取遮罩内的图片
            src = getRectBmp(src, rect);
        }
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int size = 30;
        int showWidth = rect.right - rect.left;
        //上下白边的高度和
        final int paddingSun = 200;
        int showHeight = rect.bottom - rect.top + paddingSun;

        //创建透明贴图--保证水印的大小
        Bitmap chartletBitmap = Bitmap.createBitmap(showWidth, showHeight, Bitmap.Config.ARGB_8888);
//        chartletBitmap.eraseColor(0x22000000);//设置透明度
        Canvas canvas = new Canvas(chartletBitmap);
        //贴图阴影
        Paint backPaint = new Paint();
        backPaint.setAlpha(50);
        Rect rect1 = new Rect(5, 5, showWidth - 5, showHeight - 5);
        canvas.drawBitmap(src, null, rect1, backPaint);

        //计算加上上线白边的真实高度
        int measureHeight = (int) ((float) srcHeight / (showHeight - paddingSun) * showHeight);
        //上下白边的高度
        int padTop = Math.abs(measureHeight - src.getHeight());
        //底图
        Bitmap nb = Bitmap.createBitmap(srcWidth, measureHeight, Bitmap.Config.ARGB_8888);
        Canvas cvnb = new Canvas(nb);
        cvnb.drawColor(Color.WHITE);
        cvnb.drawBitmap(src, 10, padTop / 2, null);

        //画笔
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(size);
        paint.setTextAlign(Paint.Align.CENTER);

        String format = DateFormat.getDateTimeInstance().format(new Date());
        canvas.drawText(format, showWidth / 2, paddingSun / 4 + size / 2, paint);
        canvas.drawText("username", 20, showHeight - paddingSun / 4, paint);

        Bitmap logoBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        canvas.drawBitmap(logoBitmap, showWidth - logoBitmap.getWidth() - 10, paddingSun / 2 + 10, null);

        //贴图进行缩放
        Bitmap scaleBitmap = scaleBitmap(chartletBitmap, srcWidth, measureHeight);
        cvnb.drawBitmap(scaleBitmap, 0, 0, null);
        return nb;
    }

    /**
     * 获取遮罩内的图像
     *
     * @param rect 遮罩矩形
     * @param bm   原图
     * @return 矩形内图像
     */
    private Bitmap getRectBmp(Bitmap bm, Rect rect) {
        int width = rect.right - rect.left;
        int hight = rect.bottom - rect.top;
        Log.i("ddms", "getRectBmp: " + rect.left + " " + rect.top + " " + width + " " + hight + " " + bm.getWidth() + " " + bm.getHeight());

        Bitmap rectbitmap = null;
        //横竖屏--按比例割取图片
        if (CameraParams.getInstance().oritation == 90) {
            int dpWidth = bm.getWidth();
            int dpHeight = bm.getHeight() * hight / dm.heightPixels;
            rectbitmap = Bitmap.createBitmap(bm, 0, bm.getHeight() * rect.top / dm.heightPixels, dpWidth, dpHeight);
        } else if (CameraParams.getInstance().oritation == 0) {
            int dpWidth = bm.getWidth() * width / dm.widthPixels;
            int dpHeight = bm.getHeight();
            rectbitmap = Bitmap.createBitmap(bm, bm.getWidth() * rect.left / dm.widthPixels, 0, dpWidth, dpHeight);
        }
        if (!bm.isRecycled()) {
            bm.recycle();
        }
        return rectbitmap;
    }

    /**
     * 根据给定的宽和高进行拉伸
     *
     * @param origin    原图
     * @param newWidth  新图的宽
     * @param newHeight 新图的高
     * @return 缩放后图片
     */
    private Bitmap scaleBitmap(Bitmap origin, int newWidth, int newHeight) {
        int height = origin.getHeight();
        int width = origin.getWidth();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (!origin.isRecycled()) {
            origin.recycle();
        }
        return newBM;
    }
}
