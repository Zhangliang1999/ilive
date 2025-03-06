package com.bvRadio.iLive.iLive.manager.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILIvePageDecorateDao;
import com.bvRadio.iLive.iLive.entity.PageDecorate;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.ILivePageDecorateMng;

@Service
@Transactional
public class ILivePageDecorateMngImpl implements ILivePageDecorateMng {

	@Autowired
	private ILIvePageDecorateDao liveDecorateDao;

	@Autowired
	private ILiveFieldIdManagerMng filedIdMng;

	@Override
	@Transactional(readOnly = true)
	public List<PageDecorate> getList(Long liveEventId) {
		return liveDecorateDao.getList(liveEventId);
	}

	@Override
	public void removePageDecorate(Long eventId, Integer type) {
		liveDecorateDao.removePageDecorate(eventId, type);
	}

	@Override
	public void sort(Long liveEventId, Integer menuOrder) {
		List<PageDecorate> list = getListByIdAndTypeAndOrder(liveEventId, menuOrder);
		if (list.size() > 0) {
			for (PageDecorate p : list) {
				liveDecorateDao.updatePageDecorate(liveEventId, p.getMenuType(), p.getMenuOrder() - 1);
			}
		}
	}

	@Override
	public List<PageDecorate> getListByIdAndType(Long liveEventId, Integer menuType) {
		List<PageDecorate> list = liveDecorateDao.getListByIdAndType(liveEventId, menuType);
		return list;
	}

	@Override
	public List<PageDecorate> getListByIdAndTypeAndOrder(Long liveEventId, Integer menuOrder) {
		List<PageDecorate> list = liveDecorateDao.getListByIdAndTypeAndOrder(liveEventId, menuOrder);
		return list;
	}
	
	@Override
	public void addPageDecorateInit(PageDecorate page) {
		Long nextId = filedIdMng.getNextId("ilive_page_decorate", "id", 1);
		page.setId(nextId);
		liveDecorateDao.addPageDecorate(page);
	}

	@Transactional
	@Override
	public Integer addPageDecorate(PageDecorate page,List<Map<String, Integer>> list) {
		Long nextId = filedIdMng.getNextId("ilive_page_decorate", "id", 1);
		page.setId(nextId);
		liveDecorateDao.addPageDecorate(page);
		if(list.size()>0) {
			for(Map<String, Integer> map:list) {
				// System.out.println(" service:  "+map.get("id")+"  "+map.get("order"));
				Integer id =  map.get("id");
				Integer order = map.get("order");
				long idLong = (int)id;
				liveDecorateDao.updateOrderById(idLong,order);
			}
		}
		return nextId.intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void sortAdd(PageDecorate page) {
		List<PageDecorate> list = liveDecorateDao.getAddList(page.getLiveEventId(), page.getMenuOrder());
		if (list.size() > 0) {
			for (PageDecorate p : list) {
				// // System.out.println(p.getMenuOrder()+" "+p.getMenuName());
				liveDecorateDao.updatePageDecorate(p.getLiveEventId(), p.getMenuType(), p.getMenuOrder() + 1);
			}
		}
	}

	@Override
	public void updatePageDecorate(PageDecorate page) {
		liveDecorateDao.updatePageDecorate(page);
	}

	@Override
	public void removeById(Long id) {
		liveDecorateDao.removeById(id);
	}

	@Override
	public void updateAndSetIndex(PageDecorate page, List<Map<String, Integer>> list) {
		liveDecorateDao.updatePageContent(page);
		if(list.size()>0) {
			for(Map<String, Integer> map:list) {
				// System.out.println(" service:  "+map.get("id")+"  "+map.get("order"));
				Integer id =  map.get("id");
				Integer order = map.get("order");
				long idLong = (int)id;
				liveDecorateDao.updateOrderById(idLong,order);
			}
		}
	}

	@Override
	public void setIndex(List<Map<String, Integer>> list) {
		if(list.size()>0) {
			for(Map<String, Integer> map:list) {
				// System.out.println(" service:  "+map.get("id")+"  "+map.get("order"));
				Integer id =  map.get("id");
				Integer order = map.get("order");
				long idLong = (int)id;
				liveDecorateDao.updateOrderById(idLong,order);
			}
		}
		
	}

	@Override
	public PageDecorate getPageDecorateByEventIdAndType(PageDecorate page) {
		return liveDecorateDao.getPageDecorateByEventIdAndType(page);
	}

}
