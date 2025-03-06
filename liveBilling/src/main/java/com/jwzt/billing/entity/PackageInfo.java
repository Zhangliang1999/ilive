package com.jwzt.billing.entity;
/**
* @author ysf
*/

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jwzt.billing.entity.base.BasePackageInfo;
import com.jwzt.billing.entity.bo.WorkLog;

@SuppressWarnings("serial")
public class PackageInfo extends BasePackageInfo {
	/**
	 * 套餐类型：基本
	 */
	public final static Integer PACKAGE_TYPE_BASIC = 1;
	/**
	 * 套餐类型：存储
	 */
	public final static Integer PACKAGE_TYPE_INCREMENT_STORAGE = 2;
	/**
	 * 套餐类型：短信
	 */
	public final static Integer PACKAGE_TYPE_INCREMENT_SHORT_MESSAGE = 3;
	/**
	 * 套餐类型：计时
	 */
	public final static Integer PACKAGE_TYPE_TIMING = 4;
	/**
	 * 套餐类型：子账户数量
	 */
	public final static Integer PACKAGE_TYPE_SUBCOUNT = 5;
	/**
	 * 套餐类型：并發
	 */
	public final static Integer PACKAGE_TYPE_CURR = 6;
	/**
	 * 有效时长的单位 年
	 */
	public final static Integer VAILD_DURATION_UNIT_YEAR = 1;
	/**
	 * 有效时长的单位 月
	 */
	public final static Integer VAILD_DURATION_UNIT_MONTH = 2;
	/**
	 * 有效时长的单位 日
	 */
	public final static Integer VAILD_DURATION_UNIT_DAY = 3;
	/**
	 * 状态：新增
	 */
	public final static Integer STATUS_NEW = 1;
	/**
	 * 状态：上线
	 */
	public final static Integer STATUS_ONLINE = 2;
	/**
	 * 状态：下线
	 */
	public final static Integer STATUS_OFFLINE = 3;

	/**
	 * 投放渠道：官方平台
	 */
	public final static Integer CHANNEL_TYPE_OFFICIAL_PLATFORM = 1;
	/**
	 * 投放渠道：渠道代理
	 */
	public final static Integer CHANNEL_TYPE_CHANNEL_AGENT = 2;
	/**
	 * 投放渠道：集团产品库
	 */
	public final static Integer CHANNEL_TYPE_GROUP_PRODUCT_LIBRARY = 3;
	/**
	 * 投放渠道：内部使用
	 */
	public final static Integer CHANNEL_TYPE_inside = 4;
	private List<ProductInfo> productList;
	private Long concurrenceProduct;
	private Long durationProduct;
	private Long shortMessageProduct;
	private Long storageProduct;

	private List<WorkLog> workLogList;

	public void initFieldValue() {
		if (null == getCreateTime()) {
			setCreateTime(new Timestamp(System.currentTimeMillis()));
		}
		if (null == getStatus()) {
			setStatus(STATUS_NEW);
		}
	}

	public PackageInfo() {
		super();
	}

	public boolean getEnableChannelOfOfficialPlatform() {
		return getEnableChannel(CHANNEL_TYPE_OFFICIAL_PLATFORM);
	}

	public boolean getEnableChannelOfChannelAgent() {
		return getEnableChannel(CHANNEL_TYPE_CHANNEL_AGENT);
	}

	public boolean getEnableChannelOfGroupProductLibrary() {
		return getEnableChannel(CHANNEL_TYPE_GROUP_PRODUCT_LIBRARY);
	}
	public boolean getEnableChannelOfinside() {
		return getEnableChannel(CHANNEL_TYPE_inside);
	}
	private boolean getEnableChannel(Integer channelType) {
		if (null != channelType) {
			String channelTypes = getChannelTypes();
			if (StringUtils.isNotBlank(channelTypes)) {
				String[] channelTypeArray = channelTypes.split(",");
				for (String enableChannelType : channelTypeArray) {
					if (StringUtils.isNotBlank(enableChannelType)
							&& enableChannelType.equals(String.valueOf(channelType))) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public Double getDurationProductHour() {
		if (null != durationProduct) {
			return durationProduct / 3600.0;
		}
		return 0D;
	}

	public Double getStorageProductGB() {
		if (null != storageProduct) {
			return storageProduct / 1024.0 / 1024.0;
		}
		return 0D;
	}

	public List<ProductInfo> getProductList() {
		return productList;
	}

	public void setProductList(List<ProductInfo> productList) {
		this.productList = productList;
	}

	public Long getConcurrenceProduct() {
		return concurrenceProduct;
	}

	public void setConcurrenceProduct(Long concurrenceProduct) {
		this.concurrenceProduct = concurrenceProduct;
	}

	public Long getDurationProduct() {
		return durationProduct;
	}

	public void setDurationProduct(Long durationProduct) {
		this.durationProduct = durationProduct;
	}

	public Long getShortMessageProduct() {
		return shortMessageProduct;
	}

	public void setShortMessageProduct(Long shortMessageProduct) {
		this.shortMessageProduct = shortMessageProduct;
	}

	public Long getStorageProduct() {
		return storageProduct;
	}

	public void setStorageProduct(Long storageProduct) {
		this.storageProduct = storageProduct;
	}

	public List<WorkLog> getWorkLogList() {
		return workLogList;
	}

	public void setWorkLogList(List<WorkLog> workLogList) {
		this.workLogList = workLogList;
	}

	@Override
	public String toString() {
		return super.toString();
	}

}
