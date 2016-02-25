package com.example.xie;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.ui.base.BaseActivity;
import com.examp.bean.LocalMessage;
import com.examp.bean.User;
import com.example.xie.imclient.R;
import com.im.sdk.protocal.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by mis on 2016/2/25.
 */
public class OnLineUsersListActivity extends BaseActivity {

    private  String TAG = getClass().getSimpleName();

    @Override
    public int getLayoutId() {
        return R.layout.online_user_list_layout;
    }

    private RecyclerView rcView;

    @Override
    public void findViews() {
        rcView = (RecyclerView) findViewById(R.id.rcView);
    }

    private RcAdater mAdapter;
    private List<User> userList = new ArrayList<User>();

    @Override
    public void init(Bundle savedInstanceState) {

        setTitle("当前在线用户");
        mAdapter = new RcAdater();
        rcView.setLayoutManager(new LinearLayoutManager(mActivity));
        rcView.setAdapter(mAdapter);

        createUserList();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setListeners() {
        super.setListeners();
    }

    private void createUserList(){
        for(int i=0;i<50;i++){
            User user = new User();
            String uuid = UUID.randomUUID().toString();
            uuid = uuid.replace("-","");
            Log.i(TAG,"uuid:"+uuid);
            user.setUid(uuid);
            userList.add(user);
        }

    }

    private class RcAdater extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(View.inflate(mActivity, R.layout.user_item, null));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final User user = userList.get(position);
            ViewHolder vholder = (ViewHolder) holder;
            vholder.tv_user_id.setText("uuid: "+user.getUid());
            vholder.tv_user_id.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChatActivity.lauchActivity(mActivity,user.getUid());
                }
            });
        }


        @Override
        public int getItemCount() {
            return userList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv_user_id;
            public ViewHolder(View itemView) {
                super(itemView);
                tv_user_id = (TextView) itemView.findViewById(R.id.tv_user_id);
            }
        }
    }

}
