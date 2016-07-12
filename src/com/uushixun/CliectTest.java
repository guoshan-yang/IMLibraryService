package com.uushixun;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.uushixun.client.IMClient;
import com.uushixun.client.listener.ChatServiceListener;
import com.uushixun.client.mode.SocketConfig;

public class CliectTest {
	
	public static void main(String[] args) throws Exception {
		
		SocketConfig config = new SocketConfig();
		config.host = "127.0.0.1";
		config.port = 9090;
		
		IMClient client = IMClient.getInstance(config, new ChatServiceListener() {
			
			@Override
			public void onServiceStatusConnectChanged(int statusCode) {		//连接状态监听
				//TODO	连接服务成功之后, 需要向服务器发送login消息, 用于绑定设备
				System.out.println("statusCode :" + statusCode);
			}
			
			@Override
			public void onMessageResponse(String inPacket) {	//消息接收
				System.out.println(inPacket);
			}
		});
		
		client.connect();//连接服务器
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			client.sendMsgToServer(in.readLine());
		}
	}
}
