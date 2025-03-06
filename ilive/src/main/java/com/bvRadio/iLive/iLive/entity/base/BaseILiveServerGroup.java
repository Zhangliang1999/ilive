package com.bvRadio.iLive.iLive.entity.base;

import java.util.HashSet;
import java.util.Set;

import com.bvRadio.iLive.iLive.entity.ILiveServer;
import com.bvRadio.iLive.iLive.entity.MountInfo;

/**
 * 
 * @author administrator
 * 服务器组
 */
public abstract class BaseILiveServerGroup {
	
	 /**
     * 服务器组ID
    */
    private Integer server_group_id;
    
    /**
     * 服务器组名称
    */
    private String server_group_name;
    
    /**
     * 分发是否启动  0:关闭  1:启动
    */
    private Integer distribute_status;
    
    
    // 组列表
    private Set<ILiveServer> serverList = new HashSet<ILiveServer>();
    
    //发布点列表
    private Set<MountInfo> mountList = new HashSet<>();
    
    /**
     * 分发服务器组类型  0:中心服务器组  1:分发服务器组
    */
    private Integer server_group_type;
    
    /**
     * 服务器组地址
    */
    private String virtual_ip;
    /**
     * 磁盘空间  单位 G
    */
    private Integer disk_space;
    /**
     * 公共磁盘空间  单位 G
    */
    private Integer public_disk_space;
    /**
     * 警戒磁盘空间  单位 M
    */
    private Integer warning_space;
    /**
     * 剩余磁盘空间  单位 M
    */
    private Integer left_space;
    /**
     * 负载均衡策略 1:软件智能  2:软件随机  3：网络负载均衡
    */
    private Integer loadbalance_mode;
    /**
     * 是否监控 0:是  1:否
    */
    private Integer if_monitor;
    /**
     * 监控频率 30s
    */
    private Integer monitor_frequency;
    /**
     * 服务器组集群ID
    */
    private Integer logical_server_group_id;
    
    
	public Integer getServer_group_id() {
		return server_group_id;
	}
	public void setServer_group_id(Integer server_group_id) {
		this.server_group_id = server_group_id;
	}
	public String getServer_group_name() {
		return server_group_name;
	}
	public void setServer_group_name(String server_group_name) {
		this.server_group_name = server_group_name;
	}
	public Integer getDistribute_status() {
		return distribute_status;
	}
	public void setDistribute_status(Integer distribute_status) {
		this.distribute_status = distribute_status;
	}
	public Integer getServer_group_type() {
		return server_group_type;
	}
	public void setServer_group_type(Integer server_group_type) {
		this.server_group_type = server_group_type;
	}
	public String getVirtual_ip() {
		return virtual_ip;
	}
	public void setVirtual_ip(String virtual_ip) {
		this.virtual_ip = virtual_ip;
	}
	public Integer getDisk_space() {
		return disk_space;
	}
	public void setDisk_space(Integer disk_space) {
		this.disk_space = disk_space;
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
	public Integer getLoadbalance_mode() {
		return loadbalance_mode;
	}
	public void setLoadbalance_mode(Integer loadbalance_mode) {
		this.loadbalance_mode = loadbalance_mode;
	}
	public Integer getIf_monitor() {
		return if_monitor;
	}
	public void setIf_monitor(Integer if_monitor) {
		this.if_monitor = if_monitor;
	}
	public Integer getMonitor_frequency() {
		return monitor_frequency;
	}
	public void setMonitor_frequency(Integer monitor_frequency) {
		this.monitor_frequency = monitor_frequency;
	}
	public Integer getLogical_server_group_id() {
		return logical_server_group_id;
	}
	public void setLogical_server_group_id(Integer logical_server_group_id) {
		this.logical_server_group_id = logical_server_group_id;
	}
	public Set<ILiveServer> getServerList() {
		return serverList;
	}
	public void setServerList(Set<ILiveServer> serverList) {
		this.serverList = serverList;
	}
	public Set<MountInfo> getMountList() {
		return mountList;
	}
	public void setMountList(Set<MountInfo> mountList) {
		this.mountList = mountList;
	}
    

}
