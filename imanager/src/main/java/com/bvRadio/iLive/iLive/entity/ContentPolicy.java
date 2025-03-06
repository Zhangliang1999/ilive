package com.bvRadio.iLive.iLive.entity;

public class ContentPolicy {

	//主键id
	private Integer id;
	
	//企业id
	private Integer enterpriseId;
	
	//选择方针    1手动     2自动
	private Integer policy;
	
	//显示数量
	private Integer num;
	
	//自动选择方针下单选按钮的状态  1直播 2回顾 3专题
	private Integer radios;
	
	//标记显示方式    3一行显示一条      5一行显示两条
	private Integer shows;

	//第二、四行标题
	private String title2;
	
	//第四行标题
	private String title4;
	
	//第四行中更多的链接
	private String link;
	
	//链接的标题
	private String linkName;
	
	//第一行显示的背景图片url
	private String imgurl;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPolicy() {
		return policy;
	}

	public void setPolicy(Integer policy) {
		this.policy = policy;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getRadios() {
		return radios;
	}

	public void setRadios(Integer radios) {
		this.radios = radios;
	}

	public Integer getShows() {
		return shows;
	}

	public void setShows(Integer shows) {
		this.shows = shows;
	}

	public String getTitle2() {
		return title2;
	}

	public void setTitle2(String title2) {
		this.title2 = title2;
	}

	public String getTitle4() {
		return title4;
	}

	public void setTitle4(String title4) {
		this.title4 = title4;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public Integer getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	
}
