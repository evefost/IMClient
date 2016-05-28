package com.common.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.TabHost;

import java.util.ArrayList;

public class FragmentTabManager {
    private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
    private Context mContext;
    private FragmentManager mFragmentManager;
    private int mContainerId;
    private TabHost.OnTabChangeListener mOnTabChangeListener;
    private TabInfo mLastTab;

    public FragmentTabManager(Context context, FragmentManager fragmentManager, int containerId) {
        this.mContext = context;
        this.mFragmentManager = fragmentManager;
        this.mContainerId = containerId;
    }

    public void setOnTabChangedListener(TabHost.OnTabChangeListener l) {
        mOnTabChangeListener = l;
    }

    public FragmentTabManager addTab(String tag, Class<?> clss, Bundle args) {
        TabInfo info = new TabInfo(tag, clss, args);

        mTabs.add(info);
        return this;
    }

    public Fragment getCurrentFragment() {
        return mLastTab == null ? null : mLastTab.fragment;
    }

    public String getCurrentTab() {
        return mLastTab == null ? null : mLastTab.tag;
    }

    public void changeTab(String tabId) {
        FragmentTransaction ft = doTabChanged(tabId, null);
        if (ft != null) {
            //ft.commit();
            ft.commitAllowingStateLoss();
        }
        if (mOnTabChangeListener != null) {
            mOnTabChangeListener.onTabChanged(tabId);
        }
    }

    private FragmentTransaction doTabChanged(String tabId, FragmentTransaction ft) {
        TabInfo newTab = null;
        for (int i = 0; i < mTabs.size(); i++) {
            TabInfo tab = mTabs.get(i);
            if (tab.tag.equals(tabId)) {
                newTab = tab;
            }
        }
        if (newTab == null) {
            throw new IllegalStateException("No tab known for tag " + tabId);
        }
        if (mLastTab != newTab) {
            if (ft == null) {
                ft = mFragmentManager.beginTransaction();
            }
            if (mLastTab != null) {
                if (mLastTab.fragment != null) {
                    //ft.detach(mLastTab.fragment);
                    ft.hide(mLastTab.fragment);
                }
            }
            if (newTab.fragment == null) {
                newTab.fragment = Fragment.instantiate(mContext, newTab.clss.getName(), newTab.args);
                ft.add(mContainerId, newTab.fragment, newTab.tag);
            } else {
                //ft.attach(newTab.fragment);
                ft.show(newTab.fragment);
            }

            mLastTab = newTab;
        }
        return ft;
    }

    public void restoreState(String tabId) {
        TabInfo newTab = null;
        for (int i = 0; i < mTabs.size(); i++) {
            TabInfo tab = mTabs.get(i);
            tab.fragment = mFragmentManager.findFragmentByTag(tab.tag);
            if (tab.tag.equals(tabId)) {
                newTab = tab;
            }
        }
        mLastTab = newTab;
    }

    public void detach() {
        if (mLastTab != null) {
            if (mLastTab.fragment != null) {
                mFragmentManager.beginTransaction().detach(mLastTab.fragment).commit();
            }
            mLastTab = null;
        }
    }

    public void destory(String tabId) {
        TabInfo newTab = null;
        for (int i = 0; i < mTabs.size(); i++) {
            TabInfo tab = mTabs.get(i);
            if (tab.tag.equals(tabId)) {
                newTab = tab;
            }
        }
        if (newTab == null) {
            throw new IllegalStateException("No tab known for tag " + tabId);
        }
        if (newTab.fragment != null) {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.remove(newTab.fragment).commitAllowingStateLoss();
            newTab.fragment = null;
        }
        if (newTab == mLastTab) {
            mLastTab = null;
        }
    }

    static final class TabInfo {
        private final String tag;
        private final Class<?> clss;
        private final Bundle args;
        private Fragment fragment;

        TabInfo(String _tag, Class<?> _class, Bundle _args) {
            tag = _tag;
            clss = _class;
            args = _args;
        }
    }

}
