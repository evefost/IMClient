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

    public void hideTopBar(boolean flag);

    public void hideBack(boolean flag);

    public void setTitle(int resId);

    public void setTitle(String title);
}
