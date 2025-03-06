package com.bvRadio.iLive.iLive.entity;


import java.sql.Timestamp;

public class ILiveGift {
	
    private Integer id;
    private String name;//礼物名称
	private Integer liveType;//礼物分类
	private Integer type;//礼物类型
	private Integer num;//礼物点数
	private String pic;//礼物图片
	private Timestamp creatTime;//创建时间
	public ILiveGift() {
	}
	public ILiveGift(Integer id, String name, Integer liveType, Integer type,
			Integer num, String pic, Timestamp creatTime) {
		super();
		this.id = id;
		this.name = name;
		this.liveType = liveType;
		this.type = type;
		this.num = num;
		this.pic = pic;
		this.creatTime = creatTime;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getLiveType() {
		return liveType;
	}
	public void setLiveType(Integer liveType) {
		this.liveType = liveType;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public Timestamp getCreatTime() {
		return creatTime;
	}
	public void setCreatTime(Timestamp Timestamp) {
		this.creatTime = Timestamp;
	}
}
