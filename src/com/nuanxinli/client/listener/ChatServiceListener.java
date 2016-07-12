package com.nuanxinli.client.listener;


/**
 * Created by YangGuoShan on 16/7/4 18:22.
 * Describe:
 */
public interface ChatServiceListener {

    public final static byte STATUS_CONNECT_SUCCESS = 1;

    public final static byte STATUS_CONNECT_CLOSED = 0;

    public final static byte STATUS_CONNECT_ERROR = 0;


    /**
     * 当接收到系统消息
     *
     */
    public void onMessageResponse(String msg);

    /**
     * 当服务状态发生变化时触发
     *
     */
    public void onServiceStatusConnectChanged(int statusCode);
}
