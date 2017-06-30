package com.virtualightning.dlna.soap;

import org.xml.sax.SAXException;

import javax.xml.transform.sax.TransformerHandler;

import com.virtualightning.dlna.SoapCommand;
import com.virtualightning.dlna.constant.DeviceType;

public class PlayCommand extends SoapCommand {
    private final int instanceId;
    private final int speed;

    public PlayCommand(int instanceId) {
        this(instanceId,1);
    }

    public PlayCommand(int instanceId, int speed) {
        super(DeviceType.AV_TRANSPORT, "Play");
        this.instanceId = instanceId;
        this.speed = speed;
    }

    @Override
    protected void writeCommand(TransformerHandler handler) throws SAXException {
        simpleElement("InstanceID",String.valueOf(instanceId));
        simpleElement("Speed",String.valueOf(speed));
    }
}
