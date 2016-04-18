package com.im.sdk.core;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.example.xie.ClientApplication;
import com.im.sdk.protocal.Message;
import com.im.sdk.protocal.Message.Data.Cmd;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import io.netty.channel.Channel;


/**
 * Created by xie on 2016/1/31.
 * 处理所有消息的发送接收
 */
public class MessageHandler {

    public static long timeOut = 30 * 1000;
    private static MessageHandler instance = new MessageHandler();
    public String TAG = getClass().getSimpleName();
    ConcurrentHashMap<Long, Message.Data.Builder> mQueue = new ConcurrentHashMap<>();
    private Context context = ClientApplication.instance();
    private Handler timerHandler = new Handler();
    private ExecutorService mExecutor;
    private Channel mChannel;
    private boolean looping = false;
    private boolean isStopLoop = false;

    private MessageHandler() {
    }

    public static MessageHandler instance() {
        return instance;
    }

    public void setExcutor(ExecutorService executor) {
        this.mExecutor = executor;
    }

    private void loopMessage() {
        Log.i(TAG, "loopMessage mQueue size[" + mQueue.size());
        if (isStopLoop || mQueue.size() == 0) {
            Log.i(TAG, "stopLoop.......");
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
        Log.i(TAG, "checkTimeOutMessage size[ " + mQueue.size() + " ]");
        long currentTime = System.currentTimeMillis();
        for (Map.Entry<Long, Message.Data.Builder> entry : mQueue.entrySet()) {
            Long timeStrart = entry.getKey();
            long time = currentTime - timeStrart;
            if (time >= timeOut) {
                IMClient.instance().onSendFailure(entry.getValue());
                Log.e(TAG, "messaage send timeout [" + entry.getKey());
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


    public void handSendMsg(final Message.Data.Builder msg) {
        //必须设置发送时间
        if (0 == msg.getCreateTime()) {
            msg.setCreateTime(System.currentTimeMillis());
        }
        proccessSendMessage(msg);
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                if (IMClient.instance().isConnected()) {
                    try {
                        Log.e(TAG, "message sending ...:" + msg);
                        mChannel.writeAndFlush(msg);
                        if (!isStopLoop && !looping) {
                            Log.i(TAG, "start to checkTimeOut Messages");
                            loopMessage();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "message send failue :" + e.toString());
                        pop(msg.getCreateTime());
                        if (msg.getCmd() != Cmd.HEARTBEAT_VALUE) {
                            //心跳消息不用通知
                            IMClient.instance().onSendFailure(msg);
                        }
                    }
                } else {
                    Log.i(TAG, "message sever is disconnect ,reconnect");
                    boolean stopReconnect = IMClient.instance().reconnect();
                    if (stopReconnect) {
                        if (msg.getCmd() != Cmd.HEARTBEAT_VALUE) {
                            //心跳消息不用通知
                            IMClient.instance().onSendFailure(msg);
                        }
                    }
                }
            }
        });
    }

    /**
     * 连接成功
     */
    public void onConnected(Channel channel) {
        this.mChannel = channel;
        //绑定设备
        String deviceid = ((ClientApplication) ClientApplication.instance()).getDeviceId();
        Message.Data.Builder data = Message.Data.newBuilder();
        data.setCmd(Cmd.BIND_DEVICE_VALUE);
        data.setContent(deviceid);
        data.setCreateTime(System.currentTimeMillis());
        handSendMsg(data);
        ConcurrentHashMap<Long, Message.Data.Builder> mQueue = this.mQueue;
        Set<Map.Entry<Long, Message.Data.Builder>> entries = mQueue.entrySet();
        for (Map.Entry<Long, Message.Data.Builder> entry : entries) {
            handSendMsg(entry.getValue());
        }
    }

    private void proccessSendMessage(Message.Data.Builder data) {
        Log.i(TAG, "proccess Send Message=====>>=====>>cmd[" + data.getCmd());
        push(data);
        switch (data.getCmd()) {
            case Cmd.LOGIN_VALUE:
                Log.i(TAG, "is login message account[" + data.getSender());
                break;
            case Cmd.HEARTBEAT_VALUE:
                Log.i(TAG, "is hearbreak mesage  time[" + data.getCreateTime());
                break;
            case Cmd.CHAT_MSG_VALUE:
                Log.i(TAG, "is nomal chat message   [" + data.getContent());
                break;
            case Cmd.BIND_DEVICE_VALUE:
                Log.i(TAG, "is bind device message [" + data.getContent());
                break;
        }
    }

    /**
     * 所有消息的接收在这处理
     */
    public void proccessReceiveMsg(Message.Data data, ClientHandler.IMEventListener listener) {
        Log.i(TAG, "处理收到消息<<======<<======cmd[" + data.getCmd());
        switch (data.getCmd()) {
            case Cmd.LOGIN_VALUE:
                if (TextUtils.isEmpty(data.getSender())) {
                    Log.i(TAG, "服务端登录请求    msg[" + data.getContent());
                    listener.onReceiveMessage(data);
                } else {
                    Log.i(TAG, "登录结果 LoginSuccess[" + data.getContent());
                    //移除发送消息
                    Message.Data.Builder pop = pop(data.getCreateTime());
                    if (pop != null) {
                        pop.setContent(data.getContent());
                        IMClient.instance().onSendSucceed(pop);
                    }
                }
                break;
            case Cmd.OTHER_LOGGIN_VALUE:
                Log.i(TAG, "帐号别处登录     account[" + data.getSender());
                listener.onReceiveMessage(data);
                break;
            case Cmd.HEARTBEAT_VALUE:
                Log.i(TAG, "心跳回应                [" + data.getCreateTime());
                //移除心跳消息
                pop(data.getCreateTime());
                break;
            case Cmd.CHAT_MSG_VALUE:
                Log.i(TAG, "收到聊天消息");
                listener.onReceiveMessage(data);
                break;
            case Message.Data.Cmd.CHAT_MSG_ECHO_VALUE:
                Log.i(TAG, "消息回应,发送成功   time[" + data.getCreateTime());
                Message.Data.Builder pop = pop(data.getCreateTime());
                if (pop != null) {
                    Log.i(TAG, "createTime:" + data.getCreateTime() + "==pop:" + pop.getContent());
                    pop.setCmd(Cmd.CHAT_MSG_ECHO_VALUE);
                    //移除发送消息
                    IMClient.instance().onSendSucceed(pop);
                }
                break;
            case Cmd.MINE_FRIENDS_VALUE:
                Log.i(TAG, "消息回应,好友列表" + data.getContent());
                Message.Data.Builder pop2 = pop(data.getCreateTime());
                listener.onReceiveMessage(data);
                break;
        }
    }


}
