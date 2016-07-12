package com.nuanxinli.client.mode;

/**
 * Created by YangGuoShan on 16/7/4 18:21.
 * Describe:
 */
public class SocketConfig {
    public String host;
    public int port;
    public int reconnectionDelay = 10000;//重连间隔
    public int reconnectionMaxTimes = 5;//重连次数

    
}
