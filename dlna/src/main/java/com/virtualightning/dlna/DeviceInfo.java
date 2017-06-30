package com.virtualightning.dlna;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DeviceInfo {
    String IP;//IP地址
    int port;//端口
    String host;//主机地址

    long lastActiveTime;//上一次激活时间，和生命周期相关
    long cacheTime;//存活时间，如果当前时间减去上一次激活时间大于存活时间，则认定此设备已注销

    int specMajorVersion;//主要版本
    int specMinorVersion;//次要版本

    String USN;//单一设备名
    String location;//DDD地址
    String UUID;//通用唯一识别码
    String friendlyName;//设备名
    String manufacturer;//制造商
    String manufacturerURL;//制造商地址
    String modelName;//模块名
    String modelNumber;//模块编号
    String modelDescription;//模块介绍
    String modelURL;//模块网址
    String serialNumber;//序列号
    String deviceType;//设备类型
    String server;//设备服务器

    List<Icon> iconList = new LinkedList<>();//图片列表
    HashMap<String,Service> serviceMap = new HashMap<>();//服务列表

    Object otherParams;//其他参数

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public long getLastActiveTime() {
        return lastActiveTime;
    }

    public void setLastActiveTime(long lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
    }

    public long getCacheTime() {
        return cacheTime;
    }

    public void setCacheTime(long cacheTime) {
        this.cacheTime = cacheTime;
    }

    public int getSpecMajorVersion() {
        return specMajorVersion;
    }

    public void setSpecMajorVersion(int specMajorVersion) {
        this.specMajorVersion = specMajorVersion;
    }

    public int getSpecMinorVersion() {
        return specMinorVersion;
    }

    public void setSpecMinorVersion(int specMinorVersion) {
        this.specMinorVersion = specMinorVersion;
    }

    public String getUSN() {
        return USN;
    }

    public void setUSN(String USN) {
        this.USN = USN;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getManufacturerURL() {
        return manufacturerURL;
    }

    public void setManufacturerURL(String manufacturerURL) {
        this.manufacturerURL = manufacturerURL;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getModelDescription() {
        return modelDescription;
    }

    public void setModelDescription(String modelDescription) {
        this.modelDescription = modelDescription;
    }

    public String getModelURL() {
        return modelURL;
    }

    public void setModelURL(String modelURL) {
        this.modelURL = modelURL;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setOtherParams(Object otherParams) {
        this.otherParams = otherParams;
    }

    public Object getOtherParams() {
        return otherParams;
    }

    public List<Icon> getIconList() {
        return iconList;
    }

    public void putIcon(Icon icon) {
        this.iconList.add(icon);
    }

    public HashMap<String, Service> getServiceMap() {
        return serviceMap;
    }

    public void putService(String deviceType,Service service) {
        this.serviceMap.put(deviceType, service);
    }

//    String NTS; NTS 通知消息子类型 去掉

}
