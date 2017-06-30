package com.virtualightning.dlna.interfaces.option;

import com.virtualightning.dlna.DeviceInfo;

public interface DeviceFilter {
    boolean isNeedDeviceType(DeviceInfo deviceInfo, boolean isResolved);
}
