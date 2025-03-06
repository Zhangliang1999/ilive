package com.bvRadio.iLive.iLive.action.front.vo.cloud;

import java.io.File;
import java.io.IOException;

import org.springframework.core.io.ClassPathResource;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.Annotations;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.xml.DomDriver;

@XStreamAlias(value = "Output")
public class Output {

	String OutRTMP;

	String OutUDP = "";

	CodecInfo CodecInfo;

	public String getOutRTMP() {
		return OutRTMP;
	}

	public void setOutRTMP(String outRTMP) {
		OutRTMP = outRTMP;
	}

	public String getOutUDP() {
		return OutUDP;
	}

	public void setOutUDP(String outUDP) {
		OutUDP = outUDP;
	}

	public CodecInfo getCodecInfo() {
		return CodecInfo;
	}

	public void setCodecInfo(CodecInfo codecInfo) {
		CodecInfo = codecInfo;
	}

	public CodecInfo getDefaultCodeInfo() throws IOException {
		XStream xstream = new XStream(new DomDriver());
		Annotations.configureAliases(xstream, CodecInfo.class);
		ClassPathResource classPathResource = new ClassPathResource("default_code.xml");
		CodecInfo defaultCode = (com.bvRadio.iLive.iLive.action.front.vo.cloud.CodecInfo) xstream
				.fromXML(classPathResource.getFile());
		return defaultCode;
	}

}
