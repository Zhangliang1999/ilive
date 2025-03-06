package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveUploadServer;
import com.bvRadio.iLive.common.hibernate3.Updater;
import com.bvRadio.iLive.common.page.Pagination;

public interface ILiveUploadServerDao {
	public Pagination getPage(int pageNo, int pageSize);

	public List<ILiveUploadServer> getList();

	public ILiveUploadServer findById(Integer id);

	public ILiveUploadServer findDefaultSever();

	public ILiveUploadServer save(ILiveUploadServer bean);

	public ILiveUploadServer updateByUpdater(Updater<ILiveUploadServer> updater);

	public ILiveUploadServer deleteById(Integer id);

	public void update(ILiveUploadServer bean);

	public ILiveUploadServer getDefaultServer();
}