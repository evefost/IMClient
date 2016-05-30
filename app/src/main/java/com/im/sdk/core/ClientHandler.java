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

import com.xy.util.Log;

import com.im.sdk.protocal.Message;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;


@ChannelHandler.Sharable
public class ClientHandler extends ChannelHandlerAdapter {


    public ClientHandler(IMHandlerListener listener) {
        this.mListener = listener;
    }

    private IMHandlerListener mListener;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {

        Log.i("channelActive 已连上服务器"+ctx.channel().isActive());
        if (mListener != null) {
            mListener.onConnected();
        }

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Log.i("channelRead 收到消息");
        if (mListener != null) {
            mListener.onReceiveMessage((Message.Data) msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Log.i("EchoClientHandler", "exceptionCaught 异常关闭");
        cause.printStackTrace();
        ctx.close();
        if (mListener != null) {
            mListener.onDisconnected(true);
        }
    }

    public void channelInactive(ChannelHandlerContext ctx) {
        Log.i("EchoClientHandler", "channelInactive 服务器断开");
        ctx.close();
        if (mListener != null) {
            mListener.onDisconnected(false);
        }
    }


    /**
     * 所有事件监听
     */
    public interface IMEventListener extends IMHandlerListener {

        public static final int EVENT_RECEIVE_MESSAGE = 0;
        public static final int EVENT_CONNECTED = 1;
        public static final int EVENT_DISCONNECTED = 2;
        public static final int EVENT_SEND_FAILURE = 3;
        public static final int EVENT_SEND_SUCCESS = 4;
        public static final int EVENT_CONNECT_FAILURE = 5;
        public static final int EVENT_CONNECT_ING= 6;

        public void onSendFailure(Message.Data.Builder msg);

        public void onSendSucceed(Message.Data.Builder msg);

        public void onConnectFailure(String msg);

        public void onConnecting();

    }

    public interface IMHandlerListener {

        public void onReceiveMessage(Message.Data msg);

        public void onConnected();

        public void onDisconnected(boolean isException);

    }

}
