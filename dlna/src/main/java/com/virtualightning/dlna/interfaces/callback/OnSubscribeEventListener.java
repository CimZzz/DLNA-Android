package com.virtualightning.dlna.interfaces.callback;


import com.virtualightning.dlna.Service;
import com.virtualightning.dlna.SubscribeEvent;

public interface OnSubscribeEventListener {
    void onSubscribeEvent(Service service, SubscribeEvent event);
}
