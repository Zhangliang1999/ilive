package com.jwzt.billing.entity.vo;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;

/**
 * @author ysf
 */
public class EnterpriseProductInfo {
	private String functionCode;
	private Long concurrenceProduct;
	private Long durationProduct;
	private Long shortMessageProduct;
	private Long storageProduct;
	private Long usedConcurrenceProduct;
	private Long usedDurationProduct;
	private Long usedShortMessageProduct;
	private Long usedStorageProduct;
	private Long subCountProduct;
	private Long usedSubCountProduct;
    private Integer packageId;
	private String packageName;
	private Date vaildStartTime;
	private Date vaildEndTime;

	public Double getDurationProductHour() {
		if (null != durationProduct) {
			return durationProduct / 3600.0;
		}
		return 0D;
	}

	public Double getUsedDurationProductHour() {
		if (null != usedDurationProduct) {
			return usedDurationProduct / 3600.0;
		}
		return 0D;
	}

	public Double getStorageProductGB() {
		if (null != storageProduct) {
			return storageProduct / 1024.0 / 1024.0;
		}
		return 0D;
	}

	public Double getUsedStorageProductGB() {
		if (null != usedStorageProduct) {
			return usedStorageProduct / 1024.0 / 1024.0;
		}
		return 0D;
	}

	public Integer getRemainingDays() {
		DateTime begin = new DateTime(System.currentTimeMillis());
		DateTime end = new DateTime(vaildEndTime);
		Period period = new Period(begin, end, PeriodType.days());
		int days = period.getDays();
		if (days < 0) {
			return 0;
		} else if (days == 0 && begin.getMillis() >= end.getMillis()) {
			return 0;
		} else {
			return days + 1;
		}
	}

	public EnterpriseProductInfo(String functionCode) {
		super();
		this.functionCode = functionCode;
		this.concurrenceProduct = 0L;
		this.durationProduct = 0L;
		this.shortMessageProduct = 0L;
		this.storageProduct = 0L;
		this.usedConcurrenceProduct = 0L;
		this.usedDurationProduct = 0L;
		this.usedShortMessageProduct = 0L;
		this.usedStorageProduct = 0L;
		this.subCountProduct=0L;
		this.usedSubCountProduct=0L;
	}

	public EnterpriseProductInfo(String functionCode, Long concurrenceProduct, Long durationProduct,
			Long shortMessageProduct, Long storageProduct, Long usedConcurrenceProduct, Long usedDurationProduct,
			Long usedShortMessageProduct, Long usedStorageProduct,Long subCountProduct,Long usedSubCountProduct) {
		super();
		this.functionCode = functionCode;
		this.concurrenceProduct = concurrenceProduct;
		this.durationProduct = durationProduct;
		this.shortMessageProduct = shortMessageProduct;
		this.storageProduct = storageProduct;
		this.usedConcurrenceProduct = usedConcurrenceProduct;
		this.usedDurationProduct = usedDurationProduct;
		this.usedShortMessageProduct = usedShortMessageProduct;
		this.usedStorageProduct = usedStorageProduct;
		this.subCountProduct=subCountProduct;
		this.usedSubCountProduct=usedSubCountProduct;
	}

	public String getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
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

	public Long getUsedConcurrenceProduct() {
		return usedConcurrenceProduct;
	}

	public void setUsedConcurrenceProduct(Long usedConcurrenceProduct) {
		this.usedConcurrenceProduct = usedConcurrenceProduct;
	}

	public Long getUsedDurationProduct() {
		return usedDurationProduct;
	}

	public void setUsedDurationProduct(Long usedDurationProduct) {
		this.usedDurationProduct = usedDurationProduct;
	}

	public Long getUsedShortMessageProduct() {
		return usedShortMessageProduct;
	}

	public void setUsedShortMessageProduct(Long usedShortMessageProduct) {
		this.usedShortMessageProduct = usedShortMessageProduct;
	}

	public Long getUsedStorageProduct() {
		return usedStorageProduct;
	}

	public void setUsedStorageProduct(Long usedStorageProduct) {
		this.usedStorageProduct = usedStorageProduct;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Date getVaildStartTime() {
		return vaildStartTime;
	}

	public void setVaildStartTime(Date vaildStartTime) {
		this.vaildStartTime = vaildStartTime;
	}

	public Date getVaildEndTime() {
		return vaildEndTime;
	}

	public void setVaildEndTime(Date vaildEndTime) {
		this.vaildEndTime = vaildEndTime;
	}

	public Long getSubCountProduct() {
		return subCountProduct;
	}

	public void setSubCountProduct(Long subCountProduct) {
		this.subCountProduct = subCountProduct;
	}

	public Long getUsedSubCountProduct() {
		return usedSubCountProduct;
	}

	public void setUsedSubCountProduct(Long usedSubCountProduct) {
		this.usedSubCountProduct = usedSubCountProduct;
	}

}
