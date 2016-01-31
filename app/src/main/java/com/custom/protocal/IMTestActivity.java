package com.custom.protocal;

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


/**
 * Created by mis on 2016/1/28.
 */
public class IMTestActivity extends Activity implements ClientHandler.IMEventListener{

    public  String TAG = getClass().getSimpleName();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.im_test_layout);
        initViews();
        setListenter();
        tv_status.setText(IMClient.instance().isConnected() ? "is contected" : "is disconnected");
    }

    private EditText account;
    private TextView tv_status;
    private Button bt_login;
    private Button logout;
    private Button bt_send_message;

    private void initViews() {

        account = (EditText) findViewById(R.id.account);
        tv_status = (TextView) findViewById(R.id.tv_status);
        bt_login = (Button) findViewById(R.id.bt_login);
        logout = (Button) findViewById(R.id.logout);
        bt_send_message = (Button) findViewById(R.id.bt_send_message);

        IMClient.addEventListener(this);
        IMClient.instance().connect();
    }


    int sentcont = 0;
    public void setListenter(){
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
                msg.setCmd(Message.Data.Cmd.CHAT_MESSAGE_VALUE);

                //msg.setId("id"+sentcont);
                msg.setContent("第"+sentcont+"发送");
                IMClient.instance().sendMessage(msg);
            }
        });
    }

    @Override
    public void onReceiveMessage(Object message) {

        Message.Data data = (Message.Data) message;
        Log.i(TAG,"data cmd["+data.getCmd()+"]id["+data.getId()+"]username["+data.getContent());
        if(data.getCmd() == Message.Data.Cmd.LOGIN_VALUE&& TextUtils.isEmpty(data.getAccount())){
            //未登录，登录
            Log.i(TAG,"未登录，登录");
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
    public void onSendFailure(Object msg) {
        Log.i(TAG, "onSendFailure");
    }

    @Override
    public void onSendSucceed(Object msg) {
        Log.i(TAG, "onSendSucceed");
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
