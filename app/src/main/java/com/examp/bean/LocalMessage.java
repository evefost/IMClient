package com.examp.bean;

import com.im.sdk.protocal.Message;

/**
 * Created by mis on 2016/2/24.
 */
public class LocalMessage {
    public static final  int MSG_TYPE_SEND = 0;
    public static final  int MSG_TYPE_RECEIVE = 1;

    private int type;

    private Message.Data receiveData;
    private Message.Data.Builder sendData;
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        receiveData = sendData.getDefaultInstanceForType();

    }

    public String getContent(){
        if(type == MSG_TYPE_RECEIVE) {
            if(receiveData != null){
                return receiveData.getContent();
            }
            return "";
        }else{
            if(sendData != null){
                return sendData.getContent();
            }
            return "";
        }
    }
}
