package com.bvRadio.iLive.manager.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;

public interface ILiveEnterpriseDao {
	
	public List<ILiveEnterprise> getList();
	public Pagination getPage(String enterprisetype,String content,int pass,Integer pageNo, int pageSize);
	public Pagination getPage(String content,int pass,Integer pageNo, int pageSize);
	public void update(ILiveEnterprise iLiveEnterprise);
	public void del(Integer enterpriseId);
	/**
	 * 获取企业
	 * @param enterpriseId
	 * @return
	 */
	public ILiveEnterprise getILiveEnterpriseById(Integer enterpriseId);
	
	
	/**
	 * 保存企业
	 * @param iLiveEnterprise
	 * @return
	 */
	public boolean saveILiveEnterprise(ILiveEnterprise iLiveEnterprise);
	
	
	
	/**
	 * 删除企业
	 */
	public boolean deleteILiveEnterprise(Integer enterpriseId);
	
	//修改整个实体
	public void updateEntity(ILiveEnterprise enterPrise);
}
