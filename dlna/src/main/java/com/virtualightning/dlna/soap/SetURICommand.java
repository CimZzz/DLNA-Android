package com.virtualightning.dlna.soap;

import org.xml.sax.SAXException;

import javax.xml.transform.sax.TransformerHandler;

import com.virtualightning.dlna.DLNAClient;
import com.virtualightning.dlna.SoapCommand;
import com.virtualightning.dlna.constant.DeviceType;

public class SetURICommand extends SoapCommand {
    private final int instanceId;
    private final String path;

    public SetURICommand(int instanceId,String path) {
        super(DeviceType.AV_TRANSPORT, "SetAVTransportURI");
        this.path = path;
        this.instanceId = instanceId;
        System.out.println("SetURI : " + path);
    }

    public SetURICommand(DLNAClient client, int instanceId, String path) {
        this(instanceId,"http://" + client.getHostAddr() + ":" + client.getHTTPPort() + "/" + path);
    }



    @Override
    protected void writeCommand(TransformerHandler handler) throws SAXException {
        simpleElement("InstanceID",String.valueOf(instanceId));
        simpleElement("CurrentURI",path);
        simpleElement("CurrentURIMetaData");
    }
}
