package com.bvRadio.iLive.iLive.entity;

/**
 * 专题模板
 * @author Wei
 */
public class TopicTemplate {

	/**
	 * 模板id
	 */
	private Long id;
	
	/**
	 * 模板名称
	 */
	private String name;
	
	/**
	 * 背景颜色
	 */
	private String backgroundColor;
	
	/**
	 * 字体颜色
	 */
	private String fontColor;
	
	/**
	 * 字体类型
	 */
	private String fontType;
	
	/**
	 * 企业id
	 */
	private Integer enterpriseId;

	public Integer getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public String getFontColor() {
		return fontColor;
	}

	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}

	public String getFontType() {
		return fontType;
	}

	public void setFontType(String fontType) {
		this.fontType = fontType;
	}
	
	
}
