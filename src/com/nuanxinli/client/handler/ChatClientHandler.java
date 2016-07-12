package com.nuanxinli.client.handler;



import com.nuanxinli.client.IMClient;
import com.nuanxinli.client.listener.ChatServiceListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

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
		chatistener.onServiceStatusConnectChanged(ChatServiceListener.STATUS_CONNECT_SUCCESS);
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		IMClient.getInstance().setConnectStatus(false);
		chatistener.onServiceStatusConnectChanged(ChatServiceListener.STATUS_CONNECT_CLOSED);
	}
    
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) {
        if (msg == null || msg.equals("") || msg.equals("null")){
            return;
        }
		chatistener.onMessageResponse(msg);
    }
}
