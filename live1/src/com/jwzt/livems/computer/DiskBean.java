package com.jwzt.livems.computer;

import java.util.List;

/**
 * 对本地磁盘进行的封装
 * 
 * @author 赵飞洋
 * 
 */
public class DiskBean {
	private String disk_name;
	private String disk_path;
	private List<FileAttributeBean> fileList;

	public String getDisk_name() {
		return disk_name;
	}

	public void setDisk_name(String diskName) {
		disk_name = diskName;
	}

	public String getDisk_path() {
		return disk_path;
	}

	public void setDisk_path(String diskPath) {
		disk_path = diskPath;
	}

	public List<FileAttributeBean> getFileList() {
		return fileList;
	}

	public void setFileList(List<FileAttributeBean> fileList) {
		this.fileList = fileList;
	}

}
