package com.bvRadio.iLive.iLive.manager;

import java.util.List;
import java.util.Map;

import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.action.front.vo.AppMediaFile;
import com.bvRadio.iLive.iLive.entity.ILiveHistoryVideo;

public interface ILiveVideoHistoryMng {

	Pagination getHistoryList(Integer roomId,Integer pageNo,Integer pageSize);

	void moveUp(Long historyId,Integer roomId);

	void moveDown(Long historyId,Integer roomId);

	void deleteHistory(Long historyId);

	boolean batchAddHistory(Integer roomId, Long[] ids, Long userId);

	void saveVideoHistory(Integer roomId, Long saveIliveMediaFile, long userId);

	List<AppMediaFile> getMediaFilePageByRoom(Map<String, Object> sqlParam, int pageNo, int pageSize,Integer roomId);

	ILiveHistoryVideo getViewHistoryByFileId(Long fileId, Integer roomId);

	void test();
}
