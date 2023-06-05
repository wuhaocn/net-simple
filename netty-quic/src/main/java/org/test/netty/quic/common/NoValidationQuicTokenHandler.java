package org.test.netty.quic.common;

import io.netty.buffer.ByteBuf;
import io.netty.incubator.codec.quic.QuicTokenHandler;

import java.net.InetSocketAddress;

/**
 * NoValidationQuicTokenHandler
 * 
 */
public final class NoValidationQuicTokenHandler implements QuicTokenHandler {

    private NoValidationQuicTokenHandler() {
    }

    public static final NoValidationQuicTokenHandler INSTANCE = new NoValidationQuicTokenHandler();

    @Override
    public boolean writeToken(ByteBuf out, ByteBuf dcid, InetSocketAddress address) {
        return false;
    }

    @Override
    public int validateToken(ByteBuf token, InetSocketAddress address) {
        return 0;
    }

    @Override
    public int maxTokenLength() {
        return 0;
    }
}
