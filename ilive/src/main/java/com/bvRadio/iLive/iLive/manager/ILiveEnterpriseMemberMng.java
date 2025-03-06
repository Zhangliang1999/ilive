package com.bvRadio.iLive.iLive.manager;

import java.util.List;
import java.util.Map;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseWhiteBill;
import com.bvRadio.iLive.iLive.entity.ILiveViewWhiteBill;
import com.bvRadio.iLive.iLive.entity.UserBean;

public interface ILiveEnterpriseMemberMng {

	public Pagination getPage(String queryName,Integer pageNo, Integer pageSize,Integer enterpriseId);
	public void deleteWhite(Integer whitebillId);
	public void addWhite(ILiveEnterpriseWhiteBill white);
	public void batchInsertUser( List<Object[]> readXlsx, UserBean userBean,Long iLiveEventId);
	public List<ILiveEnterpriseWhiteBill> getList(Integer enterpriseId);
	public void deleteAll(Integer enterpriseId);
	public void addWhite(Map<String, ILiveViewWhiteBill> map,Long iLiveEventId);
	public List<ILiveEnterpriseWhiteBill>  getEnterpriseWhite(Integer enterpriseId, String phoneNum);
}
