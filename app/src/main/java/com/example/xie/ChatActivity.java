package com.example.xie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.common.ui.base.BaseActivity;
import com.example.xie.imclient.R;
import com.im.sdk.core.ClientHandler;
import com.im.sdk.dao.DaoMaster;
import com.im.sdk.protocal.Message;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by xie on 2016/2/1.
 */
public class ChatActivity extends BaseActivity implements ClientHandler.IMEventListener {


    public static void lauchActivity(Activity activity, String account) {
        Intent intent = new Intent(activity.getApplicationContext(), ChatActivity.class);
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
    List<Message.Data.Builder> list = new ArrayList<Message.Data.Builder>();

    @Override
    public void init(Bundle savedInstanceState) {
        setTitle("chat...");
        mAdapter = new RcAdater();
        rcView.setLayoutManager(new LinearLayoutManager(mActivity));
        rcView.setAdapter(mAdapter);
        generDatas();
        mAdapter.notifyDataSetChanged();
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(mContext, null, null);
    }

    @Override
    public void setListeners() {
        super.setListeners();
        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                data.setAccount("123");
            } else {
                data.setAccount("123456");
            }
            String ct = content.substring(0,random.nextInt(50));
            data.setContent(ct + i);
            list.add(data);


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

    }

    @Override
    public void onConnected() {

    }

    @Override
    public void onDisconnected() {

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
            Message.Data.Builder data = list.get(position);
            ViewHolder vholder = (ViewHolder) holder;
            vholder.tv_message.setText(data.getContent());
        }

        @Override
        public int getItemViewType(int position) {
            Message.Data.Builder data = list.get(position);
//            if(ClientApplication.mUser.getAccount().equals(data.getAccount())){
            if (data.getAccount().equals("123")) {
                return 0;
            } else {
                return 1;
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
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
