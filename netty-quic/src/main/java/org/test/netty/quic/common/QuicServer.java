package org.test.netty.quic.common;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.incubator.codec.quic.*;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

/**
 * QuicServer
 * @author wuhao
 */
public class QuicServer {
    private final NioEventLoopGroup group = new NioEventLoopGroup(4);
    private final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private Channel channel;
    private final int port;
    private boolean stopping = false;


    public QuicServer(int port) {
        this.port = port;
    }

    public QuicSslContext getSslContext() throws CertificateException {
		SelfSignedCertificate selfSignedCertificate = new SelfSignedCertificate();
        QuicSslContextBuilder quicSslContextBuilder = QuicSslContextBuilder
                .forServer(selfSignedCertificate.privateKey(), null,
					selfSignedCertificate.certificate())
                .applicationProtocols("http/0.9")
                .earlyData(true);
        return quicSslContextBuilder.build();
    }
    

    public void run() throws InterruptedException, CertificateException {

        QuicSslContext context = getSslContext();
        ChannelHandler codec = new QuicServerCodecBuilder()
                .sslContext(context)
                .maxIdleTimeout(60000, TimeUnit.MILLISECONDS)
                .initialMaxData(10000000)
                .initialMaxStreamDataBidirectionalLocal(1000000)
                .initialMaxStreamDataBidirectionalRemote(1000000)
                .initialMaxStreamsBidirectional(100)
                .initialMaxStreamsUnidirectional(100)
                .tokenHandler(NoValidationQuicTokenHandler.INSTANCE)
                .handler(new QuicChannelHandler())
                .streamOption(ChannelOption.AUTO_READ, false)
                .streamHandler(new QuicStreamInitializer(this)).build();
        Bootstrap bs = new Bootstrap();
        channel = bs.group(group)
                .channel(NioDatagramChannel.class)
                .handler(codec)
                .bind(new InetSocketAddress(port))
                .sync().channel();
    }

    public void close() throws InterruptedException {
        stopping = true;
        for (Channel ch : channels) {
            ((QuicChannel) ch).close(true, 127, Unpooled.copiedBuffer("10".getBytes(StandardCharsets.UTF_8)));
        }
        this.channel.closeFuture().sync();
        group.shutdownGracefully();
    }

    public boolean isStopping() {
        return stopping;
    }
}
