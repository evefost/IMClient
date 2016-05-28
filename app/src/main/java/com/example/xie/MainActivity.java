package com.example.xie;

import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.common.ui.base.BaseActivity;
import com.common.ui.base.FragmentTabManager;
import com.example.xie.imclient.R;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by xie on 2016/2/25.
 */
public class MainActivity extends BaseActivity {

    public static final  String TAB_CHAT = "chat";
    public static final  String TAB_CONTACT = "contact";
    public static final  String TAB_ME = "me";

    @InjectView(R.id.tv_chat)
    TextView mTvChat;
    @InjectView(R.id.tv_contact)
    TextView mTvContact;
    @InjectView(R.id.tv_me)
    TextView mTvMe;

    @Override
    public int getLayoutId() {
        return R.layout.main_layout;
    }

    FragmentTabManager mTabManager;

    @Override
    public void init(Bundle savedInstanceState) {
        hideTopBar(true);
        mTabManager = new FragmentTabManager(this, getSupportFragmentManager(), R.id.container);
        mTabManager
                .addTab(TAB_CHAT, HomeFragment.class, null)
                .addTab(TAB_CONTACT, UserListFragment.class, null)
                .addTab(TAB_ME, Mefragment.class, null);
        mTabManager.changeTab(TAB_CHAT);
        mTvChat.setSelected(true);
        mTabManager.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                mTvChat.setSelected(TAB_CHAT.equals(tabId));
                mTvContact.setSelected(TAB_CONTACT.equals(tabId));
                mTvMe.setSelected(TAB_ME.equals(tabId));
            }
        });
    }

    @OnClick({
            R.id.tv_chat,
            R.id.tv_contact,
            R.id.tv_me})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_chat:
                mTabManager.changeTab(TAB_CHAT);
                break;
            case R.id.tv_contact:
                mTabManager.changeTab(TAB_CONTACT);
                break;
            case R.id.tv_me:
                mTabManager.changeTab(TAB_ME);
                break;
        }
    }

}
