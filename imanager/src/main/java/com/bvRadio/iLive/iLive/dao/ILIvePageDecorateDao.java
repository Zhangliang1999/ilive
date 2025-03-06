package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.PageDecorate;

public interface ILIvePageDecorateDao {

	public List<PageDecorate> getList(Long liveEventId);
	public List<PageDecorate> getAddList(Long liveEventId,Integer menuOrder);
	public List<PageDecorate> getListByIdAndType(Long liveEventId, Integer menuType);
	public List<PageDecorate> getListByIdAndTypeAndOrder(Long liveEventId, Integer menuOrder);
	public void removePageDecorate(Long eventId, Integer type);
	public void updatePageDecorate(Long liveEventId, Integer menuType, Integer menuOrder);
	public void addPageDecorate(PageDecorate page);
	public void updatePageDecorate(PageDecorate page);
	public void updatePageContent(PageDecorate page);
	public void updateOrderById(Long id,Integer menuOrder);
	public void removeById(Long id);
}
