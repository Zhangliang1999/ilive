package com.jwzt.statistic.action.api;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.jwzt.common.page.Pagination;
import com.jwzt.common.utils.IpUtils;
import com.jwzt.common.utils.JsonUtils;
import com.jwzt.common.web.springmvc.RenderJsonUtils;
import com.jwzt.statistic.entity.LiveInfo;
import com.jwzt.statistic.entity.RequestLog;
import com.jwzt.statistic.entity.vo.LiveVideoViewRecord;
import com.jwzt.statistic.manager.LiveInfoMng;
import com.jwzt.statistic.manager.RequestLogMng;

@Controller
public class LiveAct {
	private static final Logger log = LogManager.getLogger();
	
	/**
	 * 开始直播
	 * @param liveInfoJson
	 * @param time
	 * @param ip
	 * @param request
	 * @param mp
	 * @return
	 */
	@RequestMapping("/live/begin")
	public String begin(@RequestBody String liveInfoJson, Long time, String ip, HttpServletRequest request,
			ModelMap mp) {
		try {
			log.debug("LiveAct.begin, liveInfoJson:{}", liveInfoJson);
			if (StringUtils.isBlank(ip)) {
				throw new Exception("Ip is blank.");
			}
			if (null == time) {
				throw new Exception("time is null.");
			}
			Map<?, ?> jsonMap = JsonUtils.jsonToMap(liveInfoJson);
			Integer roomId = (Integer) jsonMap.get("roomId");
			Object liveEventIdObj = jsonMap.get("liveEventId");
			Long liveEventId;
			if (null != liveEventIdObj && Long.class.isInstance(liveEventIdObj)) {
				liveEventId = (Long) liveEventIdObj;
			} else {
				liveEventId = ((Integer) liveEventIdObj).longValue();
			}
			Integer type = (Integer) jsonMap.get("type");
			String liveTitle = (String) jsonMap.get("liveTitle");
			String coverAddr = (String) jsonMap.get("coverAddr");
			Integer openDecorateString = (Integer) jsonMap.get("openDecorate");
			Boolean openDecorate = false;
			if (null != openDecorateString && openDecorateString.equals(1)) {
				openDecorate = true;
			}
			Integer baseNum = (Integer) jsonMap.get("baseNum");
			Integer multipleNum = (Integer) jsonMap.get("multipleNum");
			Map<?, ?> enterpriseMap = (Map<?, ?>) jsonMap.get("enterprise");
			Integer enterpriseId = (Integer) enterpriseMap.get("enterpriseId");
			String enterpriseName = (String) enterpriseMap.get("enterpriseName");
			String enterpriseDesc = (String) enterpriseMap.get("enterpriseDesc");
			String enterpriseImg = (String) enterpriseMap.get("enterpriseImg");
			long ipCode = IpUtils.ipToLong(ip);
			LiveInfo liveInfo = new LiveInfo(roomId, liveEventId, liveTitle, coverAddr, openDecorate, baseNum,
					multipleNum, enterpriseId, enterpriseName, enterpriseDesc, enterpriseImg);
			liveInfo.setLiveBeginTime(new Timestamp(time));
			liveInfo.setIpCode(ipCode);
			liveInfo = liveInfoMng.save(liveInfo);
			RequestLog requestLog = new RequestLog(roomId, liveEventId, null, null, type, ipCode);
			requestLog.setLogType(RequestLog.LOG_TYPE_LIVE_BEGIN);
			requestLog.setEnterpriseId(enterpriseId);
			requestLogMng.save(requestLog);
		} catch (Exception e) {
			log.warn("LiveAct.begin error", e);
			RenderJsonUtils.addError(mp, e.getMessage());
			return "renderJson";
		}
		RenderJsonUtils.addSuccess(mp);
		return "renderJson";
	}

