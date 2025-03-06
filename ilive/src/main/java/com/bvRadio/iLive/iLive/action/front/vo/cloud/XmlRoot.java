package com.bvRadio.iLive.iLive.action.front.vo.cloud;

import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement
@XStreamAlias(value = "root")
public class XmlRoot {

	Content content;

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

}
