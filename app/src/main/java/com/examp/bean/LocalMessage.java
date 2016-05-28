package com.examp.bean;

import com.example.xie.ClientApplication;
import com.im.sdk.protocal.Message;

/**
 * Created by mis on 2016/2/24.
 */
public class LocalMessage {

    public static final  int RECIEVE_MSG = 0;
    public static final  int SEND_MSG = 1;

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

    public boolean isComMeg(){
        if (data.getSender().equals(((ClientApplication)ClientApplication.instance()).getUid())) {
            return false;
        } else {
            return true;
        }
    }

}
