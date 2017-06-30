package com.virtualightning.dlna;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class SimpleHTTPConnection {
    private String method;
    private String subUrl;
    private String ip;
    private int port;
    private final boolean useBuffer;
    private boolean getOutputStream;

    private Socket socket;
    private DataOutputStream realOutputStream;
    private DataOutputStream bufferOutputStream;
    private ByteArrayOutputStream buffer;

    SimpleHTTPConnection(boolean useBuffer) {
        this.useBuffer = useBuffer;
        if(useBuffer) {
            buffer = new ByteArrayOutputStream();
            bufferOutputStream = new DataOutputStream(buffer);
        }
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setSubUrl(String subUrl) {
        this.subUrl = subUrl;
    }

    public void connect(String ip, int port) throws IOException {
        socket = new Socket(ip,port);
        socket.setSoTimeout(5000);
        realOutputStream = new DataOutputStream(socket.getOutputStream());
        realOutputStream.writeBytes(method + " " + subUrl + " HTTP/1.1\r\n");
        if(port == 90)
            realOutputStream.writeBytes("Host: " + ip + "\r\n");
        else
            realOutputStream.writeBytes("Host: " + ip + ":" + port + "\r\n");
        realOutputStream.flush();
    }

    public void addRequestProperty(String key,String value) throws IOException {
        realOutputStream.writeBytes(key+ ": " + value + "\r\n");
        realOutputStream.flush();
    }

    public DataOutputStream getOutputStream() throws IOException{
        if(useBuffer)
            return bufferOutputStream;
        else {
            getOutputStream = true;
            realOutputStream.writeBytes("\r\n");
            return realOutputStream;
        }
    }

    public void outputCompleted() throws IOException{
        if(useBuffer) {
            byte[] bufferArray = buffer.toByteArray();
            realOutputStream.writeBytes("Content-Length: " + bufferArray.length + "\r\n\r\n");
            realOutputStream.write(bufferArray,0,bufferArray.length);
        }
        if(!getOutputStream)
            realOutputStream.writeBytes("\r\n");
        realOutputStream.flush();
    }

    public InputStream getInputStream() throws IOException {
        return socket.getInputStream();
    }

    public void close(){
        try{
            if(!socket.isClosed())
                socket.close();
        } catch (IOException e) {}
        if(useBuffer) {
            try {
                bufferOutputStream.close();
            } catch (IOException e) {}
            try {
                buffer.close();
            } catch (IOException e) {}
        }
    }
}
