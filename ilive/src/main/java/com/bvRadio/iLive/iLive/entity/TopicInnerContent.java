package com.bvRadio.iLive.iLive.entity;

/**
 * 专题结构内的内容
 * @author Wei
 *
 */
public class TopicInnerContent {

	/**
	 * 主键id
	 */
	private Long id;
	
	/**
	 * 结构id
	 */
	private Long innerTypeId;
	
	/**
	 * 内容名称
	 */
	private String name;
	
	/**
	 * 内容简介
	 */
	private String descript;
	
	/**
	 * 背景图片的url
	 */
	private String backgroundUrl;

	/**
	 * 内容对应的url
	 */
	private String contentUrl;
	
	/**
	 * 内容对应的id
	 */
	private Integer contentId;

	/**
	 * 内容的类型
	 */
	private Integer contentType;
	
	/**
	 * 内容自带的背景图片
	 */
	private String contentBackgroundUrl;
	
	/**
	 * 字体颜色
	 */
	private String fontColor;
	
	/**
	 * 字体大小
	 */
	private Integer fontSize;
	
	/**
	 * 背景颜色
	 */
	private String backgroundColor;
	
	/**
	 * 顺序
	 */
	private Integer orderN;
	
	/**
	 * 是否保存  0不保存   1保存
	 */
	private Integer isSave;

	/**
	 * 是否加粗  0不加粗  1加粗
	 */
	private Integer overStriking;
	
	/**
	 * 是否倾斜  0不倾斜   1倾斜
	 */
	private Integer tilt;
	
	/**
	 * 左右对齐   0左对齐  1居中   2右对齐
	 */
	private Integer align;
	
	/**
	 * 直播间状态
	 */
	private Integer status;
	
	/**
	 *文本上下间距 
	 */
	private Integer paddingSize;
	
	
	public Integer getPaddingSize() {
		return paddingSize;
	}

	public void setPaddingSize(Integer paddingSize) {
		this.paddingSize = paddingSize;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getContentId() {
		return contentId;
	}

	public void setContentId(Integer contentId) {
		this.contentId = contentId;
	}

	public Integer getOverStriking() {
		return overStriking;
	}

	public void setOverStriking(Integer overStriking) {
		this.overStriking = overStriking;
	}

	public Integer getTilt() {
		return tilt;
	}

	public void setTilt(Integer tilt) {
		this.tilt = tilt;
	}

	public Integer getAlign() {
		return align;
	}

	public void setAlign(Integer align) {
		this.align = align;
	}

	public String getContentBackgroundUrl() {
		return contentBackgroundUrl;
	}

	public void setContentBackgroundUrl(String contentBackgroundUrl) {
		this.contentBackgroundUrl = contentBackgroundUrl;
	}

	public Integer getIsSave() {
		return isSave;
	}

	public void setIsSave(Integer isSave) {
		this.isSave = isSave;
	}

	public Integer getFontSize() {
		return fontSize;
	}

	public void setFontSize(Integer fontSize) {
		this.fontSize = fontSize;
	}

	public Integer getOrderN() {
		return orderN;
	}

	public void setOrderN(Integer orderN) {
		this.orderN = orderN;
	}

	public String getFontColor() {
		return fontColor;
	}

	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getInnerTypeId() {
		return innerTypeId;
	}

	public void setInnerTypeId(Long innerTypeId) {
		this.innerTypeId = innerTypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public String getBackgroundUrl() {
		return backgroundUrl;
	}

	public void setBackgroundUrl(String backgroundUrl) {
		this.backgroundUrl = backgroundUrl;
	}

	public String getContentUrl() {
		return contentUrl;
	}

	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}

	public Integer getContentType() {
		return contentType;
	}

	public void setContentType(Integer contentType) {
		this.contentType = contentType;
	}
	
}
