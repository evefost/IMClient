package com.example.xie;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.common.ui.base.BaseActivity;
import com.example.xie.imclient.R;
import com.im.sdk.core.ClientHandler;
import com.im.sdk.core.IMClient;
import com.im.sdk.protocal.Message;
import com.im.sdk.protocal.Message.Data.Cmd;


/**
 * Created by mis on 2016/1/28.
 */
public class IMTestActivity extends BaseActivity implements ClientHandler.IMEventListener,View.OnClickListener {

    public String TAG = getClass().getSimpleName();

    @Override
    public int getLayoutId() {
        return R.layout.im_test_layout;
    }

    private TextView tv_status;
    private Button btn_connect;
    private Button btn_disconnect;
    private EditText account;
    private Button bt_login;
    private Button logout;
    private Button bt_send_message;
    private Button login_status;
    private Button  btn_chat;
    private ProgressBar prgs_send;
    @Override
    public void findViews() {
        account = (EditText) findViewById(R.id.account);
        tv_status = (TextView) findViewById(R.id.tv_status);
        bt_login = (Button) findViewById(R.id.bt_login);
        logout = (Button) findViewById(R.id.logout);
        bt_send_message = (Button) findViewById(R.id.bt_send_message);

        btn_connect = (Button) findViewById(R.id.btn_connect);
        btn_disconnect = (Button) findViewById(R.id.btn_disconnect);
        login_status = (Button) findViewById(R.id.login_status);
        btn_chat = (Button) findViewById(R.id.btn_chat);
        prgs_send = (ProgressBar) findViewById(R.id.prgs_send);

    }

    @Override
    public void init(Bundle savedInstanceState) {

        hideTopBar(true);
        prgs_send.setVisibility(View.INVISIBLE);
        IMClient.addEventListener(this);

        tv_status.setText(IMClient.instance().isConnected() ? "is contected" : "is disconnected");

    }


    int sentcont = 0;

    @Override
    public void setListeners() {
        btn_connect.setOnClickListener(this);
        btn_disconnect.setOnClickListener(this);
        bt_login.setOnClickListener(this);
        logout.setOnClickListener(this);
        btn_chat.setOnClickListener(this);
        bt_send_message.setOnClickListener(this);
    }

    @Override
    public void onReceiveMessage(Message.Data msg) {

        Log.i(TAG, "data cmd[" + msg.getCmd() + "]id[" + msg.getId() + "]username[" + msg.getContent());
        if (msg.getCmd() == Message.Data.Cmd.LOGIN_VALUE && TextUtils.isEmpty(msg.getAccount())) {
            //未登录，登录
            Log.i(TAG, "未登录，登录");
            login_status.setText("未登录");
            Message.Data.Builder accountInfo = Message.Data.newBuilder();
            accountInfo.setCmd(Message.Data.Cmd.LOGIN_VALUE);
            accountInfo.setAccount("xieyang123");
            IMClient.instance().sendMessage(accountInfo);
        }

    }

    @Override
    public void onConnected() {
        Log.i(TAG, "onConnected");
        tv_status.setText("on conected");

    }

    @Override
    public void onDisconnected() {
        tv_status.setText("on disconnected");
        Log.i(TAG, "onDisconnected");
    }

    @Override
    public void onSendFailure(Message.Data.Builder msg) {
        Log.i(TAG, "onSendFailure");
        if (msg.getCmd() == Message.Data.Cmd.LOGIN_VALUE) {

            Log.i(TAG, "登录失败");
        }
    }

    @Override
    public void onSendSucceed(Message.Data.Builder msg) {
        Log.i(TAG, "onSendSucceed cmd ["+msg.getCmd());
        if (msg.getCmd() == Cmd.LOGIN_VALUE) {
            Log.i(TAG, msg.getLoginSuccess() ? "登录成功" : "登录失败:" + msg.getContent());
            if (msg.getLoginSuccess()) {
                login_status.setText("已登录");
            }
        }else if(msg.getCmd() == Cmd.CHAT_MSG_ECHO_VALUE){
            Log.i(TAG, "发送成功");
            prgs_send.setVisibility(View.INVISIBLE);
            bt_send_message.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onConnectFailure(String msg) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IMClient.removeEventListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_connect:
                IMClient.instance().connect();
                break;

            case R.id.btn_disconnect:
                IMClient.instance().disconnect();
                break;
            case R.id.bt_login:
                Message.Data.Builder accountInfo = Message.Data.newBuilder();
                accountInfo.setCmd(Message.Data.Cmd.LOGIN_VALUE);
                accountInfo.setAccount("xieyang123");
                IMClient.instance().sendMessage(accountInfo);
                break;

            case R.id.logout:
                Message.Data.Builder data = Message.Data.newBuilder();
                data.setCmd(Message.Data.Cmd.LOGOUT_VALUE);
                IMClient.instance().sendMessage(data);
                break;
            case R.id.bt_send_message:
                prgs_send.setVisibility(View.VISIBLE);
                bt_send_message.setVisibility(View.INVISIBLE);
                Log.i(TAG, "发送");
                sentcont++;
                Message.Data.Builder msg = Message.Data.newBuilder();
                msg.setCmd(Message.Data.Cmd.CHAT_MSG_VALUE);
                //msg.setId("id"+sentcont);
                msg.setContent("第" + sentcont + "发送");
                IMClient.instance().sendMessage(msg);
                break;
            case R.id.btn_chat:
               ChatActivity.lauchActivity(this, ChatActivity.class);
//                startActivity(new Intent(this, DemoActivity.class));
                break;
        }
    }
}
