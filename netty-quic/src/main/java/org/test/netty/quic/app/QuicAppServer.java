package org.test.netty.quic.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test.netty.quic.common.LoggerConfiguration;
import org.test.netty.quic.common.QuicServer;

import java.security.cert.CertificateException;

/**
 * QuicServer
 *
 * @author wuhao
 */
public class QuicAppServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuicAppServer.class);

    public static void main(String[] args) throws CertificateException, InterruptedException {
        int port = 9999;
        LoggerConfiguration loggerConfiguration = new LoggerConfiguration();
        QuicServer quicServer = new QuicServer(port);
        quicServer.run();
        LOGGER.info("QuicAppServer Start:{}", port);
    }
}
