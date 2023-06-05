package org.test.netty.quic.common;

import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.incubator.codec.quic.QuicStreamChannel;

/**
 * QuicStreamInitializer
 * 
 * @author wuhao
 */
public class QuicStreamInitializer extends ChannelInitializer<QuicStreamChannel> {

    @Override
    protected void initChannel(QuicStreamChannel ch) {
        ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
        ch.pipeline().addLast(new QuicFrontendHandler());
    }
}
