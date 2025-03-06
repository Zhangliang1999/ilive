package com.bvRadio.iLive.iLive.web.pc;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import com.bvRadio.iLive.iLive.util.IPUtils;

@Component
public class MyHandshakeInterceptor extends HttpSessionHandshakeInterceptor {

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler handler,
			Map map) throws Exception {
		if (request.getHeaders().containsKey("Sec-WebSocket-Extensions")) {
			request.getHeaders().set("Sec-WebSocket-Extensions", "permessage-deflate");
		}
		String username = ((ServletServerHttpRequest) request).getServletRequest().getParameter("username");
		// // System.out.println("登录账户：" + username);
		if (username != null) {
			HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
			String realIpAddr = IPUtils.getRealIpAddr(servletRequest);
			// System.out.println("realIpAddrrealIpAddr:" + realIpAddr);
			map.put("username", username);
			map.put("realIpAddr", "");
			if (realIpAddr != null) {
				if (realIpAddr.indexOf(",") > -1) {
					String[] split = realIpAddr.split(",");
					if (split.length > 0) {
						map.put("realIpAddr", split[0].trim());
					}
				} else {
					map.put("realIpAddr", realIpAddr);
				}
			}
		}
		return super.beforeHandshake(request, response, handler, map);
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception ex) {
		super.afterHandshake(request, response, wsHandler, ex);
	}
}
