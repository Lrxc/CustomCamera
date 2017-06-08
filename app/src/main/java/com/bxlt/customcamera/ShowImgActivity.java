package com.bxlt.customcamera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Lrxc on 2017/6/8.
 */

public class ShowImgActivity extends AppCompatActivity implements View.OnClickListener {
    private byte[] data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showimg);

        data = getIntent().getByteArrayExtra("data");
        initView();
    }

    private void initView() {
        ImageView showimg = (ImageView) findViewById(R.id.showimg);
        findViewById(R.id.showimg_add).setOnClickListener(this);
        findViewById(R.id.showimg_delete).setOnClickListener(this);

        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        showimg.setImageBitmap(bitmap);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.showimg_add:
                try {
                    saveToSDCard(data); // 保存图片到sd卡中
                    finish();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.showimg_delete:
                finish();
                break;
        }
    }

    public void saveToSDCard(byte[] data) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); // 格式化时间
        String filename = format.format(new Date()) + ".jpg";
        File fileFolder = new File(Environment.getExternalStorageDirectory() + "/DCIM/finger/");
        if (!fileFolder.exists()) // 如果目录不存在，则创建一个名为"finger"的目录
            fileFolder.mkdirs();

        File jpgFile = new File(fileFolder, filename);
        FileOutputStream outputStream = new FileOutputStream(jpgFile); // 文件输出流
        outputStream.write(data); // 写入sd卡中
        outputStream.close(); // 关闭输出流

        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
    }
}
