package com.bvRadio.iLive.manager.manager;

import java.util.List;

import com.alipay.api.domain.ForbbidenTime;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveManager;

public interface ILiveEnterpriseMng {
	public List<ILiveEnterprise> getList();
	public List<ILiveEnterprise> getList(ILiveEnterprise iLiveEnterprise);

	/**
	 * 获取企业
	 * 
	 * @param enterpriseId
	 * @return
	 */
	public ILiveEnterprise getILiveEnterpriseById(Integer enterpriseId);

	public Pagination getPage(String enterprisetype, String content, int pass, Integer pageNo, int pageSize);

	public Pagination getPage(String content, int pass, Integer pageNo, int pageSize);
	public Pagination getPage(ILiveEnterprise iLiveEnterprise, Integer pageNo, int pageSize);

	public void update(ILiveEnterprise iLiveEnterprise);

	public void del(Integer enterpriseId);

	public boolean saveEnterprise(ILiveEnterprise iLiveEnterprise);

	public boolean saveEnterpriseForPhone(ILiveEnterprise iLiveEnterprise, ILiveManager iLiveManagerRefresh);

	/***
	 * 根据企业状态获得企业
	 * 
	 * @param certIng
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Pagination getILiveEnterprisesByCertStatus(Integer certIng, Integer pageNo, Integer pageSize);

	public void updateEnterpriseWithPerson(ILiveEnterprise enterprise, List<ILiveManager> managerList);
	
	public Pagination getILiveEnterprisesByCertStatusByList(List<Integer> certStatusList,String enterpriseType,String content, Integer pageNo, Integer pageSize);
	public Pagination getILiveEnterprisesByCertStatusByList(List<Integer> certStatusList,String enterpriseType,String content, Integer pageNo, Integer pageSize,Integer stamp);

	public void forbidden(Integer enterpriseId,Integer status);
	public int getUserNum();
	public int getContractUserNum();
	
	/**
	 * 批量更新
	 * @param list
	 */
	void batchUpdate(List<ILiveEnterprise> list);
	
	void startDev(Integer enterpriseId);
	public Pagination getautoPage(ILiveEnterprise iLiveEnterprise, Integer pageNo, Integer pageSize);
	
}