	/**
	 * 结束直播
	 * @param liveInfoJson
	 * @param time
	 * @param ip
	 * @param request
	 * @param mp
	 * @return
	 */
	@RequestMapping("/live/end")
	public String end(@RequestBody String liveInfoJson, Long time, String ip, HttpServletRequest request, ModelMap mp) {
		try {
			log.debug("LiveAct.end, liveInfoJson:{}", liveInfoJson);
			if (StringUtils.isBlank(ip)) {
				throw new Exception("Ip is blank.");
			}
			if (null == time) {
				throw new Exception("time is null.");
			}
			Map<?, ?> jsonMap = JsonUtils.jsonToMap(liveInfoJson);
			Long liveEventId;
			Object liveEventIdObj = jsonMap.get("liveEventId");
			if (null != liveEventIdObj && Long.class.isInstance(liveEventIdObj)) {
				liveEventId = (Long) liveEventIdObj;
			} else {
				liveEventId = ((Integer) liveEventIdObj).longValue();
			}
			String liveTitle = (String) jsonMap.get("liveTitle");
			String coverAddr = (String) jsonMap.get("coverAddr");
			Integer openDecorateString = (Integer) jsonMap.get("openDecorate");
			Boolean openDecorate = false;
			if (null != openDecorateString && openDecorateString.equals(1)) {
				openDecorate = true;
			}
			Integer baseNum = (Integer) jsonMap.get("baseNum");
			Integer multipleNum = (Integer) jsonMap.get("multipleNum");
			Map<?, ?> enterpriseMap = (Map<?, ?>) jsonMap.get("enterprise");
			Integer enterpriseId = (Integer) enterpriseMap.get("enterpriseId");
			String enterpriseName = (String) enterpriseMap.get("enterpriseName");
			String enterpriseDesc = (String) enterpriseMap.get("enterpriseDesc");
			String enterpriseImg = (String) enterpriseMap.get("enterpriseImg");
			LiveInfo liveInfo = liveInfoMng.getBeanByLiveEventId(liveEventId);
			if (null == liveInfo) {
				RenderJsonUtils.addError(mp, "直播不存在");
				return "renderJson";
			}
			liveInfo.setLiveTitle(liveTitle);
			liveInfo.setCoverAddr(coverAddr);
			liveInfo.setOpenDecorate(openDecorate);
			liveInfo.setBaseNum(baseNum);
			liveInfo.setMultipleNum(multipleNum);
			liveInfo.setEnterpriseId(enterpriseId);
			liveInfo.setEnterpriseDesc(enterpriseDesc);
			liveInfo.setEnterpriseName(enterpriseName);
			liveInfo.setEnterpriseImg(enterpriseImg);
			liveInfo.setLiveEndTime(new Timestamp(time));
			liveInfoMng.update(liveInfo);
		} catch (Exception e) {
			log.debug("LiveAct.end error", e);
			RenderJsonUtils.addError(mp, e.getMessage());
			return "renderJson";
		}
		RenderJsonUtils.addSuccess(mp);
		return "renderJson";
	}

	@Autowired
	private LiveInfoMng liveInfoMng;
	@Autowired
	private RequestLogMng requestLogMng;
	
