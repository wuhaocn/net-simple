package org.test.netty.quic.client;

import org.test.netty.quic.common.QuicServer;

import java.security.cert.CertificateException;

/**
 * QuicServer
 * @author wuhao
 */
public class QuicAppServer {
	public static void main(String[] args) throws CertificateException, InterruptedException {
		QuicServer quicServer = new QuicServer(9999);
		quicServer.run();
	}
}
