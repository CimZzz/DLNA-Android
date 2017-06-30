package com.virtualightning.dlna.constant;

public interface ErrorCode {
    int HTTP_RES_ERROR = 0;//HTTP服务器响应异常
    int HTTP_RECV_CON_ERROR = 1;//HTTP服务器接收连接异常

    int SERVER_BOOTSTRAP_ERROR = 100;//服务启动异常

    int MULTICAST_RECV_ERROR = 200;//多播接收异常

    int UNICAST_RECV_ERROR = 300;//单播接收异常
    int UNICAST_SEARCH_ERROR = 301;//单播搜索异常

    int CON_PAUSE_ERROR = 402;//暂停播放请求连接异常
    int CON_PLAY_ERROR = 403;//播放请求连接异常
    int CON_STOP_ERROR = 404;//停止播放请求连接异常
    int CON_SET_URI_ERROR = 405;//设置播放资源请求连接异常
    int CON_SEEK_ERROR = 406;//定位播放位置请求连接异常

    int SERVER_INFO_FAILED = 500;//获取服务详细信息失败
    int DEVICE_INFO_DECODE_FAILED = 600;//解析设备文档失败
}
