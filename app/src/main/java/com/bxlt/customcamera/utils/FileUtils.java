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
    private Bitmap createWaterMarkBitmap(Bitmap bitmap, Rect rect) {
        //设置bitmap旋转
        Matrix matrix = new Matrix();
        matrix.setRotate(CameraParams.getInstance().oritation);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        //宽高为矩形的宽高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (rect != null) {
            //获取遮罩内的图片
            bitmap = getRectBmp(bitmap, rect);
            width = rect.right - rect.left;
            height = rect.bottom - rect.top;
        }
        //创建透明贴图--保证水印的大小
        Bitmap chartletBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        chartletBitmap.eraseColor(Color.TRANSPARENT);//设置透明
        Canvas canvas = new Canvas(chartletBitmap);

        //画笔
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(50);

        String format = DateFormat.getDateTimeInstance().format(new Date());
        canvas.drawText(format, 20, 50, paint);
        canvas.drawText("username", 20, 100, paint);

        Bitmap logoBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        canvas.drawBitmap(logoBitmap, chartletBitmap.getWidth() - logoBitmap.getWidth() - 20, 0, null);
        //贴图进行缩放
        Bitmap scaleBitmap = scaleBitmap(chartletBitmap, bitmap.getWidth(), bitmap.getHeight());

        //原图作为底图
        Canvas canvasBase = new Canvas(bitmap);
        canvasBase.drawBitmap(scaleBitmap, 0, 0, null);
        return bitmap;
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
            rectbitmap = Bitmap.createBitmap(bm, 0, bm.getHeight() * rect.top / dm.heightPixels, bm.getWidth(), bm.getHeight() * hight / dm.heightPixels);
        } else if (CameraParams.getInstance().oritation == 0) {
            rectbitmap = Bitmap.createBitmap(bm, bm.getWidth() * rect.left / dm.heightPixels, 0, bm.getWidth() * width / dm.heightPixels, bm.getHeight());
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
