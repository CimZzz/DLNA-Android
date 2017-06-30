package com.virtualightning.dlna.interfaces.option;

import com.virtualightning.dlna.DeviceInfo;
import com.virtualightning.dlna.HTTPHeader;

public interface DeviceInfoDecoder {
    void deviceInfoDecode(DeviceInfo deviceInfo, HTTPHeader httpHeader);
}
