package com.bvRadio.iLive.iLive.timer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.bvRadio.iLive.iLive.entity.ILiveAPIGateWayRouter;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.manager.ILiveAPIGateWayRouterMng;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.jwzt.live4g.mgr.UmsMountInfo;
import com.jwzt.ums.api.UmsApi;

/**
 * 定时任务
 * @author Wei
 *
 */
public class TimerJob{
	
	@Autowired
	private ILiveLiveRoomMng iLiveLiveRoomMng;
	
	@Autowired
	private ILiveAPIGateWayRouterMng iLiveAPIGateWayRouterMng;
	
	@Autowired
	private ILiveServerAccessMethodMng accessMethodMng;
	
	@Autowired
	private RestTemplate restTemplate;
	
	private final Logger logger = LoggerFactory.getLogger(TimerJob.class);
	private final Map<Integer, String> markMap = new HashMap<>();
	public void checkRoom() {
		try {
			ILiveAPIGateWayRouter apiAddr = iLiveAPIGateWayRouterMng.get();
			
			
				Map<Integer, String> liveMap = new HashMap<>(); 
				Map<Integer, Set<String>> umsSetMap = new HashMap<>(); 
				List<ILiveLiveRoom> allLivingRoom = iLiveLiveRoomMng.getAllLivingRoom();
				for(ILiveLiveRoom room:allLivingRoom) {
					ILiveServerAccessMethod iLiveServerAccessMethod = accessMethodMng
							.getAccessMethodBySeverGroupId(room.getServerGroupId());
					UmsApi umsApi = new UmsApi(iLiveServerAccessMethod.getOrgLiveHttpUrl()
							, String.valueOf(iLiveServerAccessMethod.getUmsport()));
					Set<String> umsSet = new HashSet<>();
					List<UmsMountInfo> umsMountName = umsApi.getUmsMountName();
					if (umsMountName!=null && !umsMountName.isEmpty()) {
						for(UmsMountInfo info:umsMountName) {
							umsSet.add(info.getMountName());
						}
					}
					String pushAttr = "live" + room.getRoomId() + "_tzwj_5000k";
					liveMap.put(room.getRoomId(), pushAttr);
					umsSetMap.put(room.getRoomId(), umsSet);
				}
				Iterator<Entry<Integer, String>> iterator = liveMap.entrySet().iterator();
				while (iterator.hasNext()) {
					Map.Entry<Integer, String> entry = iterator.next();
					if (!umsSetMap.get(entry.getKey()).contains(entry.getValue())) {
						//判断是否在markMap中，如果在就删除，不在的话就添加，保证最少延迟15s再删除
						if (markMap.containsKey(entry.getKey())) {
							//当前直播间没有推流已经15s
							Map<String, String> params = new HashMap<>();
							params.put("roomId", entry.getKey()+"");
							params.put("playType", "1");
							
							try {
								HttpHeaders headers = new HttpHeaders();
								headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
								MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
								map.add("roomId",  entry.getKey()+"");
								map.add("playType", "1");
								HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
								ResponseEntity<String> postForEntity = restTemplate.postForEntity(apiAddr.getRouterUrl()+"/liveevent/live/pause.jspx", request, String.class);
								markMap.remove(entry.getKey());
							} catch (Exception e) {
								logger.info("");
								e.printStackTrace();
							}
							
						}else {
							//将当前直播间加入到markMap中
							markMap.put(entry.getKey(), entry.getValue());
						}
					}else {
						if (markMap.containsKey(entry.getKey())) {
							markMap.remove(entry.getKey());
						}
					}
				}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
