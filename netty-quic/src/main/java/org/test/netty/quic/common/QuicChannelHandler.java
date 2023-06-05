package org.test.netty.quic.common;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.incubator.codec.quic.QuicChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * QuicChannelHandler
 * 
 */
public class QuicChannelHandler extends ChannelInboundHandlerAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(QuicChannelHandler.class);
	public QuicChannelHandler() {
	}
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		LOGGER.info("channelActive:{}", ctx.channel().remoteAddress());
		ctx.fireChannelActive();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		
		try {
			LOGGER.info("channelInactive:{}", ctx.channel().remoteAddress());
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
