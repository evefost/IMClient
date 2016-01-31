package com.im.sdk.core;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;


import com.example.xie.ClientApplication;
import com.im.sdk.protocal.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;


/**
 * Created by xie on 2016/1/31.
 * 专门处理所有消息的发送接收
 */
public class MessageHandler {

    public String TAG = getClass().getSimpleName();

    Map<Long, Message.Data.Builder> mQueue = new HashMap<Long, Message.Data.Builder>();
    public static long timeOut = 30 * 1000;
    private static MessageHandler instance = new MessageHandler();
    private  Context  context = ClientApplication.instance();

    private Handler timerHandler = new Handler();
    private boolean isStop = false;

    private MessageHandler() {

    }

    public static MessageHandler instance() {
        return instance;
    }


    private void loopMessage() {
        if(isStop){
            return;
        }
        timerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkTimeOutMessage();
                loopMessage();
            }
        }, 5 * 1000);
    }

    private void checkTimeOutMessage() {
        long currentTime = System.currentTimeMillis();
        for (Map.Entry<Long, Message.Data.Builder> entry : mQueue.entrySet()) {
            Long timeStrart = entry.getKey();
            long time = currentTime - timeStrart;
            if (time >= timeOut) {
                IMClient.instance().onSendFailure(entry.getValue());
                pop(entry.getKey());
            }
        }
    }

    /**缓存消息，发送成功或失败,超时，时移除*/
    public void push(Message.Data.Builder msg) {
        if(msg != null && msg.getCreateTime() !=0L ){
            mQueue.put(msg.getCreateTime(),msg);
        }
    }

    public Message.Data.Builder pop(Long key) {
        if(key != null){
            return mQueue.remove(key);
        }
        return null;
    }

    public void handSendMsg(ExecutorService executor, final Channel channel, final Message.Data.Builder msg) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                //必须设置发送时间
                msg.setCreateTime(System.currentTimeMillis());
                proccessSendMessage(msg);
                push(msg);
                if (IMClient.instance().isConnected()) {
                    try {
                        Log.e(TAG, "发送中...time:"+msg.getCreateTime());
                        ChannelFuture channelFuture = channel.writeAndFlush(msg);
                    } catch (Exception e) {
                        Log.e(TAG, "发送失败:" + e.toString());
                        pop(msg.getCreateTime());
                        IMClient.instance().onSendFailure(msg);
                    }
                } else {
                    Log.i(TAG, "服务器已经断开");
                    IMClient.instance().onSendFailure(msg);
                }
            }
        });
    }

    private  void proccessSendMessage(Message.Data.Builder data) {
        Log.i(TAG,"发送消息");
        switch (data.getCmd()) {
            case Message.Data.Cmd.LOGIN_VALUE:
                Log.i(TAG, "登录["+data.getAccount());
                break;
            case Message.Data.Cmd.HEARTBEAT_VALUE:
                Log.i(TAG, "心跳消息 time:"+data.getCreateTime());
                break;
            case Message.Data.Cmd.CHAT_MESSAGE_VALUE:
                Log.i(TAG, "聊天消息 ["+data.getContent());
                break;
        }
    }

    public void handReceiveMsg(Message.Data data) {
        Log.i(TAG,"处理收到消息");
        switch (data.getCmd()) {
            case Message.Data.Cmd.LOGIN_VALUE:
                if (TextUtils.isEmpty(data.getAccount())) {
                    Log.i(TAG, "服务端登录请求 msg[" + data.getContent() + "] ip[" + data.getIp() + "]port[" + data.getPort());
                } else {
                    Log.i(TAG, "登录成功 account[" + data.getAccount() + "]ip[" + data.getIp() + "]port[" + data.getPort());
                    HeartBeatManager.instance().startHeartBeat();
                }
                break;
            case Message.Data.Cmd.OTHER_LOGGIN_VALUE:
                Log.i(TAG, "帐号别处登录");
                // TODO: 2016/1/31
                break;
            case Message.Data.Cmd.HEARTBEAT_VALUE:
                Log.i(TAG, "服务端回应心跳消息:"+data.getCreateTime());
                //移除心跳消息
                pop(data.getCreateTime());
                break;
            case Message.Data.Cmd.CHAT_MESSAGE_VALUE:
                Log.i(TAG, "收到聊天消息");
                // TODO: 2016/1/31
                break;
            case Message.Data.Cmd.CHAT_MESSAGE_ECHO_VALUE:

                Message.Data.Builder pop = pop(data.getCreateTime());
                Log.i(TAG, "消息回应,发送成功 createTime:"+data.getCreateTime()+"=="+pop.getContent());
                IMClient.instance().onSendSucceed(pop);
                break;
        }
    }


}
