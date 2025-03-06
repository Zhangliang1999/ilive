package com.bvRadio.iLive.iLive.entity.base;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import com.bvRadio.iLive.iLive.entity.BBSDiyform;
import com.bvRadio.iLive.iLive.entity.BBSDiyformOption;

/**
 * @author administrator 问卷对应的问题集合
 */
@SuppressWarnings("serial")
public abstract class BaseBBSDiymodel implements java.io.Serializable {

	// Fields
	/**
	 * 模型ID
	 */
	private Integer diymodelId;

	/**
	 * 自定义模型名称，暂时不用
	 */
	private String diymodelName;

	/**
	 * 问题类型
	 */
	private Short diymodelType;

	/**
	 * 问题标题
	 */
	private String diymodelTitle;

	/**
	 * 标题
	 */
	private String diymodelKey;

	/**
	 * 问题默认值
	 */
	private String defValue;

	/**
	 * 问题选项值
	 */
	private String optValue;

	/**
	 * 图片选项地址
	 */
	private String optImgUrl;

	/**
	 * 文本字数
	 */
	private Integer textSize;

	/**
	 * 区域行数
	 */
	private Integer areaRows;

	/**
	 * 区域列数
	 */
	private Integer areaCols;

	/**
	 * 提示语
	 */
	private String helpTxt;

	/**
	 * 提示未知
	 */
	private Integer helpPosition;

	/**
	 * 是否显示
	 */
	private boolean isDisplay;

	/**
	 * 关联的主表单
	 */
	private BBSDiyform bbsDiyform;

	/**
	 * 问题顺序
	 */
	private Integer diyOrder;

	/**
	 * 创建时间
	 */
	private Timestamp createTime;

	/**
	 * 问题是否开启口令回答
	 */
	private Integer openAnswer;

	/**
	 * 问题是否必答
	 */
	private Integer needAnswer;
	
	/**
	 * 问题是否短信验证
	 */
	private Integer needMsgValid;

	/**
	 * 问题的选项
	 */
	Set<BBSDiyformOption> options = new HashSet<>();

	//修改标识
	private Integer updateMark;
	
	// Constructors

	/** default constructor */
	public BaseBBSDiymodel() {
	}

	/** minimal constructor */
	public BaseBBSDiymodel(Integer diymodelId) {
		this.diymodelId = diymodelId;
	}

	public BaseBBSDiymodel(Integer diymodelId, String diymodelName, Short diymodelType, String diymodelTitle,
			String diymodelKey, String defValue, String optValue, String optImgUrl, Integer textSize, Integer areaRows,
			Integer areaCols, String helpTxt, Integer helpPosition, boolean isDisplay, BBSDiyform bbsDiyform,
			Integer diyOrder, Timestamp createTime) {
		super();
		this.diymodelId = diymodelId;
		this.diymodelName = diymodelName;
		this.diymodelType = diymodelType;
		this.diymodelTitle = diymodelTitle;
		this.diymodelKey = diymodelKey;
		this.defValue = defValue;
		this.optValue = optValue;
		this.optImgUrl = optImgUrl;
		this.textSize = textSize;
		this.areaRows = areaRows;
		this.areaCols = areaCols;
		this.helpTxt = helpTxt;
		this.helpPosition = helpPosition;
		this.isDisplay = isDisplay;
		this.bbsDiyform = bbsDiyform;
		this.diyOrder = diyOrder;
		this.createTime = createTime;
	}

	public Integer getDiymodelId() {
		return this.diymodelId;
	}

	public void setDiymodelId(Integer diymodelId) {
		this.diymodelId = diymodelId;
	}

	public String getDiymodelName() {
		return this.diymodelName;
	}

	public void setDiymodelName(String diymodelName) {
		this.diymodelName = diymodelName;
	}

	public Short getDiymodelType() {
		return this.diymodelType;
	}

	public void setDiymodelType(Short diymodelType) {
		this.diymodelType = diymodelType;
	}

	public String getDiymodelTitle() {
		return this.diymodelTitle;
	}

	public void setDiymodelTitle(String diymodelTitle) {
		this.diymodelTitle = diymodelTitle;
	}

	public String getDiymodelKey() {
		return this.diymodelKey;
	}

	public void setDiymodelKey(String diymodelKey) {
		this.diymodelKey = diymodelKey;
	}

	public String getDefValue() {
		return this.defValue;
	}

	public void setDefValue(String defValue) {
		this.defValue = defValue;
	}

	public String getOptValue() {
		return this.optValue;
	}

	public void setOptValue(String optValue) {
		this.optValue = optValue;
	}

	public String getOptImgUrl() {
		return optImgUrl;
	}

	public void setOptImgUrl(String optImgUrl) {
		this.optImgUrl = optImgUrl;
	}

	public Integer getTextSize() {
		return this.textSize;
	}

	public void setTextSize(Integer textSize) {
		this.textSize = textSize;
	}

	public Integer getAreaRows() {
		return this.areaRows;
	}

	public void setAreaRows(Integer areaRows) {
		this.areaRows = areaRows;
	}

	public Integer getAreaCols() {
		return this.areaCols;
	}

	public void setAreaCols(Integer areaCols) {
		this.areaCols = areaCols;
	}

	public String getHelpTxt() {
		return this.helpTxt;
	}

	public void setHelpTxt(String helpTxt) {
		this.helpTxt = helpTxt;
	}

	public Integer getHelpPosition() {
		return this.helpPosition;
	}

	public void setHelpPosition(Integer helpPosition) {
		this.helpPosition = helpPosition;
	}

	public boolean getIsDisplay() {
		return this.isDisplay;
	}

	public void setIsDisplay(boolean isDisplay) {
		this.isDisplay = isDisplay;
	}

	public BBSDiyform getBbsDiyform() {
		return bbsDiyform;
	}

	public void setBbsDiyform(BBSDiyform bbsDiyform) {
		this.bbsDiyform = bbsDiyform;
	}

	public Integer getDiyOrder() {
		return diyOrder;
	}

	public void setDiyOrder(Integer diyOrder) {
		this.diyOrder = diyOrder;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Integer getOpenAnswer() {
		return openAnswer;
	}

	public void setOpenAnswer(Integer openAnswer) {
		this.openAnswer = openAnswer;
	}

	public void setDisplay(boolean isDisplay) {
		this.isDisplay = isDisplay;
	}

	public Integer getNeedAnswer() {
		return needAnswer;
	}

	public void setNeedAnswer(Integer needAnswer) {
		this.needAnswer = needAnswer;
	}

	public Set<BBSDiyformOption> getOptions() {
		return options;
	}

	public void setOptions(Set<BBSDiyformOption> options) {
		this.options = options;
	}

	public Integer getNeedMsgValid() {
		return needMsgValid;
	}

	public void setNeedMsgValid(Integer needMsgValid) {
		this.needMsgValid = needMsgValid;
	}

	public Integer getUpdateMark() {
		return updateMark;
	}

	public void setUpdateMark(Integer updateMark) {
		this.updateMark = updateMark;
	}

}