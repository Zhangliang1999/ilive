package com.bvRadio.iLive.iLive.dao.impl;

import java.util.List;
import java.util.Map;

import com.bvRadio.iLive.common.hibernate3.Finder;
import com.bvRadio.iLive.common.hibernate3.HibernateBaseDao;
import com.bvRadio.iLive.common.page.Pagination;
import com.bvRadio.iLive.iLive.dao.ILiveVideoHistoryDao;
import com.bvRadio.iLive.iLive.entity.ILiveHistoryVideo;

public class ILiveVideoHistoryDaoImpl extends HibernateBaseDao<ILiveHistoryVideo, Long>
		implements ILiveVideoHistoryDao {

	@Override
	public Pagination getHistoryList(Integer roomId, Integer pageNo, Integer pageSize) {
		String hql = "select video from ILiveMediaFile file,ILiveHistoryVideo video where file.fileId=video.fileId and"
				+ " video.roomId=:roomId and file.onlineFlag=1 and file.delFlag=0 and video.delFlag=0 and file.mediaInfoStatus= 1 and file.mediaInfoDealState=1 order by video.fileOrder desc";
		Finder create = Finder.create(hql);
		create.setParam("roomId", roomId);
		Pagination find = this.find(create, pageNo, pageSize);
		return find;
	}

	@Override
	protected Class<ILiveHistoryVideo> getEntityClass() {
		return ILiveHistoryVideo.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Double getDownHistory(Double order, Integer roomId) {
		/**
		 * 由小变大
		 */
		String hql = "select video from ILiveMediaFile file,ILiveHistoryVideo video where file.fileId=video.fileId and "
				+ " video.roomId=:roomId  and file.delFlag=0 and video.delFlag=0 and file.onlineFlag=1 and file.mediaInfoStatus= 1 and file.mediaInfoDealState=1 and  video.fileOrder>=:fileOrder order by video.fileOrder asc limit 3";
		Finder finder = Finder.create(hql);
		finder.setParam("fileOrder", order);
		finder.setParam("roomId", roomId);
		List<ILiveHistoryVideo> find = this.find(finder);
		if (find.size() == 1) {
			return order;
		} else if (find.size() == 2) {
			double destOrder = find.get(1).getFileOrder() + 1;
			return destOrder;
		} else {
			double destOrder = (find.get(1).getFileOrder() + find.get(2).getFileOrder()) / 2d;
			return destOrder;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Double getFrontHistory(Double order, Integer roomId) {
		// 从大变小
		String hql = "select video from ILiveMediaFile file,ILiveHistoryVideo video where file.fileId=video.fileId and "
				+ " video.roomId=:roomId  and file.delFlag=0 and video.delFlag=0 and file.onlineFlag=1 and file.mediaInfoStatus= 1 and file.mediaInfoDealState=1 and  video.fileOrder<=:fileOrder order by video.fileOrder desc limit 3";
		Finder finder = Finder.create(hql);
		finder.setParam("fileOrder", order);
		finder.setParam("roomId", roomId);
		List<ILiveHistoryVideo> find = this.find(finder);
		if (find.size() == 1) {
			return order;
		} else if (find.size() == 2) {
			double destOrder = find.get(1).getFileOrder() / 2d;
			return destOrder;
		} else {
			double destOrder = (find.get(1).getFileOrder() + find.get(2).getFileOrder()) / 2d;
			return destOrder;
		}

	}

	@Override
	public ILiveHistoryVideo getHistory(Long historyId) {
		return this.get(historyId);
	}

	@Override
	public void updateILiveHistoryVideo(ILiveHistoryVideo video) {
		this.getSession().update(video);
	}

	@Override
	public ILiveHistoryVideo getHistoryByFileId(Long fileId, Integer roomId) {
		return (ILiveHistoryVideo) this.findUnique(" from ILiveHistoryVideo where fileId=? and roomId=? ", fileId,
				roomId);
	}

	@Override
	public void saveVideoHistory(ILiveHistoryVideo historyVideo) {
		this.getSession().save(historyVideo);
	}

	@Override
	public Pagination getMediaFilePageByRoom(Map<String, Object> sqlParam, int pageNo, int pageSize) {
		String hql = "select file from ILiveMediaFile file,ILiveHistoryVideo video where file.fileId=video.fileId and"
				+ " video.roomId=:roomId  and file.delFlag=0 and video.delFlag=0 and file.mediaInfoStatus= 1 and file.mediaInfoDealState=1 and file.onlineFlag=1 order by video.fileOrder desc";
		Finder create = Finder.create(hql);
		create.setParam("roomId", sqlParam.get("roomId"));
		Pagination find = this.find(create, pageNo, pageSize);
		return find;
	}

}
