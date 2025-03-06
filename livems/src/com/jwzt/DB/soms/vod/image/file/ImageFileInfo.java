package com.jwzt.DB.soms.vod.image.file;
/**
 * 视频图片库实体类
 * @author 许业生 2009-11-04
 *
 */
public class ImageFileInfo implements java.io.Serializable{
	//图片ID
	private Integer img_id;
	//节目ID
	private Integer Catalog_id;
	//图片路径
	private String img_path = "";
	//图片类型
	private String img_type = "";
	//文件ID
	private Integer file_id;
	//图片名称
	private String img_name="";
	public Integer getImg_id() {
		return img_id;
	}
	public void setImg_id(Integer img_id) {
		this.img_id = img_id;
	}
	public Integer getCatalog_id() {
		return Catalog_id;
	}
	public void setCatalog_id(Integer catalog_id) {
		Catalog_id = catalog_id;
	}
	public String getImg_path() {
		return img_path;
	}
	public void setImg_path(String img_path) {
		this.img_path = img_path;
	}
	public String getImg_type() {
		return img_type;
	}
	public void setImg_type(String img_type) {
		this.img_type = img_type;
	}
	public Integer getFile_id() {
		return file_id;
	}
	public void setFile_id(Integer file_id) {
		this.file_id = file_id;
	}
	public String getImg_name() {
		return img_name;
	}
	public void setImg_name(String img_name) {
		this.img_name = img_name;
	}


}
