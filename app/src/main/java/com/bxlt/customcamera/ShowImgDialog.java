package com.bxlt.customcamera;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
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
 * Created by Lrxc on 2017/6/8.
 */

public class ShowImgDialog implements View.OnClickListener {
    private Context context;
    private Dialog dialog;
    private Bitmap waterMarkBitmap;

    public ShowImgDialog(Context context, byte[] data) {
        this.context = context;
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        waterMarkBitmap = createWaterMarkBitmap(bitmap);

        initView();
    }

    private void initView() {
        dialog = new Dialog(context);
        View view = View.inflate(context, R.layout.dialog_showimg, null);
        initDialog(view);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
//        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    private void initDialog(View view) {
        ImageView showimg = (ImageView) view.findViewById(R.id.showimg);
        view.findViewById(R.id.showimg_add).setOnClickListener(this);
        view.findViewById(R.id.showimg_delete).setOnClickListener(this);

        showimg.setImageBitmap(waterMarkBitmap);
    }

    private Bitmap createWaterMarkBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(30);

        canvas.drawBitmap(bitmap, 0, 0, paint);
        String format = DateFormat.getDateTimeInstance().format(new Date());
        canvas.drawText(format, 20, 50, paint);

        return newBitmap;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showimg_add:
                try {
                    saveToSDCard(waterMarkBitmap); // 保存图片到sd卡中
                    Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.showimg_delete:
                dialog.dismiss();
                break;
        }
    }

    // 保存图片到sd卡中
    private void saveToSDCard(Bitmap bitmap) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM_ddhhMMss", Locale.getDefault());
        String filename = dateFormat.format(new Date()) + ".jpg";
        File fileFolder = new File(Environment.getExternalStorageDirectory() + "/DCIM/finger/");

        if (!fileFolder.exists()) // 如果目录不存在，则创建一个名为"finger"的目录
            fileFolder.mkdirs();

        File jpgFile = new File(fileFolder, filename);
        FileOutputStream fos = new FileOutputStream(jpgFile); // 文件输出流
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        fos.flush();
        fos.close(); // 关闭输出流
    }
}
