package com.bvRadio.iLive.iLive.action.front.vo.cloud;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias(value = "Input")
public class Input {

	String RemoteAddr = "";

	public String getRemoteAddr() {
		return RemoteAddr;
	}

	public void setRemoteAddr(String remoteAddr) {
		RemoteAddr = remoteAddr;
	}

}
