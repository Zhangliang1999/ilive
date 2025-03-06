package com.jwzt.statistic.dao;

import java.util.Date;
import java.util.List;

import com.jwzt.common.page.Pagination;
import com.jwzt.statistic.entity.RequestLog;
import com.jwzt.statistic.entity.vo.RankInfo;

public interface RequestLogDao {
	public List<Long> listNullIpCode();

	public List<RankInfo> listRankGroupByUser(Long liveEventId, Integer enterpriseId, Long videoId, Integer[] logTypes,
			Date startTime, Date endTime, Integer topNum);

	public Pagination pageByParams(Long liveEventId, Integer enterpriseId, Long videoId, Integer[] logTypes,
			String[] userIds, Date startTime, Date endTime, int pageNo, int pageSize);

	public List<RequestLog> listByParams(Long liveEventId, Integer enterpriseId, Long videoId, Integer[] logTypes,
			String[] userIds, Date startTime, Date endTime);

	public Long countUserNum(Long liveEventId, Integer enterpriseId, Long videoId, Integer requestType,
			Integer[] logTypes, Date startTime, Date endTime);

	public Long countRequestNum(Long liveEventId, Integer enterpriseId, Long videoId, Integer requestType ,Integer[] logTypes,
			Date startTime, Date endTime);

	public RequestLog save(RequestLog bean);

	public RequestLog update(RequestLog bean);
	
	public Pagination getLiveViewList(Long userId,Integer roomId,Long videoId,Integer pageNo,Integer pageSize,Integer type);

	public List<RequestLog> getLiveViewList(Long userId, Integer roomId, Long videoId, Integer type);
	
	public Integer getNumByEvent(Long eventId, Integer type);
	public Integer getNumByEvent(Long eventId, Integer type, boolean isDistinct);
	
	public Integer getNumByRoom(Integer roomId, Integer type);

	public List<RequestLog> listByParams(Long roomId, Long liveEventId, Integer enterpriseId, Long videoId, Integer[] logTypes,
			String[] userIds, Date startTime, Date endTime);
}