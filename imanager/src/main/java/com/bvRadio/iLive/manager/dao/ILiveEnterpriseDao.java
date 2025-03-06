package com.bvRadio.iLive.manager.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;

public interface ILiveEnterpriseDao {

	public List<ILiveEnterprise> getList();
	public List<ILiveEnterprise> getList(ILiveEnterprise iLiveEnterprise);

	public Pagination getPage(String enterprisetype, String content, int pass, Integer pageNo, int pageSize);

	public Pagination getPage(String content, int pass, Integer pageNo, int pageSize);
	public Pagination getPage(ILiveEnterprise iLiveEnterprise, Integer pageNo, int pageSize);

	public void update(ILiveEnterprise iLiveEnterprise);

	public void del(Integer enterpriseId);

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
	 * 根据企业状态获得企业列表
	 * 
	 * @param certStauts
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Pagination getILiveEnterprisesByCertStatus(Integer certStauts, Integer pageNo, Integer pageSize);

	
	/**
	 * 修改企业bean
	 * @param iLiveEnterprise
	 */
	public void updateByBean(ILiveEnterprise iLiveEnterprise);
	
	/**
	 * 获取认证中与未认证的
	 * @param certStatusList
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Pagination getILiveEnterprisesByCertStatusByList(List<Integer> certStatusList,String enterpriseType,String content, Integer pageNo, Integer pageSize);
	/**
	 * 获取认证中与未认证的
	 * @param certStatusList
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Pagination getILiveEnterprisesByCertStatusByList(List<Integer> certStatusList,String enterpriseType,String content, Integer pageNo, Integer pageSize,Integer stamp);

	/**
	 * 得到商户总数
	 * @return
	 */
	public int getUserNum();
	/**
	 * 得到签约商户总数
	 */
	public int getContractUserNum();
	public Pagination getPageByAppId(String appID, Integer pageNo, Integer pageSize);
	public Pagination getautoPage(ILiveEnterprise iLiveEnterprise, Integer pageNo, Integer pageSize);

}
