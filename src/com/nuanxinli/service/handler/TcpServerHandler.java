package com.nuanxinli.service.handler;

import com.nuanxinli.service.TcpChatServer;
import com.nuanxinli.service.listener.ChatServiceListener;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;

/**
 * netty handler
 * <p>
 * Created by Tony on 4/13/16.
 */
@Sharable
public class TcpServerHandler extends SimpleChannelInboundHandler<String> {
	
	private ChatServiceListener serviceListener;
	
	public TcpServerHandler(ChatServiceListener serviceListener) {
		super();
		this.serviceListener = serviceListener;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		TcpChatServer.getInstance().getAllChannels().add(channel);
		serviceListener.onClientStatusConnectChanged(ChatServiceListener.STATUS_CONNECT_ACTIVE,channel);
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		TcpChatServer.getInstance().getAllChannels().remove(channel);
		serviceListener.onClientStatusConnectChanged(ChatServiceListener.STATUS_CONNECT_INACTIVE,channel);
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		serviceListener.onMessageRequest(msg, ctx.channel());
	}
	
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getMessage());
    }
}
