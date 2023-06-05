package org.test.netty.quic.app;

import org.test.netty.quic.common.QuicClient;

/**
 * QuicClient
 * 
 * @author wuhao
 */
public class QuicAppClient {
	
	public static void main(String[] args) throws Exception {
		QuicClient quicClient = new QuicClient();
		quicClient.runClient();
	}
	
}
