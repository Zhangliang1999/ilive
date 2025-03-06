package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;

@SuppressWarnings("serial")
public abstract class BaseILiveLiveReport implements java.io.Serializable {
	
	private Integer id;  //主键
	private String reporter;	//举报人名字
	private String reported;	//被举报人
	private String content;//举报内容
	private Integer statu;//状态（0未处理，1 已处理）
	private Timestamp submitTime;//提交时间
	private Timestamp dealTime;//解决时间
	public BaseILiveLiveReport(Integer id, String reporter, String reported,
			String content, Integer statu, Timestamp submitTime,
			Timestamp dealTime) {
		super();
		this.id = id;
		this.reporter = reporter;
		this.reported = reported;
		this.content = content;
		this.statu = statu;
		this.submitTime = submitTime;
		this.dealTime = dealTime;
	}
	public BaseILiveLiveReport() {
		super();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getReporter() {
		return reporter;
	}
	public void setReporter(String reporter) {
		this.reporter = reporter;
	}
	public String getReported() {
		return reported;
	}
	public void setReported(String reported) {
		this.reported = reported;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getStatu() {
		return statu;
	}
	public void setStatu(Integer statu) {
		this.statu = statu;
	}
	public Timestamp getSubmitTime() {
		return submitTime;
	}
	public void setSubmitTime(Timestamp submitTime) {
		this.submitTime = submitTime;
	}
	public Timestamp getDealTime() {
		return dealTime;
	}
	public void setDealTime(Timestamp dealTime) {
		this.dealTime = dealTime;
	}
	
	
}