	@ResponseBody
	@RequestMapping(value="/live/getLiveViewList")
	public String getLiveViewList(Long userId,Integer roomId,Long videoId,Integer type,Integer pageNo,Integer pageSize,ModelMap mp,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			Pagination page = requestLogMng.getLiveViewList(userId, roomId,videoId, pageNo, pageSize,type);
			Pagination page2 = null;
			if (type==1) {
				page2 = requestLogMng.getLiveViewList(userId, roomId,videoId, pageNo, pageSize,3);
			}
			@SuppressWarnings("unchecked")
			List<RequestLog> list = (List<RequestLog>) page.getList();
			List<Object> viewList = new ArrayList<>();
			Iterator<RequestLog> iterator = list.iterator();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			DecimalFormat decimalFormat = new DecimalFormat("##0.00");
			while (iterator.hasNext()) {
				RequestLog requestLog = (RequestLog) iterator.next();
				LiveVideoViewRecord viewRecord = new LiveVideoViewRecord();
				viewRecord.setId(requestLog.getId());
				viewRecord.setRoomId(requestLog.getRoomId());
				viewRecord.setVideoId(requestLog.getVideoId());
				viewRecord.setUserId(String.valueOf(userId));
				viewRecord.setStartTime(format.format(requestLog.getCreateTime()));
				viewRecord.setStartTimetemp(requestLog.getCreateTime().getTime());
				if (type==1) {
					@SuppressWarnings("unchecked")
					RequestLog requestLog2 = getRequestLog((List<RequestLog>)page2.getList(),requestLog);
					if (requestLog2!=null&&requestLog2.getCreateTime()!=null) {
						viewRecord.setEndTime(format.format(requestLog2.getCreateTime()));
						viewRecord.setEndTimeTemp(requestLog2.getCreateTime().getTime());
						float temp = viewRecord.getEndTimeTemp() - viewRecord.getStartTimetemp();
						viewRecord.setDuration(temp/(1000 * 60 *60));
						viewRecord.setDurationStr(decimalFormat.format(temp/(1000 * 60 *60)));
					}
				}
				viewList.add(viewRecord);
			}
			
			String json = JSONObject.toJSONString(viewList);
			result.put("code", "0");
			result.put("msg", "获取成功");
			result.put("data", json);
			result.put("totalCount", page.getTotalCount());
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", "1");
			result.put("msg", "获取失败");
			result.put("data", "");
		}
		return result.toString();
	}
	@ResponseBody
	@RequestMapping(value="/live/getLiveViewList4Export")
	public String getLiveViewList4Export(Long userId,Integer roomId,Long videoId,Integer type,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			List<RequestLog> list = requestLogMng.getLiveViewList(userId, roomId,videoId,type);
			List<RequestLog> liveViewList2 = null;
			if (type==1) {
				liveViewList2 = requestLogMng.getLiveViewList(userId, roomId,videoId,3);
			}
			@SuppressWarnings("unchecked")
			List<Object> viewList = new ArrayList<>();
			Iterator<RequestLog> iterator = list.iterator();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			DecimalFormat decimalFormat = new DecimalFormat("##0.00");
			while (iterator.hasNext()) {
				RequestLog requestLog = (RequestLog) iterator.next();
				LiveVideoViewRecord viewRecord = new LiveVideoViewRecord();
				viewRecord.setId(requestLog.getId());
				viewRecord.setRoomId(requestLog.getRoomId());
				viewRecord.setVideoId(requestLog.getVideoId());
				viewRecord.setUserId(String.valueOf(userId));
				viewRecord.setStartTime(format.format(requestLog.getCreateTime()));
				viewRecord.setStartTimetemp(requestLog.getCreateTime().getTime());
				if (type==1) {
					RequestLog requestLog2 = getRequestLog(liveViewList2,requestLog);
					if (requestLog2!=null&&requestLog2.getCreateTime()!=null) {
						viewRecord.setEndTime(format.format(requestLog2.getCreateTime()));
						viewRecord.setEndTimeTemp(requestLog2.getCreateTime().getTime());
						float temp = viewRecord.getEndTimeTemp() - viewRecord.getStartTimetemp();
						viewRecord.setDuration(temp/(1000 * 60 *60));
						viewRecord.setDurationStr(decimalFormat.format(temp/(1000 * 60 *60)));
					}
				}
				viewList.add(viewRecord);
			}
			
			String json = JSONObject.toJSONString(viewList);
			result.put("code", "0");
			result.put("msg", "获取成功");
			result.put("data", json);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", "1");
			result.put("msg", "获取失败");
			result.put("data", "");
		}
		return result.toString();
	}
	
	private RequestLog getRequestLog(List<RequestLog> list,RequestLog requestLog) {
		try {
			Iterator<RequestLog> iterator = list.iterator();
			while(iterator.hasNext()) {
				RequestLog next = iterator.next();
				if (next.getIpCode().equals(requestLog.getIpCode())
						&&next.getRoomId().intValue()==requestLog.getRoomId().intValue()
						&&next.getLiveEventId().intValue() == requestLog.getLiveEventId().intValue()
						&&next.getCreateTime().getTime()>requestLog.getCreateTime().getTime()) {
					iterator.remove();
					return next;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	@RequestMapping(value="/live/getRequestLogList")
	public String getRequestLogList(Long roomId,Long videoId,String startTime,String endTime,ModelMap mp,HttpServletResponse response) {
		//提供API给第三放系统查询访问记录
		try {
			
			if(roomId == null){
				RenderJsonUtils.addError(mp,"参数错误");
			}else{
				java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				Date dStartDate = sf.parse(startTime);
				Date dEndDate = sf.parse(endTime);
				
				
				
				//判断一下如果超过6小时不执行查询，保护系统
				if(dEndDate.getTime() - dStartDate.getTime()>6*60*60*1000){
					RenderJsonUtils.addError(mp,"参数错误,查询最大时间长度6小时");
				}else{
					List<RequestLog> list =  requestLogMng.listByParams(roomId , null, null, videoId, null, null, dStartDate, dEndDate, false, false, false);
					RenderJsonUtils.addSuccess(mp, list);
				}
			}

		}
		catch (ParseException e) {
			e.printStackTrace();
			RenderJsonUtils.addError(mp,"参数错误");
		}
		
		catch (Exception e) {
			e.printStackTrace();
			RenderJsonUtils.addError(mp,"获取失败");
		}
		return "renderJsonData";
	}
}
