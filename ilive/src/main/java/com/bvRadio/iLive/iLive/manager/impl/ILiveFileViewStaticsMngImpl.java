package com.bvRadio.iLive.iLive.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.dao.ILiveFileViewStaticsDao;
import com.bvRadio.iLive.iLive.entity.ILiveFileViewStatics;
import com.bvRadio.iLive.iLive.manager.ILiveFileViewStaticsMng;

public class ILiveFileViewStaticsMngImpl implements ILiveFileViewStaticsMng {

	@Autowired
	private ILiveFileViewStaticsDao iLiveFileViewStaticsDao;

	@Override
	public ILiveFileViewStatics getViewNum(Long fileId) {
		return iLiveFileViewStaticsDao.getViewNum(fileId);
	}

	@Override
	@Transactional
	public void addViewNum(ILiveFileViewStatics viewStatics) {
		iLiveFileViewStaticsDao.addViewNum(viewStatics);
	}

	@Override
	@Transactional
	public void initViewNum(ILiveFileViewStatics viewStatics) {
		iLiveFileViewStaticsDao.initViewNum(viewStatics);
	}

	@Transactional
	public Long addNumCircle(Long fileId) {
		try {
			ILiveFileViewStatics viewNum = getViewNum(fileId);
			if (viewNum == null) {
				viewNum = new ILiveFileViewStatics();
				viewNum.setFileId(fileId);
				viewNum.setNumMount(1L);
				initViewNum(viewNum);
			} else {
				Long numMount = viewNum.getNumMount() + 1;
				viewNum.setNumMount(numMount);
				addViewNum(viewNum);
				return numMount;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1L;

	}

}
