package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.bvRadio.iLive.iLive.entity.ILiveSubLevel;

public interface ILiveSubLevelMng {
	public List<ILiveSubLevel> selectILiveSubById(Long userId);

	public void save(ILiveSubLevel iLiveSubLevel);
	
	public Long selectMaxId();

	public void delete(Long userId);

	public void update(ILiveSubLevel iLiveSubLevel);

	public ILiveSubLevel getSubLevel(Long userId);
	/**
	 * 根据子账户ID获取其是否具有某个权限
	 * 
	 */
	public boolean selectIfCan(HttpServletRequest request,String permission);

	public boolean selectAppIfCan(HttpServletRequest request, String enterpriseFunctionLiveroom);
}
