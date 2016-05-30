package com.im.ui;

import android.content.Context;

import com.examp.bean.LocalMessage;
import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.recyclerview.CommonAdapter;

import java.util.List;

/**
 * Created by dell on 2016/5/30.
 */

public class RecentAdapter extends CommonAdapter<LocalMessage> {

    public RecentAdapter(Context context, int layoutId, List<LocalMessage> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, LocalMessage localMessage) {

    }
}
