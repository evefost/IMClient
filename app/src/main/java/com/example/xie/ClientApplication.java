package com.example.xie;

import android.app.Application;
import android.content.Context;

import com.im.sdk.core.IMClient;

/**
 * Created by xie on 2016/1/31.
 */
public class ClientApplication extends Application {

    private static Context instance;

    public static Context instance(){
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();
    }

    private void init() {
        IMClient.init(this);
    }

}
