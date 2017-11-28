package com.bxlt.customcamera.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 文件工具类
 * Created by Lrxc on 2017/11/15.
 */

public class FileUtils {

    // 保存图片到sd卡中
    public File saveToSDCard(byte[] data) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        File fileFolder = new File(Environment.getExternalStorageDirectory() + "/Pictures/");

        if (!fileFolder.exists()) // 如果目录不存在，则创建一个名为"finger"的目录
            fileFolder.mkdirs();

        File jpgFile = new File(fileFolder, "signin_temp.jpeg");
        if(jpgFile.exists())jpgFile.delete();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(jpgFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

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
}
