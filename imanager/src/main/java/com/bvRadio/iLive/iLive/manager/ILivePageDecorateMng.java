package com.bvRadio.iLive.iLive.manager;

import java.util.List;
import java.util.Map;

import com.bvRadio.iLive.iLive.entity.PageDecorate;

public interface ILivePageDecorateMng {

	public List<PageDecorate> getList(Long liveEventId);
	public List<PageDecorate> getListByIdAndType(Long liveEventId, Integer menuType);
	public List<PageDecorate> getListByIdAndTypeAndOrder(Long liveEventId,Integer menuOrder);
	public void removePageDecorate(Long eventId,Integer type);
	public void sort(Long liveEventId,Integer menuOrder);
	public Integer addPageDecorate(PageDecorate page,List<Map<String, Integer>> list);
	public void sortAdd(PageDecorate page);
	public void updatePageDecorate(PageDecorate page);
	public void addPageDecorateInit(PageDecorate page);
	public void removeById(Long id);
	public void updateAndSetIndex(PageDecorate page,List<Map<String, Integer>> list);
	public void setIndex(List<Map<String, Integer>> list);
	
	
}
