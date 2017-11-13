package com.bxlt.customcamera;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 保存照片弹窗dialog
 * Created by Lrxc on 2017/6/8.
 */

public class ShowImgDialog implements View.OnClickListener {
    private Context context;
    private CameraPreviewView camePreview;
    private Dialog dialog;
    private Bitmap waterMarkBitmap;
    private ImageView showimg;
    private File jpgFile;

    public ShowImgDialog(Context context, byte[] data, CameraPreviewView camePreview) {
        this.context = context;
        this.camePreview = camePreview;
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        waterMarkBitmap = createWaterMarkBitmap(bitmap);

        saveToSDCard(waterMarkBitmap); // 保存图片到sd卡中

        initView();
    }

    private void initView() {
        dialog = new Dialog(context);
        View view = View.inflate(context, R.layout.dialog_showimg, null);
        initDialog(view);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
//        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.show();
    }

    private void initDialog(View view) {
        showimg = (ImageView) view.findViewById(R.id.showimg);
        view.findViewById(R.id.showimg_add).setOnClickListener(this);
        view.findViewById(R.id.showimg_delete).setOnClickListener(this);

    }

    private Bitmap createWaterMarkBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(20);

        canvas.drawBitmap(bitmap, 0, 0, paint);
        String format = DateFormat.getDateTimeInstance().format(new Date());
        canvas.drawText(format, 20, 50, paint);

        return newBitmap;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showimg_add:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        saveToSDCard(waterMarkBitmap); // 保存图片到sd卡中
                    }
                }).start();

                dialog.dismiss();
                camePreview.start();//重新开始
                break;
            case R.id.showimg_delete:
                waterMarkBitmap = null;

                dialog.dismiss();
                camePreview.start();//重新开始
                break;
        }
    }

    // 保存图片到sd卡中
    private void saveToSDCard(Bitmap bitmap) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM_ddhhMMss", Locale.getDefault());
        String filename = dateFormat.format(new Date()) + ".jpg";
        File fileFolder = new File(Environment.getExternalStorageDirectory() + "/DCIM/finger/");

        if (!fileFolder.exists()) // 如果目录不存在，则创建一个名为"finger"的目录
            fileFolder.mkdirs();

        jpgFile = new File(fileFolder, filename);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(jpgFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

            handler.sendEmptyMessage(0x001);
        } catch (Exception e) {
            e.printStackTrace();
            handler.sendEmptyMessage(0x002);
        } finally {
            try {
                fos.flush();
                fos.close(); // 关闭输出流
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0x001){
                Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
                showimg.setImageBitmap(BitmapFactory.decodeFile(jpgFile.getAbsolutePath()));
            }
            if (msg.what == 0x002)
                Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show();
            return false;
        }
    });
}
