package com.uushixun.client.handler;



import com.uushixun.client.IMClient;
import com.uushixun.client.listener.ChatServiceListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * Created by YangGuoShan on 16/7/5 09:34.
 * Describe:
 */
public class ChatClientHandler extends SimpleChannelInboundHandler<String> {

    private ChatServiceListener chatistener;
    public ChatClientHandler(ChatServiceListener chatistener){
        this.chatistener = chatistener;
    }
    
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		IMClient.getInstance().setConnectStatus(true);
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		IMClient.getInstance().setConnectStatus(false);
		chatistener.onServiceStatusConnectChanged(ChatServiceListener.STATUS_CONNECT_CLOSED);
		IMClient.getInstance().reconnect();
	}
    
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) {
        if (msg == null || msg.equals("") || msg.equals("null")){
            return;
        }
		if (!msg.equals("Service-Ping")){
			chatistener.onMessageResponse(msg);
		}
    }

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.READER_IDLE){
				ctx.close();
			}else if (event.state() == IdleState.WRITER_IDLE){
				try{
					ctx.channel().writeAndFlush("Chilent-Ping\r\n");
				} catch (Exception e){}
			}
		}
		super.userEventTriggered(ctx, evt);
	}
}
