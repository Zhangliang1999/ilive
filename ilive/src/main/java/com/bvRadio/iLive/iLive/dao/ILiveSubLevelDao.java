package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveGift;
import com.bvRadio.iLive.iLive.entity.ILiveManager;
import com.bvRadio.iLive.iLive.entity.ILiveSubLevel;

/**
 * 礼物管理
 * @author YanXL
 *
 */
public interface ILiveSubLevelDao {
	
	public List<ILiveSubLevel> selectILiveSubById(Long userId);

	public void save(ILiveSubLevel iLiveSubLevel);
	
	public Long selectMaxId();

	public void delete(Long userId);

	public void update(ILiveSubLevel iLiveSubLevel);

	public ILiveSubLevel getSubLevel(Long userId);

	public List<ILiveSubLevel> selectIfCan(Long userId, String permission);
}
