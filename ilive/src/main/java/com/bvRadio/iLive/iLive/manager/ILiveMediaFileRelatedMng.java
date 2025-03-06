package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.action.front.vo.AppMediaFile;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFileRelated;

public interface ILiveMediaFileRelatedMng {
	public List<ILiveMediaFileRelated> save(Long mainFileId, Long[] relatedFileIds);

	public ILiveMediaFileRelated save(ILiveMediaFileRelated bean);

	public Pagination pageByParams(Long mainFileId, int pageNo, int pageSize);

	public List<ILiveMediaFileRelated> listByParams(Long mainFileId);
	
	public List<AppMediaFile> listForApp(Long mainFileId, int pageNo, int pageSize);

	public ILiveMediaFileRelated update(ILiveMediaFileRelated bean);

	public ILiveMediaFileRelated deleteById(String id);

	public ILiveMediaFileRelated[] deleteByIds(String[] ids);
}
