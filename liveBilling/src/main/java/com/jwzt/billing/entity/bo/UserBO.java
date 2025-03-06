package com.jwzt.billing.entity.bo;

import java.util.List;

import javax.servlet.http.HttpServletRequest;



/**
 * @author ysf
 */
public class UserBO {
	private Long id;
	private String username;
	private String nickname;
	private String userImg;
	private Integer enterpriseId;
	private Boolean admin;
	private Integer certStatus;
    private Integer haspackage;
    private Integer getIfPassRevenue;
    private Integer subAccountManagement;
    private Integer level;
    private List subTop;
    private Integer enterPriseMessage;//企业信息
    private Integer sub;//子账号
    private Integer figure;//平台大数据
    private Integer vip;
    private Integer fans;
    private Integer account;
	public Integer getEnterPriseMessage() {
		return enterPriseMessage;
	}

	public void setEnterPriseMessage(Integer enterPriseMessage) {
		this.enterPriseMessage = enterPriseMessage;
	}

	public Integer getSub() {
		return sub;
	}

	public void setSub(Integer sub) {
		this.sub = sub;
	}

	public Integer getFigure() {
		return figure;
	}

	public void setFigure(Integer figure) {
		this.figure = figure;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getUserImg() {
		return userImg;
	}

	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public Integer getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Integer getCertStatus() {
		return certStatus;
	}

	public void setCertStatus(Integer certStatus) {
		this.certStatus = certStatus;
	}

	public Integer getHaspackage() {
		return haspackage;
	}

	public void setHaspackage(Integer haspackage) {
		this.haspackage = haspackage;
	}

	public Integer getGetIfPassRevenue() {
		return getIfPassRevenue;
	}

	public void setGetIfPassRevenue(Integer getIfPassRevenue) {
		this.getIfPassRevenue = getIfPassRevenue;
	}

	public Integer getSubAccountManagement() {
		return subAccountManagement;
	}

	public void setSubAccountManagement(Integer subAccountManagement) {
		this.subAccountManagement = subAccountManagement;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public List getSubTop() {
		return subTop;
	}

	public void setSubTop(List subTop) {
		this.subTop = subTop;
	}
	
public Integer getVip() {
		return vip;
	}

	public void setVip(Integer vip) {
		this.vip = vip;
	}

	public Integer getFans() {
		return fans;
	}

	public void setFans(Integer fans) {
		this.fans = fans;
	}

	public Integer getAccount() {
		return account;
	}

	public void setAccount(Integer account) {
		this.account = account;
	}

public boolean message(Integer level,List subTop) {
	boolean ret=false;
	boolean kan=this.selectIfCan(subTop, "6", level);
	if(kan) {
		ret=true;
	}
	return ret;
}
public boolean sub(Integer level,List subTop,Integer subAccountManagement) {
	boolean ret=false;
	boolean kan=this.selectIfCan(subTop, "7", level);
	if(kan&&(subAccountManagement==1&&level==0)) {
		ret=true;
	}
	return ret;
}
public boolean figure(Integer level,List subTop) {
	boolean ret=false;
	boolean kan=this.selectIfCan(subTop, "2", level);
	if(kan) {
		ret=true;
	}
	return ret;
}
public boolean vip(Integer level,List subTop) {
	boolean ret=false;
	boolean kan=this.selectIfCan(subTop, "9", level);
	if(kan) {
		ret=true;
	}
	return ret;
}
public boolean fans(Integer level,List subTop) {
	boolean ret=false;
	boolean kan=this.selectIfCan(subTop, "8", level);
	if(kan) {
		ret=true;
	}
	return ret;
}
public boolean account(Integer level,List subTop,Integer getIfPassRevenue) {
	boolean ret=false;
	boolean kan=this.selectIfCan(subTop, "10", level);
	if(kan&&(getIfPassRevenue==1&&level==0)) {
		ret=true;
	}
	return ret;
}
public boolean selectIfCan(List subTop, String permission,Integer level) {
	boolean ret=false;
	if(level==0) {
		ret=true;
	}else {
		if(subTop==null||subTop.size()==0) {
			ret=false;
		}else if(subTop!=null&&subTop.size()>0) {
			for(int i=0;i<subTop.size();i++) {
				String sub=subTop.get(i).toString();
				if(permission.equals(sub)) {
					ret=true;
					break;
				}
			}
		}
	}
	
	return ret;
}
}
