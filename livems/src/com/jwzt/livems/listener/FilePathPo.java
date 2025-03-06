package com.jwzt.livems.listener;

public class FilePathPo {
 String path="";
 int repeatNum=0;
 String uuid = "";
public String getPath() {
	return path;
}
public void setPath(String path) {
	this.path = path;
}
public int getRepeatNum() {
	return repeatNum;
}
public void setRepeatNum(int repeatNum) {
	this.repeatNum = repeatNum;
}

public String getUuid() {
	return uuid;
}
public void setUuid(String uuid) {
	this.uuid = uuid;
}
@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((path == null) ? 0 : path.hashCode());
	return result;
}
@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	FilePathPo other = (FilePathPo) obj;
	if (path == null) {
		if (other.path != null)
			return false;
	} else if (!path.equals(other.path))
		return false;
	return true;
}
 
}
