package com.bvRadio.iLive.iLive.dao;

import java.util.Map;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.entity.ILiveHistoryVideo;

public interface ILiveVideoHistoryDao {

	Pagination getHistoryList(Integer roomId,Integer pageNo,Integer pageSize);

	Double getDownHistory(Double order,Integer roomId);

	Double getFrontHistory(Double order,Integer roomId);

	ILiveHistoryVideo getHistory(Long historyId);

	void updateILiveHistoryVideo(ILiveHistoryVideo video);

	ILiveHistoryVideo getHistoryByFileId(Long fileId, Integer roomId);

	void saveVideoHistory(ILiveHistoryVideo historyVideo);

	Pagination getMediaFilePageByRoom(Map<String, Object> sqlParam, int pageNo, int pageSize);

}
