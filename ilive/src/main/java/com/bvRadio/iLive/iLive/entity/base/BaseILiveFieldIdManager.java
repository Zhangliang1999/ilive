package com.bvRadio.iLive.iLive.entity.base;

@SuppressWarnings("serial")
public abstract class BaseILiveFieldIdManager implements java.io.Serializable {

	// Fields

	private Integer id;
	private String tableName;
	private String fieldName;
	private Long nextId;
	private Integer maxId;

	// Constructors

	/** default constructor */
	public BaseILiveFieldIdManager() {
	}

	/** minimal constructor */
	public BaseILiveFieldIdManager(Integer id, String tableName, String fieldName) {
		this.id = id;
		this.tableName = tableName;
		this.fieldName = fieldName;
	}

	/** full constructor */
	public BaseILiveFieldIdManager(Integer id, String tableName, String fieldName, Long nextId, Integer maxId) {
		this.id = id;
		this.tableName = tableName;
		this.fieldName = fieldName;
		this.nextId = nextId;
		this.maxId = maxId;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getFieldName() {
		return this.fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Long getNextId() {
		return this.nextId;
	}

	public void setNextId(Long nextId) {
		this.nextId = nextId;
	}

	public Integer getMaxId() {
		return this.maxId;
	}

	public void setMaxId(Integer maxId) {
		this.maxId = maxId;
	}

}