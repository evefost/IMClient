package com.im.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.common.ui.base.BaseFragment;
import com.examp.bean.LocalMessage;
import com.example.xie.imclient.R;
import com.im.sdk.core.ClientHandler;
import com.im.sdk.core.IMClient;
import com.im.sdk.protocal.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.InjectView;

/**
 * 最近
 */
public class RecentFragment extends BaseFragment implements ClientHandler.IMEventListener  {

    @InjectView(R.id.rcView)
    RecyclerView mRcView;

    @Override
    public int getLayoutId() {
        return R.layout.recent_layout;
    }
    List<LocalMessage> messageList = new ArrayList<LocalMessage>();
    RecentAdapter mAdapter;

    String content ="这是测试内容这是测试容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测试内容这是测试内容容这这是测试内容这是测试内容容这这是测试内容这是测试内容容这是测试内容这是测试内容容这是测试内容这是测试内容容这是测试内容这是测试内容容这是测试内容这是测试内容容这是测试内容这是测试内测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测试内容这是测试内容容这这是测试内容这是测试内容容这这是测试内容这是测试内容容这是测试内容这是测试内容容这是测试内容这是测试内容容这是测试内容这是测试内容容这是测试内容这是测试内容容这是测试内容这是测试内测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测试内容这是测试内容容这这是测试内容这是测试内容容这这是测试内容这是测试内容容这是测试内容这是测试内容容这是测试内容这是测试内容容这是测试内容这是测试内容容这是测试内容这是测试内容容这是测试内容这是测试内测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测这这是测试内容这是测试内容容这这是测试内容这是测试内容容这这是测试内容这是测试内容容这这是测试内容这是测试内容容这是测试内容这是测试内容容这是测试内容这是测试内容容这是测试内容这是测试内容容这是测试内容这是测试内容容这是测试内容这是测试内容内容这是测试内容这是测试内容这是测试内容这是测试内容这是测试内容这是测试内容这是测试内容这是测试内容这是测试内容这是测试内容";

    @Override
    public void init(Bundle savedInstanceState) {
        IMClient.instance().registIMEventListener(this);
        setTitle("聊天");
        enableBack(false);

        mAdapter = new RecentAdapter(mContext,R.layout.chat_item_recent,messageList);
        mRcView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRcView.setAdapter(mAdapter);
        loadLocalMessages();
        mAdapter.notifyDataSetChanged();
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
                data.setSender("32223e2e2e32e23");
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

    }

    @Override
    public void onConnecting() {

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
    public void onDestroy() {
        IMClient.instance().unRegistIMEventListener(this);
        super.onDestroy();
    }
}
