package com.example.xie;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.common.ui.base.BaseFragment;
import com.example.xie.imclient.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 最近
 */
public class RecentFragment extends BaseFragment {

    @InjectView(R.id.rcView)
    RecyclerView mRcView;

    @Override
    public int getLayoutId() {
        return R.layout.recent_layout;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setTitle("聊天");
        enableBack(false);
    }

}
