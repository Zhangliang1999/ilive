package com.bvRadio.iLive.iLive.web.mina;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bvRadio.iLive.iLive.web.ConfigUtils;

public class MinaServer {
	private static final Logger log = LoggerFactory.getLogger(MinaServer.class);

	public static void runServer() {
		SocketAcceptor acceptor = new NioSocketAcceptor();
		acceptor.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
		acceptor.setHandler(new MinaServerHandler());
		acceptor.getSessionConfig().setReadBufferSize(2048);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		try {
			String socketPort = ConfigUtils.get(ConfigUtils.SOCKET_PORT);
			int bindPort = Integer.parseInt(socketPort);
			acceptor.bind(new InetSocketAddress(bindPort));
		} catch (IOException e) {
			log.error("创建socket出错。", e);
		} catch (Exception e) {
			log.error("创建socket出错。", e);
		}
	}

}
