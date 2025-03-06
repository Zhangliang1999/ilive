package com.bvRadio.iLive.iLive.entity;

public class PageDecorate {

	/**
	 * 主键id
	 */
	private Long id;

	/**
	 * 场次id
	 */
	private Long liveEventId;

	/**
	 * 菜单顺序
	 */
	private Integer menuOrder;

	/**
	 * 菜单名称
	 */
	private String menuName;
	/**
	 * 菜单名称
	 */
	private String menuName1;
	/**
	 *菜单类型		1话题   2聊天互动  3在线问答  4直播简介   5视频列表回看  6文档直播
	 */
	private Integer menuType;

	/**
	 * 菜单富文本内容
	 * 
	 * @return
	 */
	private String richContent;
	/**
	 * table 页切换
	 */
	private String hrefValue;
    /**
     * 是否开启相关视频推荐
     * @return
     */
	private Integer relatedVideo;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLiveEventId() {
		return liveEventId;
	}

	public void setLiveEventId(Long liveEventId) {
		this.liveEventId = liveEventId;
	}

	public Integer getMenuOrder() {
		return menuOrder;
	}

	public void setMenuOrder(Integer menuOrder) {
		this.menuOrder = menuOrder;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public Integer getMenuType() {
		return menuType;
	}

	public void setMenuType(Integer menuType) {
		this.menuType = menuType;
	}

	public String getRichContent() {
		return richContent;
	}

	public void setRichContent(String richContent) {
		this.richContent = richContent;
	}

	public String getHrefValue() {
		return hrefValue;
	}

	public void setHrefValue(String hrefValue) {
		this.hrefValue = hrefValue;
	}

	public String getMenuName1() {
		return menuName1;
	}

	public void setMenuName1(String menuName1) {
		this.menuName1 = menuName1;
	}

	public Integer getRelatedVideo() {
		return relatedVideo;
	}

	public void setRelatedVideo(Integer relatedVideo) {
		this.relatedVideo = relatedVideo;
	}

}
