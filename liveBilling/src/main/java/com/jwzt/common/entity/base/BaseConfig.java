package com.jwzt.common.entity.base;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class BaseConfig implements Serializable {
	private String id;

	private String value;

	public BaseConfig() {
		super();
	}

	public BaseConfig(String id, String value) {
		super();
		this.id = id;
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}