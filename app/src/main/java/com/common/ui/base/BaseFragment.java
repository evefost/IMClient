package com.common.ui.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xie.ClientApplication;
import com.example.xie.imclient.R;
import com.im.sdk.core.IMClient;

import butterknife.ButterKnife;

/**
 * Created by mis on 2016/2/17.
 */
public abstract class BaseFragment extends Fragment implements PageInterface {
    protected ClientApplication mApp;
    protected Context mContext = null;
    protected Activity mActivity = null;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext().getApplicationContext();
        mApp = (ClientApplication) getContext().getApplicationContext();
        mActivity = getActivity();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private LinearLayout mContentView;
    private ViewGroup mHeaderView;
    private View rl_left;
    private View rl_right;
    private TextView tv_title;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mContentView = new LinearLayout(mActivity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        mContentView.setLayoutParams(params);
        mContentView.setOrientation(LinearLayout.VERTICAL);

        mHeaderView = new FrameLayout(mActivity);
        ViewGroup.LayoutParams hParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        mHeaderView.addView(mActivity.getLayoutInflater().inflate(R.layout.header, null));
        mContentView.addView(mHeaderView);

        View mView = inflater.inflate(getLayoutId(), container, false);
        mContentView.addView(mView);
        ButterKnife.inject(this, mContentView);
        initHeaderView();
        findViews();
        return mContentView;
    }

    private void initHeaderView(){
        rl_left = mHeaderView.findViewById(R.id.rl_left);
        tv_title = (TextView) mHeaderView.findViewById(R.id.tv_title);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(savedInstanceState);
        setListeners();
    }

    public View findViewById(int id) {
        return mContentView.findViewById(id);
    }

    public void enableBack(boolean enable){
        if(rl_left != null){
            rl_left.setVisibility(enable?View.VISIBLE:View.INVISIBLE);
        }
    }

    @Override
    public void findViews() {

    }
    @Override
    public void setListeners() {
        if(rl_left != null){
            rl_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
        }
    }
    @Override
    public void hideTopBar(boolean flag){
        mHeaderView.setVisibility(flag?View.GONE:View.VISIBLE);
    }

    public void hideBack(boolean flag){
        rl_left.setVisibility(flag?View.GONE:View.VISIBLE);
    }
    @Override
    public void setTitle(int resId) {
        setTitle(getResources().getString(resId));
    }

    @Override
    public void setTitle(String title){
        if(tv_title != null ){
            tv_title.setText(title);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }




}
