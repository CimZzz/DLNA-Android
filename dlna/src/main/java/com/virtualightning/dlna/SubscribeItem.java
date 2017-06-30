package com.virtualightning.dlna;

public class SubscribeItem {
    String deviceUSN;//设备唯一标识名
    String subscribeId;//订阅ID
    long lastSubscribeTime;//上一次订阅的时间
    long timeOut;//订阅有效时间

    boolean isAvaliable;
    boolean isSubscrbing;
    boolean isCancelSubscrbing;

    SubscribeItem() {
        this.subscribeId = "";
        this.isAvaliable = false;
        this.isSubscrbing = false;
    }

    String getSubscribeId() {
        return subscribeId;
    }
}
