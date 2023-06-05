package org.test.netty.quic.common;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.incubator.codec.quic.*;
import io.netty.util.CharsetUtil;
import io.netty.util.NetUtil;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * QuicClient
 *
 * @author wuhao
 */
public class QuicClient {

	public void runClient() throws Exception {
		QuicSslContext context = QuicSslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).
				applicationProtocols("http/0.9").build();
		NioEventLoopGroup group = new NioEventLoopGroup(1);
		try {
			System.out.println("runClient Start");
			ChannelHandler codec = new QuicClientCodecBuilder()
					.sslContext(context)
					.maxIdleTimeout(5000, TimeUnit.MILLISECONDS)
					.initialMaxData(10000000)
					.initialMaxStreamDataBidirectionalLocal(1000000)
					.build();

			Bootstrap bs = new Bootstrap();
			Channel channel = bs.group(group)
					.channel(NioDatagramChannel.class)
					.handler(codec)
					.bind(0).sync().channel();

			QuicChannel quicChannel = QuicChannel.newBootstrap(channel)
					.streamHandler(new ChannelInboundHandlerAdapter() {
					})
					.remoteAddress(new InetSocketAddress(NetUtil.LOCALHOST4, 9999))
					.connect()
					.get();

			QuicStreamChannel streamChannel = getQuicStreamChannel(quicChannel);
			streamChannel.writeAndFlush(Unpooled.copiedBuffer("GET /\r\n", CharsetUtil.US_ASCII))
					.addListener(QuicStreamChannel.SHUTDOWN_OUTPUT);
			streamChannel.closeFuture().sync();
			quicChannel.closeFuture().sync();
			channel.close().sync();
			System.out.println("runClient Close");
		} finally {
			group.shutdownGracefully();
		}
	}

	public QuicStreamChannel getQuicStreamChannel(QuicChannel quicChannel) throws InterruptedException {
		QuicStreamChannel streamChannel = quicChannel.createStream(QuicStreamType.BIDIRECTIONAL,
				new ChannelInboundHandlerAdapter() {
					@Override
					public void channelRead(ChannelHandlerContext ctx, Object msg) {
						if (msg instanceof ByteBuf) {
							ByteBuf byteBuf = (ByteBuf) msg;
							byteBuf.release();
						}
					}
				}).sync().getNow();
		return streamChannel;
	}
}
