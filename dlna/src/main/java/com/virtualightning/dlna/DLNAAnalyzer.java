package com.virtualightning.dlna;

import java.io.IOException;
import java.io.InputStream;

import com.virtualightning.dlna.tools.BoundedInputStream;

public class DLNAAnalyzer {

    static String analyzeXMLFromStream(InputStream stream, long length) throws IOException {
        BoundedInputStream boundedInputStream = new BoundedInputStream(stream,length);
        StringBuilder stringBuilder = new StringBuilder();
        byte[] buffer = new byte[1024];
        int readLength;
        while ((readLength = boundedInputStream.read(buffer)) != -1)
            stringBuilder.append(new String(buffer, 0, readLength));

        return stringBuilder.toString().replace("&gt;",">").replace("&lt;","<");
    }

    static DeviceInfo analyzeDeviceInfo(HTTPHeader httpHeader) {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setDeviceType(httpHeader.otherHeaders.get("NT"));
        if(deviceInfo.getDeviceType() == null)
            deviceInfo.setDeviceType(httpHeader.otherHeaders.get("ST"));
        deviceInfo.setUSN(httpHeader.otherHeaders.get("USN"));
        deviceInfo.setLocation(httpHeader.otherHeaders.get("LOCATION"));
        deviceInfo.setServer(httpHeader.otherHeaders.get("SERVER"));
        deviceInfo.setLastActiveTime(System.currentTimeMillis());

        String cacheControl = httpHeader.otherHeaders.get("CACHE-CONTROL");
        if(cacheControl != null)
            deviceInfo.setCacheTime(Long.parseLong(cacheControl.substring(cacheControl.indexOf("=") + 1,cacheControl.length())) * 1000);

        int i = 3;
        int index = 0;
        while (i -- > 0)
            index = deviceInfo.getLocation().indexOf('/',index == 0 ? index : index + 1);

        deviceInfo.setHost(deviceInfo.getLocation().substring(0,index));

        String hostIP = deviceInfo.getHost().substring(deviceInfo.getHost().lastIndexOf('/') + 1, deviceInfo.getHost().length());
        String hostArr[] = hostIP.split(":");
        deviceInfo.setIP(hostArr[0]);
        deviceInfo.setPort(hostArr.length == 1 ? 80 : Integer.parseInt(hostArr[1]));
        return deviceInfo;
    }
}
