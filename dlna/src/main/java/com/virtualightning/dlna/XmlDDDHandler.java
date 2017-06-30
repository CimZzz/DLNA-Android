package com.virtualightning.dlna;

import org.xml.sax.Attributes;

import com.virtualightning.dlna.tools.XmlAnalyzeStream;

public class XmlDDDHandler extends XmlBaseHandler<DeviceInfo> {
    private Icon icon;
    private Service service;
    private final XmlAnalyzeStream.AnalyzerChain analyzerChain;

    XmlDDDHandler(final DeviceInfo deviceInfo) {

        XmlAnalyzeStream sepcVersion;
        XmlAnalyzeStream device;
        XmlAnalyzeStream iconList;
        XmlAnalyzeStream serviceList;
        XmlAnalyzeStream iconStream;
        XmlAnalyzeStream serviceStream;
        XmlAnalyzeStream root = new XmlAnalyzeStream("root",false)
                .addChildElement(sepcVersion = new XmlAnalyzeStream("specVersion",false))
                .addChildElement(device = new XmlAnalyzeStream("device",false));

        sepcVersion
                .addChildElement(new XmlAnalyzeStream("major",false,new XmlAnalyzeStream.OnElementCallback(true){
                    @Override
                    public void onElementValue(String value) {
                        deviceInfo.setSpecMajorVersion(Integer.parseInt(value));
                    }
                }))
                .addChildElement(new XmlAnalyzeStream("minor",false,new XmlAnalyzeStream.OnElementCallback(true){
                    @Override
                    public void onElementValue(String value) {
                        deviceInfo.setSpecMinorVersion(Integer.parseInt(value));
                    }
                }));



        device
                .addChildElement(new XmlAnalyzeStream("friendlyName",false,new XmlAnalyzeStream.OnElementCallback(true){
                    @Override
                    public void onElementValue(String value) {
                        deviceInfo.setFriendlyName(value);
                    }
                }))
                .addChildElement(new XmlAnalyzeStream("modelNumber",false,new XmlAnalyzeStream.OnElementCallback(true){
                    @Override
                    public void onElementValue(String value) {
                        deviceInfo.setModelNumber(value);
                    }
                }))
                .addChildElement(new XmlAnalyzeStream("modelName",false,new XmlAnalyzeStream.OnElementCallback(true){
                    @Override
                    public void onElementValue(String value) {
                        deviceInfo.setModelName(value);
                    }
                }))
                .addChildElement(new XmlAnalyzeStream("modelDescription",false,new XmlAnalyzeStream.OnElementCallback(true){
                    @Override
                    public void onElementValue(String value) {
                        deviceInfo.setModelDescription(value);
                    }
                }))
                .addChildElement(new XmlAnalyzeStream("manufacturer",false,new XmlAnalyzeStream.OnElementCallback(true){
                    @Override
                    public void onElementValue(String value) {
                        deviceInfo.setManufacturer(value);
                    }
                }))
                .addChildElement(new XmlAnalyzeStream("manufacturerURL",false,new XmlAnalyzeStream.OnElementCallback(true){
                    @Override
                    public void onElementValue(String value) {
                        deviceInfo.setManufacturerURL(value);
                    }
                }))
                .addChildElement(new XmlAnalyzeStream("modelURL",false,new XmlAnalyzeStream.OnElementCallback(true){
                    @Override
                    public void onElementValue(String value) {
                        deviceInfo.setModelURL(value);
                    }
                }))
                .addChildElement(new XmlAnalyzeStream("serialNumber",false,new XmlAnalyzeStream.OnElementCallback(true){
                    @Override
                    public void onElementValue(String value) {
                        deviceInfo.setSerialNumber(value);
                    }
                }))
                .addChildElement(new XmlAnalyzeStream("UDN",false,new XmlAnalyzeStream.OnElementCallback(true){
                    @Override
                    public void onElementValue(String value) {
                        deviceInfo.setUUID(value);
                    }
                }))
                .addChildElement(iconList = new XmlAnalyzeStream("iconList",false))
                .addChildElement(serviceList = new XmlAnalyzeStream("serviceList",false));


        iconList
                .addChildElement(iconStream = new XmlAnalyzeStream("icon",true,new XmlAnalyzeStream.OnElementCallback(){
                    @Override
                    public void onElementStart(Attributes attributes) {
                        icon = new Icon();
                    }

                    @Override
                    public void onElementEnd() {
                        deviceInfo.getIconList().add(icon);
                    }
                }));

        iconStream
                .addChildElement(new XmlAnalyzeStream("mimetype",true,new XmlAnalyzeStream.OnElementCallback(true){
                    @Override
                    public void onElementValue(String value) {
                        icon.mimetype = value;
                    }
                }))
                .addChildElement(new XmlAnalyzeStream("width",true,new XmlAnalyzeStream.OnElementCallback(true){
                    @Override
                    public void onElementValue(String value) {
                        icon.width = Integer.parseInt(value);
                    }
                }))
                .addChildElement(new XmlAnalyzeStream("height",true,new XmlAnalyzeStream.OnElementCallback(true){
                    @Override
                    public void onElementValue(String value) {
                        icon.height = Integer.parseInt(value);
                    }
                }))
                .addChildElement(new XmlAnalyzeStream("depth",true,new XmlAnalyzeStream.OnElementCallback(true){
                    @Override
                    public void onElementValue(String value) {
                        icon.depth = Integer.parseInt(value);
                    }
                }))
                .addChildElement(new XmlAnalyzeStream("url",true,new XmlAnalyzeStream.OnElementCallback(true){
                    @Override
                    public void onElementValue(String value) {
                        if(value.charAt(0) != '/')
                            value = '/' + value;
                        icon.url = deviceInfo.getHost() + value;
                    }
                }));



        serviceList
                .addChildElement(serviceStream = new XmlAnalyzeStream("service",true,new XmlAnalyzeStream.OnElementCallback(){
                    @Override
                    public void onElementStart(Attributes attributes) {
                        service = new Service();
                        service.deviceInfo = deviceInfo;
                    }

                    @Override
                    public void onElementEnd() {
                        deviceInfo.getServiceMap().put(service.serviceType,service);
                    }
                }));

        serviceStream
                .addChildElement(new XmlAnalyzeStream("serviceType",true,new XmlAnalyzeStream.OnElementCallback(true){
                    @Override
                    public void onElementValue(String value) {
                        service.serviceType = value;
                    }
                }))
                .addChildElement(new XmlAnalyzeStream("serviceId",true,new XmlAnalyzeStream.OnElementCallback(true){
                    @Override
                    public void onElementValue(String value) {
                        service.serviceId = value;
                    }
                }))
                .addChildElement(new XmlAnalyzeStream("controlURL",true,new XmlAnalyzeStream.OnElementCallback(true){
                    @Override
                    public void onElementValue(String value) {
                        service.controlURL = value;
                    }
                }))
                .addChildElement(new XmlAnalyzeStream("eventSubURL",true,new XmlAnalyzeStream.OnElementCallback(true){
                    @Override
                    public void onElementValue(String value) {
                        service.eventSubURL = value;
                    }
                }))
                .addChildElement(new XmlAnalyzeStream("SCPDURL",true,new XmlAnalyzeStream.OnElementCallback(true){
                    @Override
                    public void onElementValue(String value) {
                        service.SCPDURL = value;
                    }
                }));

        analyzerChain = new XmlAnalyzeStream.AnalyzerChain(root);
    }


    @Override
    protected XmlAnalyzeStream.AnalyzerChain getAnalyzerChain() {
        return analyzerChain;
    }
}
