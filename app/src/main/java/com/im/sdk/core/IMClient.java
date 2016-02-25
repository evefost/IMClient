/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.im.sdk.core;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;


import com.example.xie.ClientApplication;
import com.im.sdk.protocal.Message;
import com.im.sdk.protocal.ProtobufDecoder;
import com.im.sdk.protocal.ProtobufEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


/***
 * 处理所有的消息处事件
 */
public final class IMClient implements ClientHandler.IMEventListener {

    public String TAG = getClass().getSimpleName();

    public static final int STATU_DISCONNECT = 0;
    public static final int STATU_CONNECTING = 1;
    public static final int STATU_CONNECTED = 2;

    private int connect_status = STATU_DISCONNECT;

    /**
     * 服务端地址
     */
    static final String HOST = "192.168.60.91";
    /**
     * 服务端端口
     */
    static final int PORT = 53456;
    /**
     * 消息事件监听集
     */
    private static final List<ClientHandler.IMEventListener> mIMEventListener = new ArrayList<ClientHandler.IMEventListener>();
    private static IMClient mInstance;
    private MessageHandler mMessageHandler;
    private Context context = ClientApplication.instance();
    private Bootstrap bootstrap;
    private EventLoopGroup loopGroup;
    private Channel channel;

    private ClientHandler handler;
    private Handler mUIhander;
    private ExecutorService executor;

    private String account = "123456";

    private int reconectTimes = 3;

    private IMClient() {
        init();
    }
    public static void init(Context ctx) {
        if (mInstance == null) {
            mInstance = new IMClient();
        }
    }

    public boolean isConnecting(){
        return connect_status == STATU_CONNECTING;
    }

    public static IMClient instance() {
        return mInstance;
    }

    private void init() {

        Log.i(TAG, "初始化连接");
        executor = Executors.newCachedThreadPool();
        mUIhander = new Handler();
        handler = new ClientHandler(this);
        mMessageHandler = MessageHandler.instance();
        mMessageHandler.setExcutor(executor);
        loopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(loopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new ProtobufDecoder(Message.Data.getDefaultInstance()));
                        p.addLast(new ProtobufEncoder());
                        p.addLast(handler);
                    }
                });
    }

    public boolean reconnect() {

        if(reconectTimes>0){
            connect();
            return true;
        }
        return false;
    }

    public void connect() {

        executor.execute(new Runnable() {
            @Override
            public void run() {

                if (channel != null && channel.isActive()) {
                    Log.i(TAG, "已连接服务器");
                    connect_status = STATU_CONNECTED;
                    return;
                }
                Log.i(TAG, "启动连接服务器");
                ChannelFuture f = null;
                try {
                    connect_status = STATU_CONNECTING;
                    onConnecting();
                    f = bootstrap.connect(HOST, PORT).sync();
                    f.addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture channelFuture) throws Exception {
                            Log.w(TAG, "绑定服务结果:" + (channelFuture.isSuccess() ? "成功" : "失败:" + channelFuture.cause().toString()));
                        }
                    });

                    channel = f.channel();
                    // Wait until the connection is closed.
                    f.channel().closeFuture().sync();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i(TAG, "connect EX" + e.toString());
                    connect_status = STATU_DISCONNECT;
                    onConnectFailure(e.toString());

                } finally {
                    // Shut down the event loop to terminate all threads.
                    Log.i(TAG, " Shut down the event loop to terminate all threads.");
//                    loopGroup.shutdownGracefully();
                }
            }
        });


    }

    public void sendMessage(final Message.Data.Builder msg) {
        mMessageHandler.handSendMsg(channel, msg);
    }

    public void disconnect() {
        if (channel != null && channel.isActive()) {
            channel.close();
        }
        channel = null;
        connect_status = STATU_DISCONNECT;
    }

    public void destroy() {
        disconnect();
        mIMEventListener.clear();
    }

    public boolean isConnected() {
        if (channel != null && channel.isActive()) {
            connect_status = STATU_CONNECTED;
            return true;
        }
        return false;
    }

    @Override
    public void onReceiveMessage(final Message.Data message) {
        Log.i(TAG, " onReceiveMessage ");
        notifyListener(false,message, EVENT_RECEIVE_MESSAGE);
    }

    @Override
    public void onConnected() {
        HeartBeatManager.instance().startHeartBeat();
        reconectTimes = 3;
        connect_status = STATU_CONNECTED;
        Log.i(TAG, " onConnected ");
        notifyListener(false,null, EVENT_CONNECTED);
        mMessageHandler.onConnected();
    }

    @Override
    public void onDisconnected(boolean isException) {
        HeartBeatManager.instance().reset();
        Log.i(TAG, "onDisconnected ");
        connect_status = STATU_DISCONNECT;
        notifyListener(isException,null, EVENT_DISCONNECTED);
        if(isException){
            reconnect();
        }
    }

    @Override
    public void onSendFailure(Message.Data.Builder msg) {
        notifyListener(false,msg, EVENT_SEND_FAILURE);
    }

    @Override
    public void onSendSucceed(Message.Data.Builder msg) {
        notifyListener(false,msg, EVENT_SEND_SUCCESS);
    }

    @Override
    public void onConnectFailure(String msg) {
        reconectTimes--;
        notifyListener(false,msg, EVENT_CONNECT_FAILURE);
    }

    @Override
    public void onConnecting() {
        notifyListener(false,null, EVENT_CONNECT_ING);
    }


    /**
     * 通知所有事件监听器
     *
     * @param message
     * @param EVENT
     */
    private void notifyListener(final boolean isException,final Object message, final int EVENT) {
        mUIhander.post(new Runnable() {
            @Override
            public void run() {
                for (ClientHandler.IMEventListener listener : mIMEventListener) {
                    if (EVENT == EVENT_CONNECTED) {
                        Log.i(TAG, "连接成功能");
                        listener.onConnected();
                    } else if (EVENT == EVENT_DISCONNECTED) {
                        Log.i(TAG, "服务器已断开");
                        listener.onDisconnected(isException);
                    } else if (EVENT == EVENT_RECEIVE_MESSAGE) {
                        Log.i(TAG, "收到消息，处理各类消息");
                        mMessageHandler.handReceiveMsg((Message.Data) message, listener);
                    } else if (EVENT == EVENT_SEND_FAILURE) {
                        Log.i(TAG, "发送失败");
                        listener.onSendFailure((Message.Data.Builder) message);
                    } else if (EVENT == EVENT_SEND_SUCCESS) {
                        Log.i(TAG, "发送成功");
                        listener.onSendSucceed((Message.Data.Builder) message);
                    } else if (EVENT == EVENT_CONNECT_FAILURE) {
                        Log.i(TAG, "连接失败" + message.toString());
                        listener.onConnectFailure((String) message);
                    }else if (EVENT == EVENT_CONNECT_ING) {
                        Log.i(TAG, "连接中...");
                        listener.onConnecting();
                    }
                }
            }
        });
    }

    public static void addEventListener(ClientHandler.IMEventListener listener) {
        mIMEventListener.add(listener);
    }

    public static void removeEventListener(ClientHandler.IMEventListener listener) {
        mIMEventListener.remove(listener);
    }

    public void clearEventListener() {
        mIMEventListener.clear();
    }
}
