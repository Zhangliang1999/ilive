package com.bvRadio.iLive.iLive.action.front.vo.cloud;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.Annotations;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XStreamTest {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		XStream xstream = new XStream(new DomDriver());
		Annotations.configureAliases(xstream, XmlRoot.class);
		XmlRoot xmlRoot = new XmlRoot();
		Content content = new Content();
		xmlRoot.setContent(content);
		List<Input> inputs = new ArrayList<>();
		content.setInputs(inputs);
		List<Output> outputs = new ArrayList<>();
		Output ot = new Output();
		ot.setOutRTMP("123123");
//		ot.setCodecInfo(ot.getDefaultCodeInfo());
		outputs.add(ot);
		content.setOutputs(outputs);
		String obj_xml = xstream.toXML(xmlRoot);
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"GBK\"?>\r\n");
		sb.append(obj_xml);
		System.out.println(sb.toString());
	}

}
