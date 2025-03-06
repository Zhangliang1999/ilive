package com.bvRadio.iLive.iLive.manager;

import java.util.List;

import com.bvRadio.iLive.iLive.entity.ILiveUploadServer;
import com.bvRadio.iLive.common.page.Pagination;

public interface ILiveUploadServerMng {
	public Pagination getPage(int pageNo, int pageSize);

	public List<ILiveUploadServer> getList();

	public ILiveUploadServer findById(Integer id);

	public ILiveUploadServer findDefaultSever();

	public ILiveUploadServer save(ILiveUploadServer bean);

	public ILiveUploadServer update(ILiveUploadServer bean);

	public ILiveUploadServer deleteById(Integer id);

	public ILiveUploadServer[] deleteByIds(String[] split);

	public ILiveUploadServer getDefaultServer();

}