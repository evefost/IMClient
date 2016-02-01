package com.example.xie;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.example.xie.imclient.R;
import com.im.sdk.core.IMClient;

/**
 * Created by xie on 2016/2/1.
 */
public abstract class BaseActivity extends Activity implements PageInterface {

    protected Context mContext;
    private Activity mAct;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = ClientApplication.instance();
        this.mAct = this;
        setContentView(getLayoutId());
        findViews();
        init(savedInstanceState);
        setListeners();
    }

    @Override
    public void setListeners() {

    }
}
