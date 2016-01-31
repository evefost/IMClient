package com.im.sdk.core;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.util.Log;

import com.example.xie.ClientApplication;
import com.im.sdk.protocal.Message;


public class HeartBeatManager {
    // 心跳检测4分钟检测一次，并且发送心跳包
    // 服务端自身存在通道检测，5分钟没有数据会主动断开通道

    public static String TAG="IMHeartBeatManager";
    private static HeartBeatManager inst;
    private   Context context = ClientApplication.instance();

    private HeartBeatManager(){};
    public static HeartBeatManager instance() {
        if(inst == null){
            inst = new HeartBeatManager();
        }
        return inst;
    }
    private final int HEARTBEAT_INTERVAL = 10 * 1000;
    private final String ACTION_SENDING_HEARTBEAT = "com.custom.protocal.im.heart.break";
    private PendingIntent pendingIntent;


    // 登陆成功之后
    public void startHeartBeat() {
        Log.w(TAG,"定时启动心跳 ,周期["+HEARTBEAT_INTERVAL);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_SENDING_HEARTBEAT);
        context.registerReceiver(imReceiver, intentFilter);
        //获取AlarmManager系统服务
        if (pendingIntent == null) {
            Log.w(TAG,"heartbeat#fill in pendingintent");
            Intent intent = new Intent(ACTION_SENDING_HEARTBEAT);
            pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            if (pendingIntent == null) {
                Log.w(TAG,"heartbeat#scheduleHeartbeat#pi is null");
                return;
            }
        }
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + HEARTBEAT_INTERVAL, HEARTBEAT_INTERVAL, pendingIntent);
        Log.i(TAG,"已启动心跳");
    }

    public void reset() {
        Log.d(TAG,"heartbeat#reset begin");
        try {
            context.unregisterReceiver(imReceiver);
            cancelHeartbeatTimer();
            Log.d(TAG,"heartbeat#reset stop");
        } catch (Exception e) {
            Log.e(TAG,"heartbeat#reset error:%s", e.getCause());
        }
    }


    public void cancelHeartbeatTimer() {
        Log.w(TAG,"cancelHeartbeatTimer");
        if (pendingIntent == null) {
            Log.w(TAG,"heartbeat#pi is null");
            return;
        }
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pendingIntent);
    }

    /**
     * --------------------boradcast-广播相关-----------------------------
     */
    private BroadcastReceiver imReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(ACTION_SENDING_HEARTBEAT)) {
                sendHeartBeatPacket();
            }
        }
    };

    public void sendHeartBeatPacket() {
        Log.d(TAG,"发送心跳包");
//        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "teamtalk_heartBeat_wakelock");
//        wl.acquire();
        try {
            Message.Data.Builder data = Message.Data.newBuilder();
            data.setCmd(Message.Data.Cmd.HEARTBEAT_VALUE);
            IMClient.instance().sendMessage(data);
        } finally {
//            wl.release();
        }
    }
}