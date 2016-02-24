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

    String content ="这是测试内容这是测试容这这是测试内容这是测试内容容这这是测试内容这是测试内容容这这是测试内容这是测试内容容这是测试内容这是测试内容容这是测试内容这是测试内容容这是测试内容这是测试内容容这是测试内容这是测试内容容这是测试内容这是测试内容内容这是测试内容这是测试内容这是测试内容这是测试内容这是测试内容这是测试内容这是测试内容这是测试内容这是测试内容这是测试内容";
    @Override
    public int getLayoutId() {
        return R.layout.chat_layout;
    }

    private RecyclerView rcView;
    private EditText et_input;
    private TextView tv_send;
    @Override
    public void findViews() {

        rcView = (RecyclerView) findViewById(R.id.rcView);
        et_input = (EditText) findViewById(R.id.et_input);
        tv_send = (TextView) findViewById(R.id.tv_send);
    }

    private RcAdater mAdapter;
    List<LocalMessage> messageList = new ArrayList<LocalMessage>();

    private String receiverId;

    @Override
    public void init(Bundle savedInstanceState) {
        receiverId = getIntent().getStringExtra("uid");
        setTitle("chat...");
        mAdapter = new RcAdater();
        rcView.setLayoutManager(new LinearLayoutManager(mActivity));
        rcView.setAdapter(mAdapter);
        generDatas();
        mAdapter.notifyDataSetChanged();

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
                Random random = new Random();
                String ct = content.substring(0,random.nextInt(50));
                msg.setContent(ct);
                LocalMessage localMessage = new LocalMessage(msg);
                messageList.add(localMessage);
                IMClient.instance().sendMessage(msg);
            }
        });
    }

    private void generDatas() {
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

    }

    @Override
    public void onDisconnected() {

    }
    @Override
    public void onConnecting() {
        Log.i(TAG, "onConnecting");
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
