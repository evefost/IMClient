package com.example.xie;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.examp.bean.User;
import com.im.sdk.core.IMClient;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.zhy.autolayout.config.AutoLayoutConifg;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.GINGERBREAD;

/**
 * Created by xie on 2016/1/31.
 */
public class ClientApplication extends Application {

    private static Context instance;

    public User mUser;


    private RefWatcher mRefWatcher;

    public static Context instance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // enabledStrictMode();
        mRefWatcher = LeakCanary.install(this);
        instance = this;

        init();
    }

    private void init() {

        AutoLayoutConifg.getInstance().useDeviceSize().init(this);
        IMClient.init(this);
        IMClient.instance().connect();
    }

    public User getmUser() {
        return mUser;
    }

    public void setmUser(User mUser) {
        this.mUser = mUser;
    }

    public String getUid() {
        if (mUser == null) {
            return null;
        } else {
            return mUser.getUid();
        }
    }

    public boolean isLogin() {
        if (mUser != null) {
            return true;
        } else {
            return false;
        }
    }
}
