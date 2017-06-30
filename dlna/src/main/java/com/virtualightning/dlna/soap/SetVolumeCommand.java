package com.virtualightning.dlna.soap;

import com.virtualightning.dlna.SoapCommand;
import com.virtualightning.dlna.constant.DeviceType;
import org.xml.sax.SAXException;

import javax.xml.transform.sax.TransformerHandler;

public class SetVolumeCommand extends SoapCommand {
    private final int instanceId;
    private final int volume;

    public SetVolumeCommand(int instanceId, int volume) {
        super(DeviceType.RENDERING_CONTROL, "SetVolume");
        this.instanceId = instanceId;
        this.volume = volume;
    }

    @Override
    protected void writeCommand(TransformerHandler handler) throws SAXException {
        simpleElement("InstanceID",String.valueOf(instanceId));
        simpleElement("Channel","Master");
        simpleElement("DesiredVolume",String.valueOf(volume));
    }
}
