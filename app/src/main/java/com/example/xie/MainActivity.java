package com.example.xie;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.common.ui.base.BaseActivity;
import com.example.xie.imclient.R;

/**
 * Created by xie on 2016/2/25.
 */
public class MainActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.main_layout;
    }

    private ViewPager view_pager;
    private Button btn_home;
    private Button btn_users;
    @Override
    public void findViews() {
        view_pager = (ViewPager) findViewById(R.id.view_pager);
        btn_home = (Button) findViewById(R.id.btn_home);
        btn_users = (Button) findViewById(R.id.btn_users);
    }
    TradeRecordPagerAdapter mAdapter;

    @Override
    public void init(Bundle savedInstanceState) {
        hideTopBar(true);
        mAdapter = new TradeRecordPagerAdapter(getSupportFragmentManager());
        view_pager.setAdapter(mAdapter);
    }


    @Override
    public void setListeners(){
        super.setListeners();
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_pager.setCurrentItem(0);
            }
        });
        btn_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_pager.setCurrentItem(1);
            }
        });
    }

    /**
     * 分页指示器的数据适配器
     */
    class TradeRecordPagerAdapter extends FragmentPagerAdapter {
        // ChargingOrderFragment chargingOrderFragment = new ChargingOrderFragment();

        public TradeRecordPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

                switch (position) {
                    case 0:
                        return new HomeFragment();
                    case 1:
                        return new UserListFragment();
                    default:
                        return new HomeFragment();
                }

        }

        @Override
        public int getCount() {

            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "同品牌车主";
                case 1:
                    return "全部车主";
                default:
                    return "";
            }
        }
    }
}
