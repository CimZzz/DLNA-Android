package com.virtualightning.dlna.soap;

import org.xml.sax.SAXException;

import javax.xml.transform.sax.TransformerHandler;

import com.virtualightning.dlna.SoapCommand;
import com.virtualightning.dlna.constant.DeviceType;
import com.virtualightning.dlna.tools.TimeUtils;

public class SeekCommand extends SoapCommand {
    private final int instanceId;
    private final long position;

    public SeekCommand(int instanceId, long position) {
        super(DeviceType.AV_TRANSPORT, "Seek");
        this.instanceId = instanceId;
        this.position = position;
    }

    @Override
    protected void writeCommand(TransformerHandler handler) throws SAXException {
        simpleElement("InstanceID",String.valueOf(instanceId));
        simpleElement("Unit","REL_TIME");
        simpleElement("Target",TimeUtils.millis2Str(position));
    }
}
