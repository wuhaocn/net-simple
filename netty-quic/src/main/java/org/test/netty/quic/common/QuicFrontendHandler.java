package org.test.netty.quic.common;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.incubator.codec.quic.QuicChannel;
import io.netty.incubator.codec.quic.QuicStreamChannel;


/**
 * QuicFrontendHandler
 * 
 * @author wuhao
 */
public class QuicFrontendHandler extends SimpleChannelInboundHandler<Object> {
    private QuicServer quicServer;

    public QuicFrontendHandler(QuicServer quicServer) {
        this.quicServer = quicServer;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.read();
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    }
    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("channelRead0" + ctx.channel().remoteAddress());
        if (msg == null) {
            return;
        }
        QuicChannel parent = ((QuicStreamChannel) ctx.channel()).parent();
		parent.close(true, 0, Unpooled.EMPTY_BUFFER);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        QuicChannel parent = ((QuicStreamChannel) ctx.channel()).parent();
        parent.close(true, 0, Unpooled.EMPTY_BUFFER);
    }
}
