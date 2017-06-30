package com.virtualightning.dlna;

import java.util.HashMap;

public class SubscribeEvent {
    public static final int ACTION_SUBSCRIBE_SUCCESS = 0;
    public static final int ACTION_SUBSCRIBE_FAILED = 1;
    public static final int ACTION_SUBSCRIBE_CON_FAILED = 2;
    public static final int ACTION_RENEW_SUBSCRIBE_SUCCESS = 3;
    public static final int ACTION_RENEW_SUBSCRIBE_FAILED = 4;
    public static final int ACTION_RENEW_SUBSCRIBE_CON_FAILED = 5;
    public static final int ACTION_CANCEL_SUBSCRIBE_SUCCESS = 6;
    public static final int ACTION_CANCEL_SUBSCRIBE_FAILED = 7;
    public static final int ACTION_CANCEL_SUBSCRIBE_CON_FAILED = 8;
    public static final int ACTION_SUBSCRIBE_RESPONSE = 9;

    int instanceId;
    int actionId;
    long seq;
    HashMap<String,String> feature;

    public int getAction() {
        return actionId;
    }

    public long getSeq() {
        return seq;
    }

    public int getInstanceId() {
        return instanceId;
    }

    public String getFeature(String key) {
        if(feature != null)
            return feature.get(key);

        return null;
    }

    public void putFeature(String key,String value) {
        if(feature != null)
            feature.put(key, value);
    }
}
