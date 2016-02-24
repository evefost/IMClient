package com.examp.bean;

import com.im.sdk.protocal.Message;

/**
 * Created by mis on 2016/2/24.
 */
public class LocalMessage {

    private Message.Data data;

    private LocalMessage() {

    }

    public LocalMessage(Message.Data data) {
        this.data = data;
    }

    public LocalMessage(Message.Data.Builder data) {
        this.data = data.build();
    }


    public Message.Data getData() {
        return data;
    }

    public void setData(Message.Data data) {
        this.data = data;
    }


}
