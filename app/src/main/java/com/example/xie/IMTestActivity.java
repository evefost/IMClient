package com.example.xie;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.xie.imclient.R;
import com.im.sdk.core.ClientHandler;
import com.im.sdk.core.IMClient;
import com.im.sdk.protocal.Message;

import common.BaseActivity;


/**
 * Created by mis on 2016/1/28.
 */
public class IMTestActivity extends BaseActivity implements ClientHandler.IMEventListener{

    public  String TAG = getClass().getSimpleName();

    @Override
    public int getLayoutId() {
        return R.layout.im_test_layout;
    }

    private EditText account;
    private TextView tv_status;
    private Button bt_login;
    private Button logout;
    private Button bt_send_message;

    @Override
    public void findViews() {
        account = (EditText) findViewById(R.id.account);
        tv_status = (TextView) findViewById(R.id.tv_status);
        bt_login = (Button) findViewById(R.id.bt_login);
        logout = (Button) findViewById(R.id.logout);
        bt_send_message = (Button) findViewById(R.id.bt_send_message);


    }

    @Override
    public void init(Bundle savedInstanceState) {

        IMClient.addEventListener(this);
        IMClient.instance().connect();
        tv_status.setText(IMClient.instance().isConnected() ? "is contected" : "is disconnected");

    }





    int sentcont = 0;
    @Override
    public void setListeners(){
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IMClient.instance().connect();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IMClient.instance().disconnect();
            }
        });

        bt_send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"发送");
                sentcont++;
                Message.Data.Builder msg = Message.Data.newBuilder();
                msg.setCmd(Message.Data.Cmd.CHAT_MSG_VALUE);

                //msg.setId("id"+sentcont);
                msg.setContent("第"+sentcont+"发送");
                IMClient.instance().sendMessage(msg);
            }
        });
    }

    @Override
    public void onReceiveMessage(Message.Data msg) {

        Log.i(TAG,"data cmd["+msg.getCmd()+"]id["+msg.getId()+"]username["+msg.getContent());
        if(msg.getCmd() == Message.Data.Cmd.LOGIN_VALUE&& TextUtils.isEmpty(msg.getAccount())){
            //未登录，登录
            Log.i(TAG,"未登录，登录");
            Message.Data.Builder accountInfo = Message.Data.newBuilder();
            accountInfo.setCmd(Message.Data.Cmd.LOGIN_VALUE);
            accountInfo.setAccount("xieyang123");
            IMClient.instance().sendMessage(accountInfo);
        }
        if(msg.getCmd() == Message.Data.Cmd.LOGIN_VALUE&& !TextUtils.isEmpty(msg.getAccount())){
            Log.i(TAG,"登录成功");
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
    public void onSendFailure( Message.Data.Builder msg) {
        Log.i(TAG, "onSendFailure");
        if(msg.getCmd() == Message.Data.Cmd.LOGIN_VALUE){

            Log.i(TAG,"登录失败");
        }
    }

    @Override
    public void onSendSucceed( Message.Data.Builder msg) {
        Log.i(TAG, "onSendSucceed");
        if(msg.getCmd() == Message.Data.Cmd.LOGIN_VALUE){
            Log.i(TAG,msg.getLoginSuccess()?"登录成功":"登录失败:"+msg.getContent());
            if(msg.getLoginSuccess()){
//                ChatActivity.lauchActivity(mActivity,"4567");
            }
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


}
