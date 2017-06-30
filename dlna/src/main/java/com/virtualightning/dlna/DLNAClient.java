package com.virtualightning.dlna;

import java.io.File;

import com.virtualightning.dlna.factory.ThreadPoolFactory;
import com.virtualightning.dlna.interfaces.callback.*;
import com.virtualightning.dlna.interfaces.option.DeviceFilter;
import com.virtualightning.dlna.interfaces.option.InetAddressGetter;
import com.virtualightning.dlna.interfaces.option.XmlDecoder;

public class DLNAClient {
    private static final int DEFAULT_HTTP_PORT = 9090;//Default HTTP Server Port
    private static final int DEFAULT_UNICAST_PORT = 1234;//Default Unicast Port;

    private static final int FLAG_START_COMPLETED = 0;
    private static final int FLAG_START_FAILED = 1;

    private static final int STATE_CLOSE = 0;//关闭状态
    private static final int STATE_START = 1;//开启状态，尚未启动成功
    private static final int STATE_START_COMPLETED = 2;//开启状态，启动成功

    /* DLNAClient 内部参数 */
    private boolean isRelease;//是否已经释放（释放的 DLNAClient 无法再次启动）
    private int state;//运行状态

    /* DLNAClient 必要组件 */
    private HTTPServer httpServer;//HTTP服务器
    private UnicastServer unicastServer;//单播服务器
    private MulticastServer multicastServer;//多播服务器
    private DLNAContext dlnaContext;//DLNA 上下文

    /* DLNAClient 可选参数 */
    private int httpPort;//HTTP服务器监听端口
    private int unicastPort;//单播服务器监听端口
    ThreadPoolFactory threadPoolFactory;//线程池创建工厂
    DeviceFilter deviceFilter;//设备过滤器
    XmlDecoder<SubscribeEvent> subscribeEventXmlDecoder;//自定义订阅事件XML Decoder
    XmlDecoder<DeviceInfo> devDescDocXmlDecoder;//DDD文档XML Decoder
    XmlDecoder<Service> servDescDocXmlDecoder;//SDD文档XML Decoder
    InetAddressGetter inetAddressGetter;//获取地址方式接口

    /* DLNAClient 外部接口 */
    private OnBootstrapCompletedListener onBootstrapCompletedListener;//启动完成回调接口
    private OnFindDeviceListener onFindDeviceListener;//发现新设备回调接口
    private OnDeviceQuitListener onDeviceQuitListener;//设备注销回调接口
    private OnResourceRouteListener onResourceRouteListener;//资源路由回调接口
    private OnServiceInfoListener onServiceInfoListener;//获取服务详细信息回调接口
    private OnErrorListener onErrorListener;//错误信息回调接口
    private OnSubscribeEventListener onSubscribeEventListener;//订阅信息回调接口
    private OnCommandExecListener onCommandExecListener;//命令执行回调接口

    /* DLNAClient 内部使用接口 */

    private TaskStatistic.ICompletedListener bootStrapStatisticListener = new TaskStatistic.ICompletedListener() {
        @Override
        public void onTaskCompleted(int flag) {
            switch (flag) {
                case FLAG_START_COMPLETED:
                    synchronized (this) {
                        if(state == STATE_CLOSE)
                            return;
                        state = STATE_START_COMPLETED;
                    }
                    if(onBootstrapCompletedListener != null)
                        onBootstrapCompletedListener.onBootstrapCompleted(DLNAClient.this);
                    break;
                case FLAG_START_FAILED:
                    closeDLNA();
                    break;
            }
        }
    };

    /* DLNAClient 构造方法 */

    private DLNAClient() {
        state = STATE_CLOSE;
        httpPort = DEFAULT_HTTP_PORT;
        unicastPort = DEFAULT_UNICAST_PORT;
        isRelease = false;
    }

    /* DLNAClient 生命周期方法 */

