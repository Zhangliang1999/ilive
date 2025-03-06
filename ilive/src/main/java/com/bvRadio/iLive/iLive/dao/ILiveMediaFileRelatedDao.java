package com.bvRadio.iLive.iLive.dao;

import java.util.List;

import com.bvRadio.iLive.common.hibernate3.Updater;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFileRelated;

public interface ILiveMediaFileRelatedDao {

	public ILiveMediaFileRelated save(ILiveMediaFileRelated bean);

	public Pagination pageByParams(Long mainFileId, int pageNo, int pageSize);

	public List<ILiveMediaFileRelated> listByParams(Long mainFileId);

	public ILiveMediaFileRelated updateByUpdater(Updater<ILiveMediaFileRelated> updater);

	public ILiveMediaFileRelated deleteById(String id);

}
