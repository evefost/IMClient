package com.example.xie;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.examp.bean.User;
import com.im.sdk.core.IMClient;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.zhy.autolayout.config.AutoLayoutConifg;

import java.util.UUID;

/**
 * 2016/1/31.
 */
public class ClientApplication extends Application {

    private static Context instance;
    public User mUser;
    private String TAG = getClass().getSimpleName();
    private RefWatcher mRefWatcher;

    public static Context instance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mRefWatcher = LeakCanary.install(this);
        instance = this;
        init();
    }

    private void init() {

        AutoLayoutConifg.getInstance().useDeviceSize().init(this);
        mUser = new User();
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        Log.i(TAG, "uuid:" + uuid);
        mUser.setUid(uuid);

        IMClient.init(this);
        IMClient.instance().connect();


    }

    public String getDeviceId(){
        TelephonyManager manager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        return manager.getDeviceId();
    }

    public User getmUser() {
        return mUser;
    }

    public void setmUser(User mUser) {
        this.mUser = mUser;
    }

    public String getUid() {
        if (mUser == null) {
            return "";
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
