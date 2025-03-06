package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.iLive.action.front.vo.ILiveAppEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveEnterpriseFans;
import com.bvRadio.iLive.iLive.entity.ILiveManager;

public interface ILiveEnterpriseMng {

	public boolean saveILiveEnterprise(ILiveEnterprise iLiveEnterprise);

	/**
	 * 获得企业
	 * 
	 * @param enterpriseId
	 * @return
	 */
	public ILiveEnterprise getILiveEnterPrise(Integer enterpriseId);
	public ILiveEnterprise getILiveEnterPriseByAppId(String appId);
	public ILiveEnterprise getdefaultEnterprise();

	public List<ILiveAppEnterprise> getPagerForView(String keyWord, Integer pageNo, Integer pageSize,
			Integer searchType,Long userId);

	public List<ILiveEnterprise> getEnterPriseByIds(List<Integer> myEnterprise);

	public List<ILiveAppEnterprise> getTop4ForView(String keyword,Long userId);
	public List<ILiveAppEnterprise> getPagerForView1(String keyWord, Integer pageNo, Integer pageSize,
			Integer searchType,Long userId);
	
	/**
	 * 给手机app用的接口
	 * @param iLiveEnterprise
	 * @return
	 */
	public ILiveEnterprise saveILiveEnterpriseForPhone(ILiveEnterprise iLiveEnterprise);
	/**
	 * 给营业厅用的接口
	 * @param iLiveEnterprise
	 * @return
	 */
	public ILiveEnterprise saveILiveEnterpriseForyyt(ILiveEnterprise iLiveEnterprise);

	
	/**
	 * 给其他系统调用的接口
	 * @param enterpriseIds
	 * @return
	 */
	public List<ILiveAppEnterprise> getBatchEnterpriseForStatics(Integer startId,Integer size);

	
	/**
	 * 获取单个企业信息接口
	 * @param enterpriseId
	 * @return
	 */
	public List<ILiveAppEnterprise> geSingleEnterpriseForStatics(Integer[] enterpriseId);

	public void updateEnterpriseWithPerson(ILiveEnterprise enterprise, List<ILiveManager> managerList);
	
	public void update(ILiveEnterprise enterprise);

}
