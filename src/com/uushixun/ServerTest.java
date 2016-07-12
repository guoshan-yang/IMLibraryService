package com.uushixun;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;

import com.uushixun.service.TcpChatServer;
import com.uushixun.service.handler.AttributeKeys;
import com.uushixun.service.listener.ChatServiceListener;

import io.netty.channel.Channel;

public class ServerTest {
	
	private static TcpChatServer tcpChatServer = TcpChatServer.getInstance();
	
	public static void main(String[] args) {
		try {
			
			tcpChatServer.setChatServerListenter(new ChatServiceListener() {
				
				@Override
				public void onMessageRequest(String msg, Channel channel) {		//消息监听
					
//					tcpChatServer.setChannelFlag(msg, channel);//这是一个userId,用于定点推送消息的唯一标示,也可以是token之类的
					
					System.out.println(channel.remoteAddress()+" : "+msg);
					
					tcpChatServer.pushToSingleDevice("Server Receive:"+msg, msg);//定点推送消息,参数1:消息体 参数2:设备唯一标示
				}
				
				@Override
				public void onClientStatusConnectChanged(int statusCode, Channel channel) {	//连接状态监听
					
					
					
					if (statusCode == ChatServiceListener.STATUS_CONNECT_ACTIVE) {
						tcpChatServer.pushToAllDevice("Welcome To "+channel.remoteAddress());
					}
					System.out.println(channel.remoteAddress()+" : "+statusCode);
				}
			});
			
			tcpChatServer.start();//开启服务
			
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				tcpChatServer.pushToAllDevice(in.readLine());//推送消息到所以设备
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
