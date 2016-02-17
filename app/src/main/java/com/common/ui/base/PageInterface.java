package com.common.ui.base;

import android.os.Bundle;

/**
 * Created by xie on 2016/2/1.
 */
public interface PageInterface {




    public int getLayoutId();

    public void findViews();


    public void init(Bundle savedInstanceState);

    public void setListeners();
}
