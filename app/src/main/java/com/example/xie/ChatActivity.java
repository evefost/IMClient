package com.example.xie;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.example.xie.imclient.R;
import com.im.sdk.core.ClientHandler;
import com.im.sdk.protocal.Message;

/**
 * Created by xie on 2016/2/1.
 */
public class ChatActivity extends BaseActivity implements ClientHandler.IMEventListener{


    @Override
    public int getLayoutId() {
        return R.layout.chat_layout;
    }

    private RecyclerView rcView;
    @Override
    public void findViews() {

        rcView = (RecyclerView) findViewById(R.id.rcView);
    }

    @Override
    public void init(Bundle savedInstanceState) {

    }


    @Override
    public void onSendFailure( Message.Data.Builder  msg) {

    }

    @Override
    public void onSendSucceed( Message.Data.Builder  msg) {

    }

    @Override
    public void onConnectFailure(String msg) {

    }

    @Override
    public void onReceiveMessage(Message.Data  msg) {

    }

    @Override
    public void onConnected() {

    }

    @Override
    public void onDisconnected() {

    }


//    private class RcAdater extends  RecyclerView.Adapter{
//
//    }

}
