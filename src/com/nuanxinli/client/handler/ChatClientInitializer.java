package com.nuanxinli.client.handler;


import com.nuanxinli.client.listener.ChatServiceListener;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * Created by YangGuoShan on 16/7/5 09:47.
 * Describe:
 */
public class ChatClientInitializer extends ChannelInitializer<Channel> {

    private ChatServiceListener chatistener;

    public ChatClientInitializer(ChatServiceListener chatistener) {
        this.chatistener = chatistener;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast("decoder", new StringDecoder());
        pipeline.addLast("encoder", new StringEncoder());
        pipeline.addLast("handler", new ChatClientHandler(chatistener));
    }
}
