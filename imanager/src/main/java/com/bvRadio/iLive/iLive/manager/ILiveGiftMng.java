package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveGift;

public interface ILiveGiftMng {
    public void save(ILiveGift iLiveGift);//添加
    public void delete(Integer id);//删除
    public ILiveGift findById(Integer id);//根据ID查询
    public Pagination getPage(int pageNo, int pageSize);//查询全部
	public void update(ILiveGift bean);//修改
	public void deleteByIds(Integer[] ids);//批量删除
	/**
	 * 查询所有礼物信息
	 * @return
	 */
	public List<ILiveGift> findGiftList();
}
