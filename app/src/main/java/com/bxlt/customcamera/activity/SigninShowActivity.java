package com.bxlt.customcamera.activity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bxlt.customcamera.R;

import java.text.DateFormat;
import java.util.Date;

/**
 * 签到--拍照成功预览页
 * Created by Lrxc on 2017/11/15.
 */

public class SigninShowActivity extends AppCompatActivity implements View.OnClickListener {
    private String jpgFile;//拍照字节流
    private int cameraPosition;//当前选用的摄像头

    private Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signinshow);

        jpgFile = getIntent().getStringExtra("jpgFile");
        cameraPosition = getIntent().getIntExtra("cameraPosition", 404);

        initToolbar();
        initView();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("签到确认");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            outHint();
        }
        return super.onOptionsItemSelected(item);
    }

    //退出提示
    private void outHint() {
        new AlertDialog.Builder(this)
                .setTitle("提示").setMessage("是否取消签到")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(RESULT_OK);//关闭拍照页面
                        finish();
                    }
                }).setNegativeButton("取消", null).show();
    }

    private void initView() {
        ImageView img = (ImageView) findViewById(R.id.signinImg);
        findViewById(R.id.signinAgain).setOnClickListener(this);
        findViewById(R.id.signinSubmit).setOnClickListener(this);

        //转化为bitmap
        bitmap = BitmapFactory.decodeFile(jpgFile);
        img.setImageBitmap(createWaterMarkBitmap(bitmap));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signinAgain:
                finish();
                break;
            case R.id.signinSubmit:
                //提交成功信息
                Toast.makeText(this, "提交成功", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //添加水印
    private Bitmap createWaterMarkBitmap(Bitmap bitmap) {
        //设置bitmap旋转90度
        Matrix matrix = new Matrix();
        //后置摄像头旋转角度
        if (cameraPosition == 1)
            matrix.setRotate(90);
        else matrix.setRotate(-90);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        //画笔
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(30);

        //画布
        Canvas canvas = new Canvas(bitmap);
        String format = DateFormat.getDateTimeInstance().format(new Date());
        canvas.drawText(format, 20, 50, paint);
        canvas.drawText("username", 20, 100, paint);

        return bitmap;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //拦截返回键
        outHint();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放资源
        bitmap.recycle();
        bitmap = null;
    }
}
