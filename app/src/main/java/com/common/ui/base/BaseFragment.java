package com.common.ui.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mis on 2016/2/17.
 */
public abstract class BaseFragment extends Fragment implements PageInterface {

    protected Context mContext = null;
    protected Activity mActivity = null;
    private View mView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getContext().getApplicationContext();
        mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getLayoutId(), container, false);
        findViews();
        return mView;
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(savedInstanceState);
        setListeners();
    }

    public View findViewById(int id) {
        return mView.findViewById(id);
    }

    @Override
    public void setListeners() {

    }
    @Override
    public void hideTopBar(boolean flag){

    }
    @Override
    public void setTitle(int resId) {
        setTitle(getResources().getString(resId));
    }

    @Override
    public void setTitle(String title){

    }

}
