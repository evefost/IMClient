package com.im.sdk.core;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;


import com.example.xie.ClientApplication;
import com.im.sdk.protocal.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;


/**
 * Created by xie on 2016/1/31.
 * 处理所有消息的发送接收
 */
public class MessageHandler {

    public String TAG = getClass().getSimpleName();

    ConcurrentHashMap<Long, Message.Data.Builder> mQueue = new ConcurrentHashMap<Long, Message.Data.Builder>();
    public static long timeOut = 30 * 1000;
    private static MessageHandler instance = new MessageHandler();
    private Context context = ClientApplication.instance();

    private Handler timerHandler = new Handler();
    private boolean looping = false;
    private boolean isStopLoop = false;
    private MessageHandler() {
    }

    public static MessageHandler instance() {
        return instance;
    }


    private void loopMessage() {
        Log.i(TAG,"loopMessage mQueue size["+mQueue.size());
        if (isStopLoop ||mQueue.size()==0 ) {
            Log.i(TAG,"stopLoop.......");
            looping = false;
            return;
        }
        looping = true;
        timerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkTimeOutMessage();
                loopMessage();
            }
        }, 5 * 1000);
    }

    private void checkTimeOutMessage() {
        Log.i(TAG,"checkTimeOutMessage size[ "+mQueue.size()+" ]");
        long currentTime = System.currentTimeMillis();
        for (Map.Entry<Long, Message.Data.Builder> entry : mQueue.entrySet()) {
            Long timeStrart = entry.getKey();
            long time = currentTime - timeStrart;
            if (time >= timeOut) {
                IMClient.instance().onSendFailure(entry.getValue());
                Log.e(TAG,"消息发送超时 ["+entry.getKey());
                pop(entry.getKey());
            }
        }
    }

    /**
     * 缓存消息，发送成功或失败,超时，时移除
     */
    public void push(Message.Data.Builder msg) {
        if (msg != null && msg.getCreateTime() != 0L) {
            mQueue.put(msg.getCreateTime(), msg);
        }
    }

    public Message.Data.Builder pop(Long key) {
        if (key != null) {
            return mQueue.remove(key);
        }
        return null;
    }

    public void handSendMsg(ExecutorService executor, final Channel channel, final Message.Data.Builder msg) {
        //必须设置发送时间
        msg.setCreateTime(System.currentTimeMillis());
        proccessSendMessage(msg);
        push(msg);
        executor.execute(new Runnable() {
            @Override
            public void run() {

                if (IMClient.instance().isConnected()) {
                    try {
                        Log.e(TAG, "发送中...time:" + msg.getCreateTime());
                        ChannelFuture channelFuture = channel.writeAndFlush(msg);
                        if(!isStopLoop && !looping ){
                            Log.i(TAG," start checkTimeOutMessage");
                            loopMessage();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "发送失败:" + e.toString());
                        pop(msg.getCreateTime());
                        if (msg.getCmd() != Message.Data.Cmd.HEARTBEAT_VALUE) {
                            //心跳消息不用通知
                            IMClient.instance().onSendFailure(msg);
                        }

                    }
                } else {
                    Log.i(TAG, "服务器已经断开,重连");
                    boolean stopReconnect = IMClient.instance().reconnect();
                    if (stopReconnect) {
                        if (msg.getCmd() != Message.Data.Cmd.HEARTBEAT_VALUE) {
                            //心跳消息不用通知
                            IMClient.instance().onSendFailure(msg);
                        }
                    }
                }
            }
        });
    }

    private void proccessSendMessage(Message.Data.Builder data) {
        Log.i(TAG, "处理发送消息===========>>==========>>");
        switch (data.getCmd()) {
            case Message.Data.Cmd.LOGIN_VALUE:
                Log.i(TAG, "登录[" + data.getAccount());
                break;
            case Message.Data.Cmd.HEARTBEAT_VALUE:
                Log.i("HeartBeatManager", "心跳消息 time:" + data.getCreateTime());
                break;
            case Message.Data.Cmd.CHAT_MESSAGE_VALUE:
                Log.i(TAG, "聊天消息 [" + data.getContent());
                break;
        }
    }

    /**所有消息的接收在这处理*/
    public void handReceiveMsg(Message.Data data, ClientHandler.IMEventListener listener) {
        Log.i(TAG, "处理收到消息<<===========<<===========");
        switch (data.getCmd()) {
            case Message.Data.Cmd.LOGIN_VALUE:
                if (TextUtils.isEmpty(data.getAccount())) {
                    Log.i(TAG, "服务端登录请求 msg[" + data.getContent() );
                    listener.onReceiveMessage(data);
                } else {
                    Log.i(TAG, "登录结果:"+data.getLoginSuccess());
                    Log.i(TAG, data.getContent() + " time" + data.getCreateTime());
                    HeartBeatManager.instance().startHeartBeat();
                    //移除发送消息
                    Message.Data.Builder pop = pop(data.getCreateTime());
                    pop.setLoginSuccess(data.getLoginSuccess());
                    pop.setContent(data.getContent());
                    IMClient.instance().onSendSucceed(pop);
                }
                break;
            case Message.Data.Cmd.OTHER_LOGGIN_VALUE:
                Log.i(TAG, "帐号别处登录");
                listener.onReceiveMessage(data);
                break;
            case Message.Data.Cmd.HEARTBEAT_VALUE:
                Log.i("HeartBeatManager", "服务端回应的心跳消息:" + data.getCreateTime());
                //移除心跳消息
                pop(data.getCreateTime());
                break;
            case Message.Data.Cmd.CHAT_MESSAGE_VALUE:
                Log.i(TAG, "收到聊天消息");
                listener.onReceiveMessage(data);
                break;
            case Message.Data.Cmd.CHAT_MESSAGE_ECHO_VALUE:
                Log.i(TAG, "<<<<<<<<<<<<<<<<<<<<<消息回应,发送成功");
                Message.Data.Builder pop = pop(data.getCreateTime());
                Log.i(TAG, "createTime:" + data.getCreateTime() + "==pop:" + pop.getContent());
                //移除发送消息
                IMClient.instance().onSendSucceed(pop);
                break;
        }
    }


}
