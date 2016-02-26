package com.example.xie;

import android.os.Bundle;

import com.common.ui.base.BaseFragment;
import com.im.sdk.core.ClientHandler;
import com.im.sdk.protocal.Message;

/**
 * Created by xie on 2016/2/25.
 */
public class HomeFragment extends BaseFragment implements ClientHandler.IMEventListener{

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void findViews() {

    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void onReceiveMessage(Message.Data msg) {

    }

    @Override
    public void onConnected() {

    }

    @Override
    public void onDisconnected(boolean isException) {

    }

    @Override
    public void onSendFailure(Message.Data.Builder msg) {

    }

    @Override
    public void onSendSucceed(Message.Data.Builder msg) {

    }

    @Override
    public void onConnectFailure(String msg) {

    }

    @Override
    public void onConnecting() {

    }
}
