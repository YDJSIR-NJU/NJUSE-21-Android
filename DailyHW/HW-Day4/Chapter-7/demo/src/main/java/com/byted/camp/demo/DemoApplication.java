package com.byted.camp.demo;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * @author wangzhongshan
 * @date 2021-07-16.
 */
class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
