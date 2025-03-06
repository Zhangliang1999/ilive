package com.bvRadio.iLive.iLive.entity.base;

import com.bvRadio.iLive.iLive.entity.ILiveServerGroup;

/**
 * @author administrator
 * 发布点
 */
public abstract class BaseILiveMountPointer {
	
	  /**
     * 发布点ID
    */
    private Integer server_mount_id;
    /**
     * 发布点名称
    */
    private String server_mount_name;
    /**
     * 服务器组ID
    */
    private ILiveServerGroup server_group;
    /**
     * 分配的存储空间（0:无限制）
    */
    private Integer storage_space;
    /**
     * 已使用空间（单位：M）
    */
    private Integer used_space;
    /**
     * 发布点对应真实路径
    */
    private String base_path;
    /**
     * FTP路径
    */
    private String ftp_path;
    /**
     * 发布点类型  0：存储发布点  1：分发发布点 
    */
    private Integer mount_type;
    /**
     * 镜像、影子发布点ID
    */
    private Integer mirror_mount_id;
    /**
     * 发布点描述
    */
    private String mount_desc;
    /**
     * 是否正在使用 0：正在使用 1：作废
    */
    private Integer if_use;
    
    /**
     * 公共磁盘空间
    */
    private Integer public_disk_space;
    
    /**
     * 警戒磁盘空间
    */
    private Integer warning_space;
    
    /**
     * 剩余磁盘空间
    */
    private Integer left_space;

    private Integer iscut;

	public Integer getServer_mount_id() {
		return server_mount_id;
	}

	public void setServer_mount_id(Integer server_mount_id) {
		this.server_mount_id = server_mount_id;
	}

	public String getServer_mount_name() {
		return server_mount_name;
	}

	public void setServer_mount_name(String server_mount_name) {
		this.server_mount_name = server_mount_name;
	}

	public ILiveServerGroup getServer_group() {
		return server_group;
	}

	public void setServer_group(ILiveServerGroup server_group) {
		this.server_group = server_group;
	}

	public Integer getStorage_space() {
		return storage_space;
	}

	public void setStorage_space(Integer storage_space) {
		this.storage_space = storage_space;
	}

	public Integer getUsed_space() {
		return used_space;
	}

	public void setUsed_space(Integer used_space) {
		this.used_space = used_space;
	}

	public String getBase_path() {
		return base_path;
	}

	public void setBase_path(String base_path) {
		this.base_path = base_path;
	}

	public String getFtp_path() {
		return ftp_path;
	}

	public void setFtp_path(String ftp_path) {
		this.ftp_path = ftp_path;
	}

	public Integer getMount_type() {
		return mount_type;
	}

	public void setMount_type(Integer mount_type) {
		this.mount_type = mount_type;
	}

	public Integer getMirror_mount_id() {
		return mirror_mount_id;
	}

	public void setMirror_mount_id(Integer mirror_mount_id) {
		this.mirror_mount_id = mirror_mount_id;
	}

	public String getMount_desc() {
		return mount_desc;
	}

	public void setMount_desc(String mount_desc) {
		this.mount_desc = mount_desc;
	}

	public Integer getIf_use() {
		return if_use;
	}

	public void setIf_use(Integer if_use) {
		this.if_use = if_use;
	}

	public Integer getPublic_disk_space() {
		return public_disk_space;
	}

	public void setPublic_disk_space(Integer public_disk_space) {
		this.public_disk_space = public_disk_space;
	}

	public Integer getWarning_space() {
		return warning_space;
	}

	public void setWarning_space(Integer warning_space) {
		this.warning_space = warning_space;
	}

	public Integer getLeft_space() {
		return left_space;
	}

	public void setLeft_space(Integer left_space) {
		this.left_space = left_space;
	}

	public Integer getIscut() {
		return iscut;
	}

	public void setIscut(Integer iscut) {
		this.iscut = iscut;
	}
    

}