    public void startDLNA() {
        synchronized (this) {
            if(isRelease)
                return;
            if(state != STATE_CLOSE)
                return;
            state = STATE_START;

            final TaskStatistic bootStrapStatistic = new TaskStatistic(
                    bootStrapStatisticListener
                    ,4
                    ,FLAG_START_COMPLETED
                    ,FLAG_START_FAILED);


            dlnaContext = new DLNAContext(this);
            httpServer = new HTTPServer(dlnaContext,httpPort);
            unicastServer = new UnicastServer(dlnaContext,unicastPort);
            multicastServer = new MulticastServer(dlnaContext);
            dlnaContext.init(bootStrapStatistic);
            httpServer.startServer(bootStrapStatistic);
            unicastServer.startServer(bootStrapStatistic);
            multicastServer.startServer(bootStrapStatistic);
        }
    }

    public void closeDLNA() {
        synchronized (this) {
            if(state == STATE_CLOSE)
                return;
            state = STATE_CLOSE;

            new Thread(new Runnable() {
                HTTPServer httpServer = DLNAClient.this.httpServer;
                UnicastServer unicastServer = DLNAClient.this.unicastServer;
                MulticastServer multicastServer = DLNAClient.this.multicastServer;
                DLNAContext dlnaContext = DLNAClient.this.dlnaContext;

                @Override
                public void run() {
                    if(dlnaContext != null)
                        dlnaContext.clear();
                    if(unicastServer != null)
                        unicastServer.closeServer();
                    if(httpServer != null)
                        httpServer.closeServer();
                    if(multicastServer != null)
                        multicastServer.closeServer();
                }
            }).start();

            unicastServer = null;
            httpServer = null;
            multicastServer = null;
            dlnaContext = null;
        }
    }

    public void release() {
        synchronized (this) {
            closeDLNA();
            clearInterfaces();
            isRelease = true;
        }
    }

    private void clearInterfaces() {
        onBootstrapCompletedListener = null;
        onDeviceQuitListener = null;
        onErrorListener = null;
        onFindDeviceListener = null;
        onResourceRouteListener = null;
        onServiceInfoListener = null;
        onSubscribeEventListener = null;

        threadPoolFactory = null;
        inetAddressGetter = null;
        deviceFilter = null;
        subscribeEventXmlDecoder = null;
        devDescDocXmlDecoder = null;
        servDescDocXmlDecoder = null;
    }

    /* 外部方法 */

    //获得本机的网络地址
    public String getHostAddr() {
        synchronized (this) {
            if(state < STATE_START_COMPLETED)
                return null;
        }

        return dlnaContext.hostAddr;
    }

    //获取HTTP服务器监听端口
    public int getHTTPPort() {
        synchronized (this) {
            if(state < STATE_START_COMPLETED)
                return -1;
        }

        return httpPort;
    }

    //搜索设备方法
    public void search(final String deviceType) {
        synchronized (this) {
            if(state < STATE_START_COMPLETED)
                return;
        }

        dlnaContext.execute(new Runnable() {
            @Override
            public void run() {
                if(unicastServer != null)
                    unicastServer.search(deviceType);
            }
        });
    }

    //对指定设备执行操作
    public void executeCommand(Service service, SoapCommand soapCommand) {
        synchronized (this) {
            if(state < STATE_START_COMPLETED)
                return;
        }

        dlnaContext.executeCommand(service,soapCommand);
    }

    //获取服务详细信息
    public void findServiceInfo(Service service) {
        synchronized (this) {
            if(state < STATE_START_COMPLETED)
                return;
        }

        dlnaContext.findServiceInfo(service);
    }

    //订阅服务
    public void subscribe(Service service) {
        synchronized (this) {
            if(state < STATE_START_COMPLETED)
                return;
        }

        dlnaContext.subscribe(service);
    }

    //取消订阅服务
    public void cancelSubscribe(final Service service) {
        synchronized (this) {
            if(state < STATE_START_COMPLETED)
                return;
        }

        dlnaContext.cancelSubscribe(service);
    }


    /* 内部回调方法 */

