package common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.zhy.autolayou.AutoLayoutActivity;

import java.io.Serializable;

/**
 * Created by xie on 2016/2/1.
 */
public abstract class BaseActivity extends AutoLayoutActivity implements PageInterface {

    protected Context mContext;
    protected Activity mActivity;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getApplicationContext();
        this.mActivity = this;
        setContentView(getLayoutId());
        findViews();
        init(savedInstanceState);
        setListeners();
    }

    @Override
    public void setListeners() {

    }
}
