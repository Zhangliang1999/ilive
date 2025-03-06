package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveGift;

public interface ILiveGiftDao {
    public void save(ILiveGift iLiveGift);//添加
    public ILiveGift delete(Integer id);//删除
	public ILiveGift findById(Integer id);//根据ID查询
	public Pagination getPage(int pageNo, int pageSize);//查询全部
	public void update(ILiveGift bean);//修改
	public void deleteByIds(Integer[] ids);//批量删除
	
	/**
	 * 获取所有；礼物
	 * @return
	 */
	public List<ILiveGift> selectILiveGiftList();
}
