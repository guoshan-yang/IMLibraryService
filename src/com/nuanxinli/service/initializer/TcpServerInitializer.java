package com.nuanxinli.service.initializer;

import com.nuanxinli.service.handler.TcpServerHandler;
import com.nuanxinli.service.listener.ChatServiceListener;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Created by YangGuoShan on 16/7/4 19:12.
 * Describe:
 */
public class TcpServerInitializer extends ChannelInitializer<SocketChannel> {

    private final StringDecoder DECODER = new StringDecoder();
    private final StringEncoder ENCODER = new StringEncoder();
    private final TcpServerHandler SERVER_HANDLER;
    private int IDLE_TIME_SECONDS = 600;
    
    public TcpServerInitializer(ChatServiceListener serviceListener){
    	SERVER_HANDLER = new TcpServerHandler(serviceListener);
    }
    
    public void init(int idleTimeSeconds) {
        IDLE_TIME_SECONDS = idleTimeSeconds;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new IdleStateHandler(IDLE_TIME_SECONDS,
                IDLE_TIME_SECONDS, IDLE_TIME_SECONDS));
        pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters
                .lineDelimiter()));
        pipeline.addLast(DECODER);
        pipeline.addLast(ENCODER);
        pipeline.addLast(SERVER_HANDLER);
    }
}
