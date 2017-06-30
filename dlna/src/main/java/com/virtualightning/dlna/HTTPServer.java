package com.virtualightning.dlna;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import com.virtualightning.dlna.constant.ErrorCode;
import com.virtualightning.dlna.interfaces.option.XmlDecoder;

/**
 * Created by CimZzz on 17/5/31.<br>
 * Project Name : Hunban.com Education<br>
 * Since : Education_0.0.1<br>
 * Description:<br>
 * Description
 */
public class HTTPServer extends BaseServer {
    private ServerSocket socket;
    private int port;

    HTTPServer(DLNAContext dlnaContext, int port) {
        super(dlnaContext);
        this.port = port;
    }

    @Override
    protected void onStart() throws IOException {
        socket = new ServerSocket(port);
    }

    @Override
    protected void onLoop(){
        final Socket connSocket;
        try {
            connSocket = socket.accept();
            dlnaContext.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        handleSocket(connSocket);
                    } catch (Exception e) {
                        dlnaContext.error(ErrorCode.HTTP_RES_ERROR,e);
                    }
                    finally {
                        try {if(!connSocket.isClosed())connSocket.close();} catch (IOException e) {}
                    }
                }
            });
        } catch (IOException e) {
            dlnaContext.error(ErrorCode.HTTP_RECV_CON_ERROR,e);
        }
    }

    @Override
    protected void onClose() {
        try {
            socket.close();
        } catch (IOException e) {}
        socket = null;
    }


    private void handleSocket(Socket socket) throws Exception {
        InputStream socketInput = socket.getInputStream();
        HTTPHeader params = HTTPHeader.analyzeParams(socketInput,true);
        /*
         * 如果请求报文中包含Content-Length字段，则判断为订阅信息
         */
        if(params.contentLength != null) {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            String sid = params.otherHeaders.get("SID").trim();
            Service service = dlnaContext.findServiceInfoBySID(sid);

            if(service != null) {
                long seq = Long.parseLong(params.otherHeaders.get("SEQ").trim());
                //如果订阅号有效，解析XML文档，否则略过
                String xml = DLNAAnalyzer.analyzeXMLFromStream(socketInput,params.contentLength);
                //解析xml文件
                if(xml != null) {
                    System.out.println(xml);
                    SubscribeEvent event = new SubscribeEvent();
                    event.seq = seq;
                    event.actionId = SubscribeEvent.ACTION_SUBSCRIBE_RESPONSE;
                    event.feature = new HashMap<>();
                    XmlDecoder<SubscribeEvent> xmlDecoder = dlnaContext.getSubscribeEventXmlDecoder();
                    if(xmlDecoder == null)
                        xmlDecoder = new XmlEventHandler(event);
                    if(xmlDecoder.decoderXMLStream(event,new ByteArrayInputStream(xml.getBytes())))
                        //解析xml成功，发送订阅事件
                        dlnaContext.subscribeEvent(service,event);
                }
                dataOutputStream.writeBytes("HTTP/1.1 200 STATUS_OK\r\n\r\n");
            } else dataOutputStream.writeBytes("HTTP/1.1 404 NOT_FOUND\r\n\r\n");

            dataOutputStream.flush();
            socket.close();
        }
        /*
         * 否则，则认为是访问本地资源
         */
        else {
            System.out.println("Range : " + params.rangeStart + " , " + params.rangeEnd);
            System.out.println(params.otherHeaders);
            File file = dlnaContext.getResourcesFile(params.methodPath.substring(1,params.methodPath.length()));

            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeBytes("HTTP/1.0 200 STATUS_OK\r\n");
            if(file != null) {
                long fileLength = file.length();
                FileInputStream inputStream = new FileInputStream(file);
                if(params.rangeStart != -1L) {
                    skipCount(inputStream, params.rangeStart);
                    fileLength -= params.rangeStart;
                }
                outputStream.writeBytes("Content-Length: "+fileLength+"\r\n");
                outputStream.writeBytes("Accept-Ranges: bytes\r\n");
                outputStream.writeBytes("\r\n");
                byte[] buffer = new byte[1024];
                int readLength;

                while ((readLength = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer,0,readLength);
                    outputStream.flush();
                }

                inputStream.close();
            }
            System.out.println("响应完毕");
        }
    }

    private void skipCount(InputStream inputStream,long size) throws IOException {
        while (size != 0) {
            size -= inputStream.skip(size);
        }
    }
}
