package com.nuanxinli.service;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import com.nuanxinli.service.handler.AttributeKeys;
import com.nuanxinli.service.initializer.TcpServerInitializer;
import com.nuanxinli.service.listener.ChatServiceListener;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelMatcher;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;

public class TcpChatServer{

    private TcpServerInitializer serverInitializer;
    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workGroup = new NioEventLoopGroup();
    private ChannelGroup allChannels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private ChannelFuture channelFuture;
    private int port = 9090;
    
    private static TcpChatServer chatServer = new TcpChatServer();
    
    public static TcpChatServer getInstance(){
    	return chatServer;
    }
    
    public void setChatServerListenter(ChatServiceListener serviceListener){
    	this.serverInitializer = new TcpServerInitializer(serviceListener);
    }
    
    public void setPort(int port){
    	this.port = port;
    }
    
	public void start() throws Exception {
		
		try {
            ServerBootstrap b = new ServerBootstrap()
                    .group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(serverInitializer);

            channelFuture = b.bind(port).sync();
            System.out.println("start server port "+port);
        } finally {
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    shutdown();
                }
            });
        }
	}

    public void restart() throws Exception {
        shutdown();
        start();
    }

    public void shutdown() {
        if (channelFuture != null) {
            channelFuture.channel().close().syncUninterruptibly();
        }
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workGroup != null) {
            workGroup.shutdownGracefully();
        }
    }
    
    public ChannelGroup getAllChannels() {
        return allChannels;
    }
    
    public void pushToSingleDevice(String text, final String userId) {
    	allChannels.writeAndFlush(text + "\r\n", new ChannelMatcher() {
			public boolean matches(Channel channel) {
				return userId.equals(channel.attr(AttributeKeys.UserId).get());
			}
		});
    }
    
    public void pushToAllDevice(String text) {
    	Iterator<Channel> iterator = allChannels.iterator();
		while (iterator.hasNext()) {
			iterator.next().writeAndFlush(text + "\r\n");
		}
    }
    
    public void setChannelFlag(String userId, Channel channel){
    	channel.attr(AttributeKeys.UserId).set(userId);
    }
    
    public String getChannelFlag(Channel channel){
    	return channel.attr(AttributeKeys.UserId).get();
    }
}
