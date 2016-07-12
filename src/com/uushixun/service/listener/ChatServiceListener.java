package com.uushixun.service.listener;

import io.netty.channel.Channel;

/**
 * Created by YangGuoShan on 16/7/4 18:22.
 * Describe:
 */
public interface ChatServiceListener {

    public final static byte STATUS_CONNECT_ACTIVE = 1;

    public final static byte STATUS_CONNECT_INACTIVE = 0;


    /**
     * 当接收到系统消息
     *
     */
    public void onMessageRequest(String msg,Channel channel);

    /**
     * 当服务状态发生变化时触发
     *
     */
    public void onClientStatusConnectChanged(int statusCode,Channel channel);
}
