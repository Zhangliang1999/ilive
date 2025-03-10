﻿package com.jwzt.DB.soms.vod.file;

// Generated 2006-6-6 14:24:19 by Hibernate Tools 3.1.0 beta3

import com.jwzt.DB.cdn.mount.MountInfo;
import com.jwzt.DB.soms.vod.catalog.CatalogInfo;

import java.sql.Timestamp;
import java.util.Date;

/**
 * FileInfo generated by hbm2java
 */

public class FileInfo implements java.io.Serializable
{

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -5975151044343304736L;

	/**
	 * 文件ID
	 */
	private Integer file_id;

	/**
	 * 栏目ID
	 */
	private CatalogInfo catalogInfo;

	/**
	 * 文件发布点
	 */
	private MountInfo mountInfo;

	/**
	 * 文件名称
	 */
	private String file_name;

	/**
	 * 文件别名
	 */
	private String file_alias;

	/**
	 * 内容提供商ID
	 */
	private Integer cp_id;

	/**
	 * 文件路径
	 */
	private String file_path;

	/**
	 * 本地文件路径
	 */
	private String local_path;

	/**
	 * 上传状态 0：等待 1：正在上传 2：上传完成（已有文件） 3：上传失败
	 */
	private Integer upload_state;

	/**
	 * 是否加密
	 */
	private Integer if_drm;

	/**
	 * 栏目类型 点播(0) 广告(1)
	 */
	private Integer file_type;

	/**
	 * 栏目类型 使用状态(0) 预删除状态(1)
	 */
	private Integer file_state;

	/**
	 * 是否分段 0：主文件 1：分段文件
	 */
	private Integer if_segment;

	/**
	 * 主文件ID
	 */
	private Integer parent_file_id =0;

	/**
	 * 分段文件的开始时间 （毫秒）
	 */
	private Integer start_time;

	/**
	 * 分段文件的结束时间 （毫秒）
	 */
	private Integer end_time;

	/**
	 * 文件大小 单位 K
	 */
	private Integer file_size = 0;

	/**
	 * 文件时长，单位是秒
	 */
	private Integer duration = 0;

	/**
	 * 带宽，单位是 Kbps
	 */
	private Integer band_width = 0;

	/**
	 * 入库时间
	 */
	private Timestamp record_time;

	/**
	 * 文件描述
	 */
	private String file_desc;

	/**
	 * REAL_ID_洪波
	 */
	private Integer real_id;

	/**
	 * P2P_ID_洪波
	 */
	private Integer p2p_id;

	/**
	 * 文件类型
	 */
	private String media_type;

	/**
	 * 远程路径_洪波
	 */
	private String remote_path;

	/**
	 * 语言
	 */
	private String language;

	/**
	 * 片头曲时长-山西晋城
	 */
	private String contentguid;

	/**
	 * 片尾曲时长-山西晋城
	 */
	private String contentkey;
	
	/**
	 * 预览短片的发布点ID
	 */
	private Integer preview_mount_id;
	
	/**
	 * 预览短片的文件路径
	 */
	private String preview_file_path;
	
	/**
	 * 预览短片的本地路径
	 */
	private String preview_local_path;
	
	/**
	 * 扩展字段
	 */
	private String guid_1;
	
	/**
	 * 扩展字段
	 */
	private String guid_2;
	
	/**
	 * 扩展字段
	 */
	private String guid_3;
	
	/**
	 * 扩展字段
	 */
	private String guid_4;
	
	/**
	 * 2009-11.20已占用(xhf)。视频文件放入回收站后，该字段记录其原先所在栏目id,以便还原
	 */
	private String guid_5;
	
	/**
	 * 导演
	 */
	private String director;
	
	/**
	 * 主演
	 */
	private String leadact;
	
	/**
	 * 节目名称
	 */
	private String actname;
	
	/**
	 * 关键字
	 */
	private String keywords;
	
	/**
	 * 扩展字段
	 */
	private String guid_6;
	
	/**
	 * 扩展字段
	 */
	private String guid_7;
	
	/**
	 * 扩展字段
	 */
	private String guid_8;
	
	/**
	 * 扩展字段 
	 */
	private String guid_9 ="0";
	
	/**
	 * 是否审核
	 */
	private Integer is_checked;
	/**
	 * 是否是flv文件
	 */
	private String is_flv;
	/**
	 * 文件格式
	 */
	private String vod_format ="";
	/**
	 * 文件分辨率
	 */
	private String vod_width_height;

