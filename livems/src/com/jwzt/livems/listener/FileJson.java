package com.jwzt.livems.listener;


public class FileJson implements  java.io.Serializable {
 /**
	 * 
	 */
	private static final long serialVersionUID = -679603025705506851L;
public  String fileName;
 public  Integer bandWidth;
public String getFileName() {
	return fileName;
}
public void setFileName(String fileName) {
	this.fileName = fileName;
}
public Integer getBandWidth() {
	return bandWidth;
}
public void setBandWidth(Integer bandWidth) {
	this.bandWidth = bandWidth;
}

 
}
