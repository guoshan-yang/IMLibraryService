package com.nuanxinli.service.handler;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelMatcher;

public class VinChannelMatcher implements ChannelMatcher {
    private final String vin;

    public VinChannelMatcher(String vin) {
        this.vin = vin;
    }

    @Override
    public boolean matches(Channel channel) {
        return vin.equals(channel.attr(AttributeKeys.UserId).get());
    }
}
