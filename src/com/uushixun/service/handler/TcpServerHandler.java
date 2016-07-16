package com.uushixun.service.handler;

import com.uushixun.service.TcpChatServer;
import com.uushixun.service.listener.ChatServiceListener;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

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
		serviceListener.onClientStatusConnectChanged(ChatServiceListener.STATUS_CONNECT_ACTIVE, channel);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		TcpChatServer.getInstance().getAllChannels().remove(channel);
		serviceListener.onClientStatusConnectChanged(ChatServiceListener.STATUS_CONNECT_INACTIVE, channel);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		if (msg.equals("Chilent-Ping")) {
			ctx.channel().writeAndFlush("Service-Ping\r\n");
		} else {
			serviceListener.onMessageRequest(msg, ctx.channel());
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println(cause.getMessage());
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		Channel channel = ctx.channel();
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state().equals(IdleState.READER_IDLE)) {
				TcpChatServer.getInstance().getAllChannels().remove(channel);
				channel.close();
			}
		}
		super.userEventTriggered(ctx, evt);
	}
}
