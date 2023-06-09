package io.netty.incubator.codec.quic;

import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLSession;

/**
 * QuicConnectManager
 * @author wuhao
 * @createTime 2023-06-01
 */
public class QuicConnectManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuicConnectManager.class);
    public static void  closeSsl(QuicChannel quicChannel){
        try {
            if (quicChannel == null || quicChannel.sslEngine() == null){
                return;
            }
//            QuicheQuicSslEngine quicSslEngine = (QuicheQuicSslEngine) quicChannel.sslEngine();
//            SSLSession session = quicChannel.sslEngine().getSession();
//            session.invalidate();
//            quicSslEngine.ctx.finalize();
            quicChannel.close(true, 0, Unpooled.EMPTY_BUFFER);

        } catch (Throwable e){
            LOGGER.warn("closeSsl Error:{}", e.getMessage());
        }
    }
}
