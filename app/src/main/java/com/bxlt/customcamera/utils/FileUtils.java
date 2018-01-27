package com.bxlt.customcamera.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Environment;
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

    public FileUtils(Context context) {
        this.context = context;
    }

    // 保存图片到sd卡中
    public File saveToSDCard(byte[] data, Rect rect, Point p, int cameraPosition) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        Bitmap waterMarkBitmap = createWaterMarkBitmap(bitmap, rect, p, cameraPosition);
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
                fos.close(); // 关闭输出流
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jpgFile;
    }

    //添加水印
    private Bitmap createWaterMarkBitmap(Bitmap bitmap, Rect rect, Point p, int cameraPosition) {
        //设置bitmap旋转90度
        Matrix matrix = new Matrix();
        //后置摄像头旋转角度
        if (cameraPosition == 1)
            matrix.setRotate(90);
        else matrix.setRotate(-90);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (rect != null) {
            //遮罩
            bitmap = ImageUtil.getRectBmp(rect, bitmap, p);
        }

        //画笔
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(30);

        //画布
        Canvas canvas = new Canvas(bitmap);
        String format = DateFormat.getDateTimeInstance().format(new Date());
        canvas.drawText(format, 20, 50, paint);
        canvas.drawText("username", 20, 100, paint);

        Bitmap logoBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        canvas.drawBitmap(logoBitmap, bitmap.getWidth()-logoBitmap.getWidth()-20, 0, null);

        return bitmap;
    }
}
