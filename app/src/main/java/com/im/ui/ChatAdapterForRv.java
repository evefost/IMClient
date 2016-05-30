package com.im.ui;

import android.content.Context;

import com.examp.bean.LocalMessage;
import com.example.xie.imclient.R;
import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.recyclerview.MultiItemCommonAdapter;
import com.zhy.base.adapter.recyclerview.MultiItemTypeSupport;

import java.util.List;

public class ChatAdapterForRv extends MultiItemCommonAdapter<LocalMessage> {

    public ChatAdapterForRv(Context context, List<LocalMessage> datas) {
        super(context, datas, new MultiItemTypeSupport<LocalMessage>() {
            @Override
            public int getLayoutId(int itemType) {
                if (itemType == LocalMessage.RECIEVE_MSG) {
                    return R.layout.chat_item_other_text;
                } else{
                    return R.layout.chat_item_me_text;
                }
            }

            @Override
            public int getItemViewType(int postion, LocalMessage msg) {
                if (msg.isComMeg()) {
                    return LocalMessage.RECIEVE_MSG;
                }
                return LocalMessage.SEND_MSG;
            }
        });
    }

    @Override
    public void convert(ViewHolder holder, LocalMessage chatMessage) {

        switch (holder.getLayoutId()) {
            case R.layout.chat_item_me_text:
                holder.setText(R.id.tv_message,chatMessage.getData().getContent());
                break;
            case  R.layout.chat_item_other_text:
                holder.setText(R.id.tv_message,chatMessage.getData().getContent());
                break;
        }
    }
}
