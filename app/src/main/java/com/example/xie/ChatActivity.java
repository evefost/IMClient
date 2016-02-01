package com.example.xie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xie.imclient.R;
import com.im.sdk.core.ClientHandler;
import com.im.sdk.protocal.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by xie on 2016/2/1.
 */
public class ChatActivity extends BaseActivity implements ClientHandler.IMEventListener{


    public static  void lauchActivity(Activity activity,String account){
        Intent intent = new Intent(activity.getApplicationContext(),ChatActivity.class);
        activity.startActivity(intent);
    }
    @Override
    public int getLayoutId() {
        return R.layout.chat_layout;
    }

    private RecyclerView rcView;

    @Override
    public void findViews() {

        rcView = (RecyclerView) findViewById(R.id.rcView);
    }

    private  RcAdater mAdapter;
    List<Message.Data> list = new ArrayList<Message.Data>();
    @Override
    public void init(Bundle savedInstanceState) {
        mAdapter = new RcAdater();
        rcView.setLayoutManager(new LinearLayoutManager(mActivity));
        rcView.setAdapter(mAdapter);
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


    private class RcAdater extends  RecyclerView.Adapter{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType){
                case 0:
                    return new ViewHolder(View.inflate(mActivity,R.layout.chat_item_left,null));
                case 1:
                    return new ViewHolder(View.inflate(mActivity,R.layout.chat_item_right,null));
                default:
                    return new ViewHolder(View.inflate(mActivity,R.layout.chat_item_right,null));
            }

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Message.Data data = list.get(position);
            ViewHolder vholder = (ViewHolder) holder;
            vholder.tv_message.setText(data.getContent());
        }

        @Override
        public int getItemViewType(int position) {
            Message.Data data = list.get(position);
            if(ClientApplication.mUser.getAccount().equals(data.getAccount())){
                return 0;
            }else{
                return 1;
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView tv_message;
            public ViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

}
