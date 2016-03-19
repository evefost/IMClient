package com.example.xie;

import android.text.TextUtils;

import com.example.xie.imclient.BuildConfig;

import java.net.URLEncoder;

public class Constants {

    private static final int ENVIRONMENT_F = 0;  // 生产模式
    private static final int ENVIRONMENT_T = 1;  // 测试模式
    private static final int ENVIRONMENT_D = 2;  // 开发模式

    public static boolean isF() {
        return BuildConfig.serviceEnvironment == ENVIRONMENT_F;
    }

    public static boolean isT() {
        return BuildConfig.serviceEnvironment == ENVIRONMENT_T;
    }

    public static boolean isD() {
        return BuildConfig.serviceEnvironment == ENVIRONMENT_D;
    }

    public static String HOST =
            isT() ? "http://192.168.1.2" : isD() ? "http://192.168.1.2" : "https://192.168.1.2";
    public static String SOCKET_HOST =
            isT() ? "192.168.1.2" : isD() ? "192.168.1.2" : "192.168.1.2";
   public  static final int SOCKET_PORT = 53456;

}
