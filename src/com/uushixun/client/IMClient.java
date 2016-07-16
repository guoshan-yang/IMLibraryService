package com.uushixun.client;

/**
 * Created by YangGuoShan on 16/7/4 19:12.
 * Describe:
 */

import com.uushixun.client.handler.ChatClientInitializer;
import com.uushixun.client.listener.ChatServiceListener;
import com.uushixun.client.mode.SocketConfig;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class IMClient {

	private static IMClient imClient = new IMClient();

	private SocketConfig socketConfig;

	private EventLoopGroup group;

	private ChatServiceListener chatistener;

	private Channel channel;
	
	private boolean isConnect = false;

	private int reconnectNum = Integer.MAX_VALUE;

	private long reconnectIntervalTime = 5000;

	public static IMClient getInstance(SocketConfig config, ChatServiceListener chatistener) {
		imClient.setSocketConfig(config);
		imClient.setChatistener(chatistener);
		return imClient;
	}
	
	public static IMClient getInstance(){
		return imClient;
	}
	
	public synchronized IMClient connect() {
		if (isConnect == false) {
			group = new NioEventLoopGroup();
			Bootstrap bootstrap = new Bootstrap().group(group)
					.option(ChannelOption.SO_KEEPALIVE,true)
					.channel(NioSocketChannel.class)
					.handler(new ChatClientInitializer(chatistener));
			
			try {
				bootstrap.connect(socketConfig.host, socketConfig.port).addListener(new ChannelFutureListener() {
					@Override
					public void operationComplete(ChannelFuture channelFuture) throws Exception {
						if (channelFuture.isSuccess()) {
							isConnect = true;
							channel = channelFuture.channel();
						} else {
							isConnect = false;
						}
					}
				}).sync();

			} catch (Exception e) {
				chatistener.onServiceStatusConnectChanged(ChatServiceListener.STATUS_CONNECT_ERROR);
				reconnect();
			}
		}
		return this;
	}

	public void disconnect() {
		group.shutdownGracefully();
	}

	public void reconnect() {
		if(reconnectNum >0 && !isConnect){
			reconnectNum--;
			try {
				Thread.sleep(reconnectIntervalTime);
			} catch (InterruptedException e) {}
			System.out.println("重新连接");
			disconnect();
			connect();
		}else{
			disconnect();
		}
	}

	public void sendMsgToServer(String msg) {
		channel.writeAndFlush(msg+"\r\n");
	}

	public void sendMsgToServer(String msg,ChannelFutureListener listener){
		channel.writeAndFlush(msg+"\r\n").addListener(listener);
	}

	public void setReconnectNum(int reconnectNum) {
		this.reconnectNum = reconnectNum;
	}

	public void setReconnectIntervalTime(long reconnectIntervalTime) {
		this.reconnectIntervalTime = reconnectIntervalTime;
	}

	private void setSocketConfig(SocketConfig socketConfig) {
		this.socketConfig = socketConfig;
	}
	
	public boolean getConnectStatus(){
		return isConnect;
	}
	
	public void setConnectStatus(boolean status){
		this.isConnect = status;
	}
	
	public void setChatistener(ChatServiceListener chatistener) {
		this.chatistener = chatistener;
	}
}
