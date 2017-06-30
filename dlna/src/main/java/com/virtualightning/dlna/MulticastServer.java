package com.virtualightning.dlna;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MulticastSocket;

import com.virtualightning.dlna.constant.ErrorCode;
import com.virtualightning.dlna.constant.SSDP;
import com.virtualightning.dlna.tools.BoundedInputStream;

public class MulticastServer extends BaseServer {
    private MulticastSocket socket;

    private byte[] recvBuffer;
    private DatagramPacket receiver;

    MulticastServer(DLNAContext dlnaContext) {
        super(dlnaContext);
    }

    @Override
    protected void onStart() throws IOException {
        socket = new MulticastSocket(1900);
        InetAddress inetAddress = Inet4Address.getByName("239.255.255.250");
        socket.joinGroup(inetAddress);
        recvBuffer = new byte[1024];
        receiver = new DatagramPacket(recvBuffer,recvBuffer.length);
    }

    @Override
    protected void onLoop() {
        try {
            socket.receive(receiver);

            BoundedInputStream inputStream = new BoundedInputStream(
                    new ByteArrayInputStream(receiver.getData()),receiver.getLength()
            );
            HTTPHeader httpHeader = HTTPHeader.analyzeParams(inputStream,"NOTIFY",true);

            if(httpHeader != null) {
                DeviceInfo deviceInfo = DLNAAnalyzer.analyzeDeviceInfo(httpHeader);
                String NTS = httpHeader.otherHeaders.get("NTS");
                if(NTS == null)
                    return;

                NTS = NTS.trim();
                if (NTS.equals(SSDP.ALIVE))
                    dlnaContext.findNewDevice(deviceInfo,true);
                else if(NTS.equals(SSDP.BYEBYE))
                    dlnaContext.deviceQuit(deviceInfo);
            }
        } catch (IOException e) {
            dlnaContext.error(ErrorCode.MULTICAST_RECV_ERROR,e);
        }
    }

    @Override
    protected void onClose() {
        socket.close();
        recvBuffer = null;
        receiver = null;
    }
}
