﻿package com.jwzt.DB.cdn.ip_manage;

import java.sql.Timestamp;

// Generated 2006-6-6 14:24:15 by Hibernate Tools 3.1.0 beta3

/**
 * IpAddressInfo generated by hbm2java
 */

public class IpAddressInfo  implements java.io.Serializable 
{
     /**
      * 唯一id
     */
     private Integer address_id;
     /**
      * 区域名称
     */
     private String address_name;
     /**
      * 该IP地址是否生效 0：不生效  1：生效
     */
     private Integer if_use;
     /**
      * 该IP地址是否需要登录 0：不需要  1：需要
     */
     private Integer if_need_login;
     /**
      * 该IP地址类型 0：禁止  1：允许
     */
     private Integer type;
     /**
      * 地址描述
     */
     private String address_desc;
     /**
      * 添加时间
     */
     private Timestamp add_time;
     /**
      * 绑定栏目权限
      */
     private int rights_id;

    // Constructors

    public int getRights_id()
	{
		return rights_id;
	}

	public void setRights_id(int rights_id)
	{
		this.rights_id = rights_id;
	}

	/** default constructor */
    public IpAddressInfo() {
    }

	/** minimal constructor */
    public IpAddressInfo(Integer address_id) {
        this.address_id = address_id;
    }

	public Timestamp getAdd_time() {
		return add_time;
	}

	public void setAdd_time(Timestamp add_time) {
		this.add_time = add_time;
	}

	public String getAddress_desc() {
		return address_desc;
	}

	public void setAddress_desc(String address_desc) {
		this.address_desc = address_desc;
	}

	public Integer getAddress_id() {
		return address_id;
	}

	public void setAddress_id(Integer address_id) {
		this.address_id = address_id;
	}

	public String getAddress_name() {
		return address_name;
	}

	public void setAddress_name(String address_name) {
		this.address_name = address_name;
	}

	public Integer getIf_need_login() {
		return if_need_login;
	}

	public void setIf_need_login(Integer if_need_login) {
		this.if_need_login = if_need_login;
	}

	public Integer getIf_use() {
		return if_use;
	}

	public void setIf_use(Integer if_use) {
		this.if_use = if_use;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
    
    
}
