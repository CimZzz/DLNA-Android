package com.virtualightning.dlna;

import java.util.HashMap;

import com.virtualightning.dlna.tools.Locker;

public class Service {
    DeviceInfo deviceInfo;//所属设备
    String serviceType;//服务类型
    String serviceId;//服务ID
    String controlURL;//SOAP控制地址
    String eventSubURL;//GENA订阅地址
    String SCPDURL;//服务描述文档地址

    int specMajorVersion;//主要版本
    int specMinorVersion;//次要版本

    final SubscribeItem subscribeItem;//订阅项
    final Locker subscribeLocker;//订阅锁

    HashMap<String,Action> actionMap = null;//动作表
    HashMap<String,StateVariable> stateVariableMap = null;//变量表

    Service() {
        subscribeItem = new SubscribeItem();
        subscribeLocker = new Locker();
    }

    void acceptLocker() {
        subscribeLocker.validLocker();
        subscribeLocker.lock();
    }

    void releaseLocker() {
        subscribeLocker.releaseAll();
    }

    //判断是否需要重新订阅
    synchronized boolean isNeedSubscribe() {
        if(!subscribeItem.isAvaliable) {
            return true;
        }
        long timeDifference = System.currentTimeMillis() - subscribeItem.lastSubscribeTime;

        if(timeDifference > subscribeItem.timeOut) {
            subscribeItem.isAvaliable = false;
            return true;
        }

        return false;
    }

    synchronized boolean checkSubscribingState(boolean flag) {
        boolean returnVal = subscribeItem.isSubscrbing == flag;

        if(!returnVal)
            subscribeItem.isSubscrbing = flag;

        return returnVal;
    }

    synchronized void subscribe(String subscribeId,long lastSubscribeTime,long timeOut) {
        subscribeItem.subscribeId = subscribeId;
        subscribeItem.lastSubscribeTime = lastSubscribeTime;
        subscribeItem.timeOut = timeOut;
        subscribeItem.isAvaliable = true;
    }

    synchronized String cancelSubscribe() {
        String subscribeId = subscribeItem.subscribeId;
        subscribeItem.subscribeId = "";
        subscribeItem.isAvaliable = false;
        return subscribeId;
    }

    synchronized boolean checkCancelSubscribeState(boolean flag) {
        //如果需要执行取消订阅但尚未订阅则返回false
        if(flag && !subscribeItem.isAvaliable)
            return false;

        boolean returnVal = subscribeItem.isCancelSubscrbing == flag;

        if(!returnVal)
            subscribeItem.isCancelSubscrbing = flag;

        return returnVal;
    }

    synchronized String getSubscribeId() {
        return subscribeItem.subscribeId;
    }

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }


}
