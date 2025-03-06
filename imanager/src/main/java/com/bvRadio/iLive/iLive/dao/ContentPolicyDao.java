package com.bvRadio.iLive.iLive.dao;

import com.bvRadio.iLive.iLive.entity.ContentPolicy;

public interface ContentPolicyDao {
	public ContentPolicy getPolicy();
	public void updatePolicy(ContentPolicy policy);
	public void save(ContentPolicy policy);
	public ContentPolicy getPolicyByShows(Integer shows,Integer enterpriseId);
	public ContentPolicy gettitle2(Integer shows);
	public void saveTitle2(ContentPolicy policy);
	public void updateTitle2(String title2,Integer shows,Integer enterpriseId);
	public ContentPolicy gettitle4(Integer shows);
	public void saveLink(ContentPolicy policy);
	public void updateLink(String link, String linkName,Integer enterpriseId,Integer shows);
	public void deletelink(Integer enterpriseId,Integer shows);
	public void updateImg(String imgurl,Integer enterpriseId);
	public void deleteimg(Integer enterpriseId);
}
