package com.bvRadio.iLive.iLive.entity;

import java.sql.Timestamp;
import com.bvRadio.iLive.iLive.entity.base.BaseILiveSensitiveWord;

@SuppressWarnings("serial")
public class ILiveSensitiveWord extends BaseILiveSensitiveWord implements java.io.Serializable {

	public String replaceSensitiveWord(String origStr) {
		String replaceStr = origStr;
		if (null != origStr && null != getSensitiveName()) {
			replaceStr = origStr.replace(getSensitiveName(), "***");
		}
		return replaceStr;
	}

	public ILiveSensitiveWord() {
	}

	public ILiveSensitiveWord(Integer id) {
		super(id);
	}

	public ILiveSensitiveWord(Integer id, String sensitiveName, String sensitiveDesc, Timestamp createTime) {
		super(id, sensitiveName, sensitiveDesc, createTime);
	}

}
