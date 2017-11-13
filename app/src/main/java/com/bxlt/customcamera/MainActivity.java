package com.bxlt.customcamera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private int REQ_01 = 1;
    private int REQ_02 = 2;
    private ImageView img;
    private TextView tv;
    private String photoPath;//图片保存路径

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = (ImageView) findViewById(R.id.img);
        tv = (TextView) findViewById(R.id.tv);
        photoPath = Environment.getExternalStorageDirectory() + "/test.jgp";
    }

    //缩略图
    public void gotoCamera1(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQ_01);
    }

    //真实图
    public void gotoCamera2(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = Uri.fromFile(new File(photoPath));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, REQ_02);
    }

    //自定义相机
    public void gotoCamera3(View view) {
        startActivity(new Intent(MainActivity.this, TwoTakeActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_01) {
                Bundle extras = data.getExtras();
                Bitmap bitmap = (Bitmap) extras.get("data");
                tv.setText(bitmap.getByteCount() + "");
                img.setImageBitmap(bitmap);
            } else if (requestCode == REQ_02) {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(photoPath);
                    Bitmap bitmap = BitmapFactory.decodeStream(fis);
                    tv.setText(bitmap.getByteCount() + "");
                    img.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
