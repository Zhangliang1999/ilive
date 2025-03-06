package com.bvRadio.iLive.iLive.dao;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;

public interface ILiveEnterpriseDao {

	/**
	 * 获取企业
	 * 
	 * @param enterpriseId
	 * @return
	 */
	public ILiveEnterprise getILiveEnterpriseById(Integer enterpriseId);

	/**
	 * 保存企业
	 * 
	 * @param iLiveEnterprise
	 * @return
	 */
	public boolean saveILiveEnterprise(ILiveEnterprise iLiveEnterprise);

	/**
	 * 删除企业
	 */
	public boolean deleteILiveEnterprise(Integer enterpriseId);

	/**
	 * 获得企业列表
	 * @param keyWord
	 * @param pageNo
	 * @param pageSize
	 * @param searchType
	 * @return
	 */
	public Pagination getPagerForView(String keyWord, Integer pageNo, Integer pageSize, Integer searchType);

}
