package org.test.netty.quic.common;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.incubator.codec.quic.QuicChannel;

/**
 * QuicChannelHandler
 * 
 */
public class QuicChannelHandler extends ChannelInboundHandlerAdapter {
	public QuicChannelHandler() {
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		System.out.println("channelActive" + ctx.channel().remoteAddress());
		ctx.fireChannelActive();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		
		try {
			System.out.println("channelInactive" + ctx.channel().remoteAddress());
			QuicChannel quicChannel = (QuicChannel) ctx.channel();
			quicChannel.collectStats().addListener(f -> {
			});
			quicChannel.close(true, 0, Unpooled.EMPTY_BUFFER);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ctx.fireChannelInactive();
		}
		
	}

	@Override
	public boolean isSharable() {
		return true;
	}

}
