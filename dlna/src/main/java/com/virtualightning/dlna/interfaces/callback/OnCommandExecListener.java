package com.virtualightning.dlna.interfaces.callback;

import com.virtualightning.dlna.Service;
import com.virtualightning.dlna.SoapCommand;

public interface OnCommandExecListener {
    void onCommandExec(Service service, SoapCommand cmd, boolean isSuccess, String responseStr);
}
