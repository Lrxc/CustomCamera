package com.bxlt.customcamera.activity;

import android.app.Application;
import android.content.Context;

/**
 * Created by Lrxc on 2018/1/28.
 */

public class BaseApp extends Application {
    public Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public Context getContext() {
        return mContext;
    }
}