	/**
	 * 文件上传人
	 */
	private String file_owner;
	
	/**
	 * 文件上传人姓名
	 */
	private String file_owner_name;
	
	private Date scheme_startTime = null;

	private Date scheme_endTime = null;
	
	
	
	/**
	 * 是否转码 是否通过转码入库,0:不是 1:是
	 */
	private Integer if_convert =0;
	// Constructors

	private String smallmagePath ;

	private String largeImagePath;
	
	private String smallmageLocalPath ;

	private String largeImageLocalPath;
	
	
	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getLeadact() {
		return leadact;
	}

	public void setLeadact(String leadact) {
		this.leadact = leadact;
	}

	public String getActname() {
		return actname;
	}

	public void setActname(String actname) {
		this.actname = actname;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getGuid_6() {
		return guid_6;
	}

	public void setGuid_6(String guid_6) {
		this.guid_6 = guid_6;
	}

	public String getGuid_7() {
		return guid_7;
	}

	public void setGuid_7(String guid_7) {
		this.guid_7 = guid_7;
	}

	public String getGuid_8() {
		return guid_8;
	}

	public void setGuid_8(String guid_8) {
		this.guid_8 = guid_8;
	}

	public String getGuid_9() {
		return guid_9;
	}

	public void setGuid_9(String guid_9) {
		this.guid_9 = guid_9;
	}

	/**
	 * 片头曲时长-山西晋城
	 * @return
	 */
	public String getContentguid()
	{
		return contentguid;
	}

	/**
	 * 片头曲时长-山西晋城
	 * @return
	 */
	public void setContentguid(String contentguid)
	{
		this.contentguid = contentguid;
	}

	/**
	 * 片尾曲时长-山西晋城
	 * @return
	 */
	public String getContentkey()
	{
		return contentkey;
	}

	/**
	 * 片尾曲时长-山西晋城
	 * @return
	 */
	public void setContentkey(String contentkey)
	{
		this.contentkey = contentkey;
	}

	public String getLanguage()
	{
		return language;
	}

	public void setLanguage(String language)
	{
		this.language = language;
	}

	public String getMedia_type()
	{
		return media_type;
	}

	public void setMedia_type(String media_type)
	{
		this.media_type = media_type;
	}

	public Integer getP2p_id()
	{
		return p2p_id;
	}

	public void setP2p_id(Integer p2p_id)
	{
		this.p2p_id = p2p_id;
	}

	public Integer getReal_id()
	{
		return real_id;
	}

	public void setReal_id(Integer real_id)
	{
		this.real_id = real_id;
	}

	public String getRemote_path()
	{
		return remote_path;
	}

	public void setRemote_path(String remote_path)
	{
		this.remote_path = remote_path;
	}

	/** default constructor */
	public FileInfo()
	{
		this.is_checked =0;
	}

	/** minimal constructor */
	public FileInfo(Integer file_id)
	{
		this.file_id = file_id;
		this.is_checked =0;
	}

	/** full constructor */
	public FileInfo(Integer file_id, CatalogInfo catalogInfo,
			MountInfo mountInfo, String file_name, String file_alias,
			Integer cp_id, String file_path, String local_path,
			Integer upload_state, Integer if_drm, Integer file_type,
			Integer file_state, Integer if_segment, Integer parent_file_id,
			Integer start_time, Integer end_time, Integer file_size,
			Integer duration, Integer band_width, Timestamp record_time,
			String file_desc, String contentguid, String contentkey, 
			Integer preview_mount_id, String preview_file_path, String preview_local_path,Integer if_convert)
	{
		this.file_id = file_id;
		this.catalogInfo = catalogInfo;
		this.mountInfo = mountInfo;
		this.file_name = file_name;
		this.file_alias = file_alias;
		this.cp_id = cp_id;
		this.file_path = file_path;
		this.local_path = local_path;
		this.upload_state = upload_state;
		this.if_drm = if_drm;
		this.file_type = file_type;
		this.file_state = file_state;
		this.if_segment = if_segment;
		this.parent_file_id = parent_file_id;
		this.start_time = start_time;
		this.end_time = end_time;
		this.file_size = file_size;
		this.duration = duration;
		this.band_width = band_width;
		this.record_time = record_time;
		this.file_desc = file_desc;
		this.contentguid = contentguid;
		this.contentkey = contentkey;
		this.preview_mount_id = preview_mount_id;
		this.preview_file_path = preview_file_path;
		this.preview_local_path = preview_local_path;
		this.is_checked =0;
		this.if_convert =if_convert;
	}

	// Property accessors
	/**
	 * * 文件ID
	 */

	public Integer getFile_id()
	{
		return this.file_id;
	}

	public void setFile_id(Integer file_id)
	{
		this.file_id = file_id;
	}

	/**
	 * * 栏目ID
	 */

	public CatalogInfo getCatalogInfo()
	{
		return this.catalogInfo;
	}

	public void setCatalogInfo(CatalogInfo catalogInfo)
	{
		this.catalogInfo = catalogInfo;
	}

	/**
	 * * 文件发布点
	 */

	public MountInfo getMountInfo()
	{
		return this.mountInfo;
	}

	public void setMountInfo(MountInfo mountInfo)
	{
		this.mountInfo = mountInfo;
	}

	/**
	 * * 文件名称
	 */

	public String getFile_name()
	{
		return this.file_name;
	}

	public void setFile_name(String file_name)
	{
		this.file_name = file_name;
	}

	/**
	 * * 文件别名
	 */

	public String getFile_alias()
	{
		return this.file_alias;
	}

	public void setFile_alias(String file_alias)
	{
		this.file_alias = file_alias;
	}

	/**
	 * * 内容提供商ID
	 */

	public Integer getCp_id()
	{
		return this.cp_id;
	}

	public void setCp_id(Integer cp_id)
	{
		this.cp_id = cp_id;
	}

	/**
	 * * 文件路径
	 */

	public String getFile_path()
	{
		return this.file_path;
	}

	public void setFile_path(String file_path)
	{
		this.file_path = file_path;
	}

	/**
	 * * 本地文件路径
	 */

	public String getLocal_path()
	{
		return this.local_path;
	}

	public void setLocal_path(String local_path)
	{
		this.local_path = local_path;
	}

	/**
	 * * 上传状态 0：等待 1：正在上传 2：上传完成（已有文件） 3：上传失败
	 */

	public Integer getUpload_state()
	{
		return this.upload_state;
	}

	public void setUpload_state(Integer upload_state)
	{
		this.upload_state = upload_state;
	}

	/**
	 * * 是否加密
	 */

	public Integer getIf_drm()
	{
		return this.if_drm;
	}

	public void setIf_drm(Integer if_drm)
	{
		this.if_drm = if_drm;
	}

	/**
	 * * 栏目类型 点播(0) 广告(1)
	 */

	public Integer getFile_type()
	{
		return this.file_type;
	}

	public void setFile_type(Integer file_type)
	{
		this.file_type = file_type;
	}

	/**
	 * * 栏目类型 使用状态(0) 预删除状态(1)
	 */

	public Integer getFile_state()
	{
		return this.file_state;
	}

	public void setFile_state(Integer file_state)
	{
		this.file_state = file_state;
	}

	/**
	 * * 是否分段 0：主文件 1：分段文件
	 */

	public Integer getIf_segment()
	{
		return this.if_segment;
	}

	public void setIf_segment(Integer if_segment)
	{
		this.if_segment = if_segment;
	}

	/**
	 * * 主文件ID
	 */

	public Integer getParent_file_id()
	{
		return this.parent_file_id;
	}

	public void setParent_file_id(Integer parent_file_id)
	{
		this.parent_file_id = parent_file_id;
	}

	/**
	 * * 分段文件的开始时间 （毫秒）
	 */

	public Integer getStart_time()
	{
		return this.start_time;
	}

	public void setStart_time(Integer start_time)
	{
		this.start_time = start_time;
	}

	/**
	 * * 分段文件的结束时间 （毫秒）
	 */

	public Integer getEnd_time()
	{
		return this.end_time;
	}

	public void setEnd_time(Integer end_time)
	{
		this.end_time = end_time;
	}

	/**
	 * * 文件大小 单位 K
	 */

	public Integer getFile_size()
	{
		return this.file_size;
	}

	public void setFile_size(Integer file_size)
	{
		this.file_size = file_size;
	}

	/**
	 * * 文件时长，单位是秒
	 */

	public Integer getDuration()
	{
		return this.duration;
	}

	public void setDuration(Integer duration)
	{
		this.duration = duration;
	}

	/**
	 * * 带宽，单位是 Kbps
	 */

	public Integer getBand_width()
	{
		return this.band_width;
	}

	public void setBand_width(Integer band_width)
	{
		this.band_width = band_width;
	}

	/**
	 * * 入库时间
	 */

	public Timestamp getRecord_time()
	{
		return this.record_time;
	}

	public void setRecord_time(Timestamp record_time)
	{
		this.record_time = record_time;
	}

	/**
	 * * 文件描述
	 */

	public String getFile_desc()
	{
		return this.file_desc;
	}

	public void setFile_desc(String file_desc)
	{
		this.file_desc = file_desc;
	}

	public String getPreview_file_path()
	{
		return preview_file_path;
	}

	public void setPreview_file_path(String preview_file_path)
	{
		this.preview_file_path = preview_file_path;
	}

	public String getPreview_local_path()
	{
		return preview_local_path;
	}

	public void setPreview_local_path(String preview_local_path)
	{
		this.preview_local_path = preview_local_path;
	}

	public Integer getPreview_mount_id()
	{
		return preview_mount_id;
	}

	public void setPreview_mount_id(Integer preview_mount_id)
	{
		this.preview_mount_id = preview_mount_id;
	}

	public String getGuid_1()
	{
		return guid_1;
	}

	public void setGuid_1(String guid_1)
	{
		this.guid_1 = guid_1;
	}

	public String getGuid_2()
	{
		return guid_2;
	}

	public void setGuid_2(String guid_2)
	{
		this.guid_2 = guid_2;
	}

	public String getGuid_3()
	{
		return guid_3;
	}

	public void setGuid_3(String guid_3)
	{
		this.guid_3 = guid_3;
	}

	public String getGuid_4()
	{
		return guid_4;
	}

	public void setGuid_4(String guid_4)
	{
		this.guid_4 = guid_4;
	}

	public String getGuid_5()
	{
		return guid_5;
	}

	public void setGuid_5(String guid_5)
	{
		this.guid_5 = guid_5;
	}

	public Integer getIs_checked() {
		return is_checked;
	}

	public void setIs_checked(Integer is_checked) {
		this.is_checked = is_checked;
	}

	public String getIs_flv() {
		return is_flv;
	}

	public void setIs_flv(String is_flv) {
		this.is_flv = is_flv;
	}

	public String getVod_format() {
		return vod_format;
	}

	public void setVod_format(String vod_format) {
		this.vod_format = vod_format;
	}

	public String getVod_width_height() {
		return vod_width_height;
	}

	public void setVod_width_height(String vod_width_height) {
		this.vod_width_height = vod_width_height;
	}

	public Integer getIf_convert() {
		return if_convert;
	}

	public void setIf_convert(Integer if_convert) {
		this.if_convert = if_convert;
	}
	public String getSmallmagePath() {
		return smallmagePath;
	}

	public void setSmallmagePath(String smallmagePath) {
		this.smallmagePath = smallmagePath;
	}

	public String getLargeImagePath() {
		return largeImagePath;
	}

	public void setLargeImagePath(String largeImagePath) {
		this.largeImagePath = largeImagePath;
	}

	public String getSmallmageLocalPath() {
		return smallmageLocalPath;
	}

	public void setSmallmageLocalPath(String smallmageLocalPath) {
		this.smallmageLocalPath = smallmageLocalPath;
	}

	public String getLargeImageLocalPath() {
		return largeImageLocalPath;
	}

	public void setLargeImageLocalPath(String largeImageLocalPath) {
		this.largeImageLocalPath = largeImageLocalPath;
	}
	
	public String getFile_owner() {
		return file_owner;
	}

	public void setFile_owner(String file_owner) {
		this.file_owner = file_owner;
	}

	public String getFile_owner_name() {
		return file_owner_name;
	}

	public void setFile_owner_name(String file_owner_name) {
		this.file_owner_name = file_owner_name;
	}

	public Date getScheme_startTime() {
		return scheme_startTime;
	}

	public void setScheme_startTime(Date scheme_startTime) {
		this.scheme_startTime = scheme_startTime;
	}

	public Date getScheme_endTime() {
		return scheme_endTime;
	}

	public void setScheme_endTime(Date scheme_endTime) {
		this.scheme_endTime = scheme_endTime;
	}

}
