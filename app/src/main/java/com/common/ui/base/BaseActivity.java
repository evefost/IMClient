package com.common.ui.base;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xie.ClientApplication;
import com.example.xie.imclient.R;
import com.zhy.autolayou.AutoLayoutActivity;

import java.io.Serializable;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.Utils;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;

/**
 * Created by xie on 2016/2/1.
 */
public abstract class BaseActivity extends AutoLayoutActivity implements PageInterface,SwipeBackActivityBase {

    protected ClientApplication mApp;
    protected  Context mContext = null;
    protected Activity mActivity = null;


    public static void lauchActivity(Activity startActivity,Class cls){
        lauchActivity(startActivity,cls,null);
    }

    public static void lauchActivity(Activity startActivity,Class cls,Serializable object){
        Intent intent = new Intent(startActivity.getApplicationContext(),cls);
        Bundle options = new Bundle();
        if(object != null){
            options.putSerializable("postObject", object);
        }
        ActivityCompat.startActivity(startActivity, intent, options);
    }

    private LinearLayout mContentView;
    private ViewGroup mHeaderView;

    private View rl_left;
    private View rl_right;
    private TextView tv_title;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApp = (ClientApplication) getApplicationContext();
        mContext = getApplicationContext();
        this.mActivity = this;

        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mContentView = new LinearLayout(this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        mContentView.setLayoutParams(params);
        mContentView.setOrientation(LinearLayout.VERTICAL);
        mHeaderView = new FrameLayout(this);
        ViewGroup.LayoutParams hParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        mHeaderView.addView(getLayoutInflater().inflate(R.layout.header, null));
        mContentView.addView(mHeaderView);

        mContentView.addView(getLayoutInflater().inflate(getLayoutId(), null));

        setContentView(mContentView);
        initHeaderView();

        findViews();
        init(savedInstanceState);
        setListeners();
    }

    private void initHeaderView(){
        rl_left = mHeaderView.findViewById(R.id.rl_left);
        tv_title = (TextView) mHeaderView.findViewById(R.id.tv_title);
    }

    @Override
    public void hideTopBar(boolean flag){
        mHeaderView.setVisibility(flag?View.GONE:View.VISIBLE);
    }

    public void hideBack(boolean flag){
        rl_left.setVisibility(flag?View.GONE:View.VISIBLE);
    }
    @Override
    public void setListeners() {
      if(rl_left != null){
          rl_left.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  finish();
              }
          });
      }
    }


    @Override
    public void setTitle(int resId){
        setTitle(getResources().getString(resId));
    }

    @Override
    public void setTitle(String title){
        if(tv_title != null){
            tv_title.setText(title==null?"":title);
        }
    }

    private SwipeBackActivityHelper mHelper;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }



    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    @Override
    public void startActivity(Intent intent){
        super.startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }



}
