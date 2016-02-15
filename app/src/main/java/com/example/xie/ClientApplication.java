package com.example.xie;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.examp.bean.User;
import com.im.sdk.core.IMClient;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.GINGERBREAD;

/**
 * Created by xie on 2016/1/31.
 */
public class ClientApplication extends Application {

    private static Context instance;

    public static User mUser;


    private RefWatcher mRefWatcher;
    public static Context instance(){
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
        IMClient.init(this);
    }
    private void enabledStrictMode() {
        if (SDK_INT >= GINGERBREAD) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder() //
                    .detectAll() //
                    .penaltyLog() //
                    .penaltyDeath() //
                    .build());
        }
    }

}
