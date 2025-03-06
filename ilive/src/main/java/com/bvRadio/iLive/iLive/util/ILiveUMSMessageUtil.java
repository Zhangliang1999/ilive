package com.bvRadio.iLive.iLive.util;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveServerAccessMethod;
import com.bvRadio.iLive.iLive.manager.ILiveLiveRoomMng;
import com.bvRadio.iLive.iLive.manager.ILiveServerAccessMethodMng;
import com.bvRadio.iLive.iLive.manager.impl.ILiveLiveRoomMngImpl;
import com.bvRadio.iLive.iLive.manager.impl.ILiveServerAccessMethodMngImpl;
import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.jwzt.live4g.mgr.UmsMountInfo;
import com.jwzt.ums.api.UmsApi;

/**
 * 与UMS进行交互的工具类
 * 
 * @author administrator
 *
 */
public enum ILiveUMSMessageUtil {

	INSTANCE;

	Logger logger = LoggerFactory.getLogger(ILiveUMSMessageUtil.class);

	private ConcurrentHashMap<Integer, Long> roomStreamTimeCache = new ConcurrentHashMap<>();

	/**
	 * 开始直播
	 * 
	 * @param aceessMethod
	 * @param iLiveLiveRoom
	 * @return
	 */
	public boolean startPlay(ILiveServerAccessMethod aceessMethod, ILiveLiveRoom iLiveLiveRoom) throws Exception {
		UmsApi umsApi = new UmsApi(aceessMethod.getOrgLiveHttpUrl(), String.valueOf(aceessMethod.getUmsport()));
		boolean openMountPlay = umsApi.openMountPlay("live", "live" + iLiveLiveRoom.getRoomId() + "_tzwj_5000k");
		if (!openMountPlay) {
			throw new RuntimeException("开始推流失败");
		}
		return openMountPlay;
	}

	/**
	 * 停止直播
	 * 
	 * @param aceessMethod
	 * @param iLiveLiveRoom
	 * @return
	 */
	public boolean stopPlay(ILiveServerAccessMethod aceessMethod, ILiveLiveRoom iLiveLiveRoom) throws Exception {
		UmsApi umsApi = new UmsApi(aceessMethod.getOrgLiveHttpUrl(), String.valueOf(aceessMethod.getUmsport()));
		boolean openMountPlay = umsApi.closeMountPlay("live", "live" + iLiveLiveRoom.getRoomId() + "_tzwj_5000k");
		if (!openMountPlay) {
			throw new RuntimeException("停止推流失败");
		}
		return openMountPlay;
	}

	/**
	 * 拉流检测
	 * 
	 * @param aceessMethod
	 * @param iLiveLiveRoom
	 * @return
	 */
	public boolean pullStream(String pullStreamAddr, ILiveServerAccessMethod aceessMethod, ILiveLiveRoom iLiveLiveRoom)
			throws Exception {
		UmsApi umsApi = new UmsApi(aceessMethod.getOrgLiveHttpUrl(), String.valueOf(aceessMethod.getUmsport()));
		boolean openMountPlay = umsApi.openMountPlay("live", "live" + iLiveLiveRoom.getRoomId() + "_tzwj_5000k");
		if (openMountPlay) {
			boolean setRelayAddr = umsApi.SetRelayAddr("live", "live" + iLiveLiveRoom.getRoomId() + "_tzwj_5000k",
					pullStreamAddr);
			if (!setRelayAddr) {
				throw new RuntimeException("拉流失败");
			} else {
				return true;
			}
		}
		return false;
	}

	/**
	 * 关闭拉流
	 */
	public boolean closePullStream(ILiveServerAccessMethod aceessMethod, ILiveLiveRoom iLiveLiveRoom) {
		UmsApi umsApi = new UmsApi(aceessMethod.getOrgLiveHttpUrl(), String.valueOf(aceessMethod.getUmsport()));
		boolean setRelayAddr = umsApi.SetRelayAddr("live", "live" + iLiveLiveRoom.getRoomId() + "_tzwj_5000k", "");
		if (!setRelayAddr) {
			throw new RuntimeException("断开拉流失败");
		} else {
			return true;
		}
	}

	/**
	 * 房间断流检测机制
	 * 
	 * @param args
	 */
	public void monitorStreamDown() {
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		if (null == context) {
			return;
		}
		ILiveServerAccessMethodMng accessMng = context.getBean(ILiveServerAccessMethodMngImpl.class);
		//String serverGroupId = ConfigUtils.get("defaultLiveServerGroupId");
		
		
			ILiveLiveRoomMng roomMng = context.getBean(ILiveLiveRoomMngImpl.class);
			List<ILiveLiveRoom> livingRooms = roomMng.getAllLivingRoom();
			if (livingRooms != null && !livingRooms.isEmpty()) {
				for (ILiveLiveRoom room : livingRooms) {
					ILiveServerAccessMethod aceessMethod = accessMng.getAccessMethodBySeverGroupId(room.getServerGroupId());
					UmsApi umsApi = new UmsApi(aceessMethod.getOrgLiveHttpUrl(), String.valueOf(aceessMethod.getUmsport()));
					List<UmsMountInfo> umsMountName = umsApi.getUmsMountName();
					if (umsMountName != null && !umsMountName.isEmpty()) {
					Integer roomId = room.getRoomId();
					String roomMountName = "live" + roomId + "_tzwj_5000k";
					boolean exist = false;
					for (UmsMountInfo mountInfo : umsMountName) {
						String mountName = mountInfo.getMountName();
						if (mountName.equals(roomMountName)) {
							exist = true;
							break;
						}
					}
					if (!exist) {
						Long timeCache = roomStreamTimeCache.get(roomId);
						if (timeCache == null) {
							roomStreamTimeCache.put(roomId, System.currentTimeMillis());
						} else {
							// 以s为单位
							String streamDisconnectThreshold = ConfigUtils.get("streamDisconnectThreshold");
							Integer streamDisconnectThresholdInt = Integer.parseInt(streamDisconnectThreshold);
							if (System.currentTimeMillis() - timeCache / 1000 > streamDisconnectThresholdInt) {
								// 开启结束直播机制
								roomMng.stopLive(room);
								roomStreamTimeCache.remove(roomId);
							}
						}
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		UmsApi umsApi = new UmsApi("source.live01.zb.tv189.com", "1935");
		 boolean openMountPlay = umsApi.openMount("live", "live" + 797 +
		 "_tzwj_5000k");
		 System.out.println(openMountPlay);
//		 boolean setRelayAddr = umsApi.SetRelayAddr("live", "live" + 1173 +
//		 "_tzwj_5000k",
//		 "rtmp://live.hkstv.hk.lxdns.com/live/hks");
//		 System.out.println(setRelayAddr);
		List<UmsMountInfo> umsMountName = umsApi.getUmsMountName();
		if (umsMountName != null) {
			for (UmsMountInfo mi : umsMountName) {
				System.out.println(mi.getMountName());
			}
		}
	}

}
