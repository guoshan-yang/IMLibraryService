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
					System.out.println(channel.remoteAddress()+" : "+msg);
				}
				
				@Override
				public void onClientStatusConnectChanged(int statusCode, Channel channel) {	//连接状态监听
					if (statusCode == ChatServiceListener.STATUS_CONNECT_ACTIVE) {
						tcpChatServer.pushToSingleDevice("Welcome To "+channel.remoteAddress(), channel);
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