    void findNewDevice(DeviceInfo deviceInfo) {
        synchronized (this) {
            if(state < STATE_START_COMPLETED)
                return;
        }

        if(onFindDeviceListener != null)
            onFindDeviceListener.onFindDevice(deviceInfo);
    }

    void deviceQuit(DeviceInfo deviceInfo) {
        synchronized (this) {
            if(state < STATE_START_COMPLETED)
                return;
        }

        if(onDeviceQuitListener != null)
            onDeviceQuitListener.onDeviceQuit(deviceInfo);
    }

    File getResourcesFile(String substring) {
        synchronized (this) {
            if(state < STATE_START_COMPLETED)
                return null;
        }

        if(onResourceRouteListener != null)
            return onResourceRouteListener.onResourceRoute(substring);

        return null;
    }

    void findServiceInfoComplete(Service service) {
        synchronized (this) {
            if(state < STATE_START_COMPLETED)
                return;
        }

        if(onServiceInfoListener != null)
            onServiceInfoListener.onServiceInfo(service);
    }

    void subscribeEvent(Service service, SubscribeEvent event) {
        synchronized (this) {
            if(state < STATE_START_COMPLETED)
                return;
        }

        if(onSubscribeEventListener != null)
            onSubscribeEventListener.onSubscribeEvent(service,event);
    }

    void error(int errorCode,Object... args) {
        synchronized (this) {
            if(state == STATE_CLOSE)
                return;
        }

        if(onErrorListener != null)
            onErrorListener.onError(errorCode,args);
    }

    void commandExecResult(Service service, SoapCommand cmd, boolean isSuccess, String msg) {
        synchronized (this) {
            if(state < STATE_START_COMPLETED)
                return;
        }

        if(onCommandExecListener != null)
            onCommandExecListener.onCommandExec(service,cmd,isSuccess,msg);
    }


    /* 设置接口方法 */

    public void setOnBootstrapCompletedListener(OnBootstrapCompletedListener onBootstrapCompletedListener) {
        this.onBootstrapCompletedListener = onBootstrapCompletedListener;
    }

    public void setOnFindDeviceListener(OnFindDeviceListener onFindDeviceListener) {
        this.onFindDeviceListener = onFindDeviceListener;
    }

    public void setOnDeviceQuitListener(OnDeviceQuitListener onDeviceQuitListener) {
        this.onDeviceQuitListener = onDeviceQuitListener;
    }

    public void setOnResourceRouteListener(OnResourceRouteListener onResourceRouteListener) {
        this.onResourceRouteListener = onResourceRouteListener;
    }

    public void setOnServiceInfoListener(OnServiceInfoListener onServiceInfoListener) {
        this.onServiceInfoListener = onServiceInfoListener;
    }

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    public void setOnSubscribeEventListener(OnSubscribeEventListener onSubscribeEventListener) {
        this.onSubscribeEventListener = onSubscribeEventListener;
    }

    public void setOnCommandExecListener(OnCommandExecListener onCommandExecListener) {
        this.onCommandExecListener = onCommandExecListener;
    }
    /* Builder */

    public static class Builder {
        private DLNAClient client;
        public Builder() {
            client = new DLNAClient();
        }

        public Builder httpPort(int port) {
            client.httpPort = port;
            return this;
        }

        public Builder unicastPort(int port) {
            client.unicastPort = port;
            return this;
        }

        public Builder threadPoolFactory(ThreadPoolFactory threadPoolFactory) {
            client.threadPoolFactory = threadPoolFactory;
            return this;
        }

        public Builder customSubEventDecoder(XmlDecoder<SubscribeEvent> subscribeEventXmlDecoder) {
            client.subscribeEventXmlDecoder = subscribeEventXmlDecoder;
            return this;
        }

        public Builder customDeviceFilter(DeviceFilter deviceFilter) {
            client.deviceFilter = deviceFilter;
            return this;
        }

        public Builder customInetAddressGetter(InetAddressGetter inetAddressGetter) {
            client.inetAddressGetter = inetAddressGetter;
            return this;
        }

        public DLNAClient build() {
            return client;
        }
    }
}
