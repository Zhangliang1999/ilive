package com.bvRadio.iLive.iLive.entity;

import java.util.List;

/**
 * 专题内部结构
 * @author Wei
 *
 */
public class TopicInnerType {

	/**
	 * 结构id
	 */
	private Long id;
	
	/**
	 * 结构对应的主题id
	 */
	private Long topicId;
	
	/**
	 * 结构的顺序
	 */
	private Integer orderN;
	
	/**
	 * 结构类型   1、文字  2、图片  3、图集  4、单列   5、双列  6、标题
	 */
	private Integer type;
	
	/**
	 * 包含多少个内容   用于图集
	 */
	private Integer num;
	
	/**
	 * 是否保存  0不保存   1保存
	 */
	private Integer isSave;
	
	/**
	 * 该机构包含的内容列表
	 */
	private List<TopicInnerContent> contentList;

	public Integer getIsSave() {
		return isSave;
	}

	public void setIsSave(Integer isSave) {
		this.isSave = isSave;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public List<TopicInnerContent> getContentList() {
		return contentList;
	}

	public void setContentList(List<TopicInnerContent> contentList) {
		this.contentList = contentList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTopicId() {
		return topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	public Integer getOrderN() {
		return orderN;
	}

	public void setOrderN(Integer orderN) {
		this.orderN = orderN;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}
