package com.example.xie;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.common.ui.base.BaseFragment;
import com.example.xie.imclient.R;
import com.im.sdk.core.ClientHandler;
import com.im.sdk.core.IMClient;
import com.im.sdk.protocal.Message;

/**
 * Created by xie on 2016/2/25.
 */
public class HomeFragment extends BaseFragment implements ClientHandler.IMEventListener, View.OnClickListener {

    @Override
    public int getLayoutId() {
        return R.layout.home_fragment_layout;
    }

    public String TAG = getClass().getSimpleName();



    private TextView tv_status;
    private Button btn_connect;
    private Button btn_disconnect;
    private EditText account;

    private Button bt_login;
    private Button logout;
    private Button login_status;

    @Override
    public void findViews() {
        account = (EditText) findViewById(R.id.account);
        tv_status = (TextView) findViewById(R.id.tv_status);
        bt_login = (Button) findViewById(R.id.bt_login);
        logout = (Button) findViewById(R.id.logout);

        btn_connect = (Button) findViewById(R.id.btn_connect);
        btn_disconnect = (Button) findViewById(R.id.btn_disconnect);
        login_status = (Button) findViewById(R.id.login_status);
    }

    @Override
    public void init(Bundle savedInstanceState) {

        hideTopBar(true);
        account.setText(mApp.getUid());
        IMClient.addEventListener(this);
        tv_status.setText(IMClient.instance().isConnected() ? "server is contected" : "server is disconnected");

    }


    int sentcont = 0;

    @Override
    public void setListeners() {
        btn_connect.setOnClickListener(this);
        btn_disconnect.setOnClickListener(this);
        bt_login.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    public void onReceiveMessage(Message.Data msg) {

        Log.i(TAG, "data cmd[" + msg.getCmd() + "]id[" + msg.getId() + "]username[" + msg.getContent());
        if (msg.getCmd() == Message.Data.Cmd.LOGIN_VALUE && TextUtils.isEmpty(msg.getSender())) {
            //未登录，登录
            Log.i(TAG, "未登录，登录");
            login_status.setText("未登录");
            Message.Data.Builder accountInfo = Message.Data.newBuilder();
            accountInfo.setCmd(Message.Data.Cmd.LOGIN_VALUE);
            accountInfo.setSender(mApp.getUid());
            //IMClient.instance().sendMessage(accountInfo);
        }

    }

    @Override
    public void onConnecting() {
        Log.i(TAG, "onConnecting");
        tv_status.setText("连接中....");
    }

    @Override
    public void onConnected() {
        Log.i(TAG, "onConnected");
        tv_status.setText("on conected");

    }

    @Override
    public void onDisconnected(boolean isException) {
        tv_status.setText("on disconnected");
        Log.i(TAG, "onDisconnected");
    }


    @Override
    public void onSendFailure(Message.Data.Builder msg) {
        Log.i(TAG, "onSendFailure");
        if (msg.getCmd() == Message.Data.Cmd.LOGIN_VALUE) {
            Log.i(TAG, "登录失败");
        } else if (msg.getCmd() == Message.Data.Cmd.CHAT_MSG_VALUE) {

        }
    }

    @Override
    public void onSendSucceed(Message.Data.Builder msg) {
        Log.i(TAG, "onSendSucceed cmd [" + msg.getCmd());
        if (msg.getCmd() == Message.Data.Cmd.LOGIN_VALUE) {
            Log.i(TAG, "1".equals(msg.getContent()) ? "登录成功" : "登录失败:" + msg.getContent());
            if ("1".equals(msg.getContent())) {
                login_status.setText("已登录");
            }
        } else if (msg.getCmd() == Message.Data.Cmd.CHAT_MSG_ECHO_VALUE) {
            Log.i(TAG, "发送成功");

        }
    }

    @Override
    public void onConnectFailure(String msg) {
        Log.i(TAG, "onConnectFailure" + msg);
        tv_status.setText("连接失败....");

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        IMClient.removeEventListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_connect:
                IMClient.instance().connect();
                break;

            case R.id.btn_disconnect:
                IMClient.instance().disconnect();
                break;
            case R.id.bt_login:
                Message.Data.Builder accountInfo = Message.Data.newBuilder();
                accountInfo.setCmd(Message.Data.Cmd.LOGIN_VALUE);
                accountInfo.setSender(mApp.getUid());
                IMClient.instance().sendMessage(accountInfo);
                break;

            case R.id.logout:
                Message.Data.Builder data = Message.Data.newBuilder();
                data.setCmd(Message.Data.Cmd.LOGOUT_VALUE);
                IMClient.instance().sendMessage(data);
                break;
//            case R.id.bt_send_message:
//
//                Log.i(TAG, "发送");
//                sentcont++;
//                Message.Data.Builder msg = Message.Data.newBuilder();
//                msg.setCmd(Message.Data.Cmd.CHAT_MSG_VALUE);
//                //msg.setId("id"+sentcont);
//                msg.setContent("第" + sentcont + "发送");
//                IMClient.instance().sendMessage(msg);
//                Message.Data sdata = msg.build();
//                Log.i(TAG, "send data content:" + sdata.getContent());
//                break;
        }
    }


}
