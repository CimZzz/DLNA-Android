package com.virtualightning.dlna.soap;

import org.xml.sax.SAXException;

import javax.xml.transform.sax.TransformerHandler;

import com.virtualightning.dlna.SoapCommand;
import com.virtualightning.dlna.constant.DeviceType;

public class PauseCommand extends SoapCommand {
    private final int instanceId;

    public PauseCommand(int instanceId) {
        super(DeviceType.AV_TRANSPORT, "Pause");
        this.instanceId = instanceId;
    }

    @Override
    protected void writeCommand(TransformerHandler handler) throws SAXException {
        simpleElement("InstanceID",String.valueOf(instanceId));
    }
}
