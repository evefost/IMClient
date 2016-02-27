package com.example.xie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.common.ui.base.BaseActivity;
import com.examp.bean.LocalMessage;
import com.example.xie.imclient.R;
import com.im.sdk.core.ClientHandler;
import com.im.sdk.core.IMClient;
import com.im.sdk.protocal.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by xie on 2016/2/1.
 */
public class ChatActivity extends BaseActivity implements ClientHandler.IMEventListener {


    private  String TAG = getClass().getSimpleName();

    public static void lauchActivity(Activity activity, String uid) {
        Intent intent = new Intent(activity.getApplicationContext(), ChatActivity.class);
        intent.putExtra("uid",uid);
        activity.startActivity(intent);
    }

    String content ="这是测试内容这是测试容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测试内容这是测试内容容这这是测试内容这是测试内容容这这是测试内容这是测试内容容这是测试内容这是测试内容容这是测试内容这是测试内容容这是测试内容这是测试内容容这是测试内容这是测试内容容这是测试内容这是测试内测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测试内容这是测试内容容这这是测试内容这是测试内容容这这是测试内容这是测试内容容这是测试内容这是测试内容容这是测试内容这是测试内容容这是测试内容这是测试内容容这是测试内容这是测试内容容这是测试内容这是测试内测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测试内容这是测试内容容这这是测试内容这是测试内容容这这是测试内容这是测试内容容这是测试内容这是测试内容容这是测试内容这是测试内容容这是测试内容这是测试内容容这是测试内容这是测试内容容这是测试内容这是测试内测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测试内容这是测试内容容这这是测试内容这是测试内容容这这是测试内容这是测试内容容这是测试内容这是测试内容容这是测试内容这是测试内容容这是测试内容这是测试内容容这是测试内容这是测试内容容这是测试内容这是测试内容内容这是测试内容这是测试内容这是测试内容这是测试内容这是测试内容这是测试内容这是测试内容这是测试内容这是测试内容这是测试内容";
    @Override
    public int getLayoutId() {
        return R.layout.chat_layout;
    }

    private RecyclerView rcView;
    private EditText et_input;
    private TextView tv_send;
    private TextView tv_connect_state;
    @Override
    public void findViews() {

        tv_connect_state = (TextView) findViewById(R.id.tv_connect_state);

        rcView = (RecyclerView) findViewById(R.id.rcView);
        et_input = (EditText) findViewById(R.id.et_input);
        tv_send = (TextView) findViewById(R.id.tv_send);
    }

    private RcAdater mAdapter;
    List<LocalMessage> messageList = new ArrayList<LocalMessage>();

    private String receiverId;

    @Override
    public void init(Bundle savedInstanceState) {
        IMClient.instance().addEventListener(this);
        receiverId = getIntent().getStringExtra("uid")==null?"":getIntent().getStringExtra("uid");
        setTitle("与" + receiverId);
        tv_connect_state.setText(IMClient.instance().isConnecting()?"连接中...":"服务器已断开..");
        tv_connect_state.setVisibility(IMClient.instance().isConnected() ? View.GONE : View.VISIBLE);


        mAdapter = new RcAdater();
        rcView.setLayoutManager(new LinearLayoutManager(mActivity));
        rcView.setAdapter(mAdapter);
        loadLocalMessages();
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void  onDestroy(){
        super.onDestroy();
        IMClient.instance().removeEventListener(this);
    }
    @Override
    public void setListeners() {
        super.setListeners();
        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message.Data.Builder msg = Message.Data.newBuilder();
                msg.setCmd(Message.Data.Cmd.CHAT_MSG_VALUE);
                msg.setCreateTime(System.currentTimeMillis());
                msg.setSender(mApp.getUid());
                msg.setReceiver(receiverId);
                String ct = et_input.getText().toString().trim();
                msg.setContent(ct);
                et_input.setText("");
                LocalMessage localMessage = new LocalMessage(msg);
             ;
                IMClient.instance().sendMessage(msg);
                messageList.add(localMessage);
                mAdapter.notifyDataSetChanged();
                rcView.scrollToPosition(messageList.size()-1);
            }
        });
    }

    private void loadLocalMessages() {
        Message.Data.Builder data = null;
        for (int i = 0; i < 50; i++) {
            data = Message.Data.newBuilder();
            data.setCmd(Message.Data.Cmd.CHAT_MSG_VALUE);
            Random random = new Random();
            boolean b = random.nextBoolean();
            if (b) {
                data.setSender(mApp.getUid());
            } else {
                data.setSender(receiverId);
            }
            String ct = content.substring(0,random.nextInt(50));
            data.setContent(ct + i);
            data.setCreateTime(System.currentTimeMillis());
            LocalMessage localMessage = new LocalMessage(data);
            messageList.add(localMessage);


        }
    }


    @Override
    public void onSendFailure(Message.Data.Builder msg) {

    }

    @Override
    public void onSendSucceed(Message.Data.Builder msg) {

    }

    @Override
    public void onConnectFailure(String msg) {
        tv_connect_state.setText("连接失败..");
        tv_connect_state.setVisibility(View.VISIBLE);
    }

    @Override
    public void onReceiveMessage(Message.Data msg) {
        if(msg.getCmd() == Message.Data.Cmd.CHAT_MSG_VALUE){
            LocalMessage localMessage = new LocalMessage(msg);
            messageList.add(localMessage);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onConnected() {
        tv_connect_state.setVisibility(View.GONE);
    }

    @Override
    public void onDisconnected(boolean isException) {
        Log.i(TAG, "onDisconnected");
        tv_connect_state.setText("服务器已断开..");
        tv_connect_state.setVisibility(View.VISIBLE);
    }
    @Override
    public void onConnecting() {
        Log.i(TAG, "onConnecting");
        tv_connect_state.setText("连接中..");
        tv_connect_state.setVisibility(View.VISIBLE);
    }

    private class RcAdater extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case 0:
                    return new ViewHolder(View.inflate(mActivity, R.layout.chat_item_left, null));
                case 1:
                    return new ViewHolder(View.inflate(mActivity, R.layout.chat_item_right, null));
                default:
                    return new ViewHolder(View.inflate(mActivity, R.layout.chat_item_right, null));
            }

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            LocalMessage localMessage = messageList.get(position);
            Message.Data data = localMessage.getData();
            ViewHolder vholder = (ViewHolder) holder;
            vholder.tv_message.setText(data.getContent());
        }

        @Override
        public int getItemViewType(int position) {
            LocalMessage localMessage = messageList.get(position);
            Message.Data data = localMessage.getData();
            if (data.getSender().equals(mApp.getUid())) {
                return 0;
            } else {
                return 1;
            }
        }

        @Override
        public int getItemCount() {
            return messageList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv_message;

            public ViewHolder(View itemView) {
                super(itemView);
                tv_message = (TextView) itemView.findViewById(R.id.tv_message);
            }
        }
    }

}
