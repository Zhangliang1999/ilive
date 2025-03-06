package com.bvRadio.iLive.iLive.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.socket.WebSocketSession;

import com.bvRadio.iLive.iLive.action.front.vo.AppMediaFile;
import com.bvRadio.iLive.iLive.action.front.vo.ILiveAppEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveLiveRoom;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.entity.UserBean;
import com.bvRadio.iLive.iLive.util.SignUtils;
import com.bvRadio.iLive.iLive.util.StringPatternUtil;
import com.bvRadio.iLive.iLive.web.vo.NotifyStaticsLiveRoom;
import com.bvRadio.iLive.iLive.web.vo.NotifyStaticsLoginVo;
import com.bvRadio.iLive.iLive.web.vo.UserBaseInfo;
import com.jwzt.comm.StringUtils;

/**
 * UV统计
 * 
 * @author administrator
 */
public enum ILiveUserViewStatics {

	INSTANCE;

	Logger logger = LoggerFactory.getLogger(ILiveUserViewStatics.class);

	/**
	 * 统计总人数
	 */
	public int staticTotalUser(Integer liveRoomId) {
		List<String> list = userTotalMap.get(liveRoomId);
		if (list == null || list.isEmpty()) {
			return 0;
		} else {
			int size = list.size();
			return size;
		}
	}

	/**
	 * 用户总人数
	 */
	public ConcurrentHashMap<Integer, List<String>> userTotalMap = new ConcurrentHashMap<>();

	/**
	 * 运行线程池
	 */

	/**
	 * 观看总人数
	 * 
	 * @param userSessionId
	 * @param liveRoomId
	 */
	public void loginEvent(final NotifyStaticsLoginVo loginVo) {
		String userSessionId = loginVo.getUserId();
		Integer liveRoomId = loginVo.getLiveId();
		// System.out.println("loginEvent:" + loginVo.toString());
		try {
			List<String> list = userTotalMap.get(liveRoomId);
			if (list == null) {
				list = new ArrayList<>();
			} else {
				if (userSessionId.indexOf("_") > -1) {
					String[] splitArr = userSessionId.split("_");
					if (splitArr.length > 1) {
						String userId = splitArr[0];
						String sessioId = splitArr[1];
						// 以sessionId作为key
						synchronized ("1") {
							if ("0".equals(userId)) {
								boolean contains = list.contains(sessioId);
								if (!contains) {
									list.add(sessioId);
									userTotalMap.put(liveRoomId, list);
								}
							} else {
								boolean contains = list.contains(userId);
								if (!contains) {
									list.add(userId);
									userTotalMap.put(liveRoomId, list);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String OpenStatisticSwitch = ConfigUtils.get("OpenStatisticSwitch");
		if (OpenStatisticSwitch != null && OpenStatisticSwitch.equals("1")) {
			try {
				WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
				if (null == context) {
					return;
				}
				TaskExecutor executor = (TaskExecutor) context.getBean("threadPoolTaskExecutor");
				executor.execute(new Runnable() {
					public void run() {
						Integer webType = loginVo.getWebType();
						// 1 h5 2 pc
						int transWebType = 0;
						if (webType == 1) {
							transWebType = 4;
						} else if (webType == 2) {
							transWebType = 3;
						} else if (webType == 5) {
							transWebType = 1;
						} else if (webType == 6) {
							transWebType = 2;
						}
						String url = ConfigUtils.get("StaticsNofityUrl");
						url = url + "/service/user/enter";
						UserBaseInfo ubi = new UserBaseInfo();
						ubi.setIp(loginVo.getIpAddr());
						ubi.setLiveEventId(loginVo.getLiveEventId());
						ubi.setRoomId(loginVo.getLiveId());
						ubi.setType(transWebType);
						ubi.setUserId(loginVo.getUserId());
						JSONArray jArr = new JSONArray();
						jArr.put(ubi.buildJson());
						PostMan.postJson(url, "POST", "UTF-8", jArr.toString());
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 登出socket
	 * 
	 * @param loginVo
	 */
	public void logoutEvent(final NotifyStaticsLoginVo loginVo) {
		String OpenStatisticSwitch = ConfigUtils.get("OpenStatisticSwitch");
		if (OpenStatisticSwitch != null && OpenStatisticSwitch.equals("1")) {
			WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
			if (null == context) {
				return;
			}
			TaskExecutor executor = (TaskExecutor) context.getBean("threadPoolTaskExecutor");
			executor.execute(new Runnable() {
				public void run() {
					String url = ConfigUtils.get("StaticsNofityUrl");
					Integer webType = loginVo.getWebType();
					int transWebType = 0;
					if (webType == 1) {
						transWebType = 4;
					} else if (webType == 2) {
						transWebType = 3;
					} else if (webType == 5) {
						transWebType = 1;
					} else if (webType == 6) {
						transWebType = 2;
					}
					// 通知统计服务器
					url = url + "/service/user/leave";
					UserBaseInfo ubi = new UserBaseInfo();
					ubi.setIp(loginVo.getIpAddr());
					ubi.setLiveEventId(loginVo.getLiveEventId());
					ubi.setRoomId(loginVo.getLiveId());
					ubi.setType(transWebType);
					ubi.setUserId(loginVo.getUserId());
					JSONArray jArr = new JSONArray();
					jArr.put(ubi.buildJson());
					PostMan.postJson(url, "POST", "UTF-8", jArr.toString());
				}
			});
		}

	}

	/**
	 * 开始直播
	 */
	public void startLive(final ILiveLiveRoom room, final String ipAddr, String terminalType) {
		String OpenStatisticSwitch = ConfigUtils.get("OpenStatisticSwitch");
		if (OpenStatisticSwitch != null && OpenStatisticSwitch.equals("1")) {
			try {
				WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
				if (null == context) {
					return;
				}
				TaskExecutor executor = (TaskExecutor) context.getBean("threadPoolTaskExecutor");
				final NotifyStaticsLiveRoom notifyRoom = this.convertRoom2NotifyStaticsLiveRoom(room);
				executor.execute(new Runnable() {
					@Override
					public void run() {
						String url = ConfigUtils.get("StaticsNofityUrl");
						long nowTime = System.currentTimeMillis();
						String sign = SignUtils.getMD5(nowTime + "__jwzt_chinaNet");
						url = url + "/service/live/begin?roomId=" + room.getRoomId() + "&time=" + nowTime + "&ip="
								+ ipAddr + "&sign=" + sign + "&liveEventId=" + room.getLiveEvent().getLiveEventId();
						logger.info("startLive {}", url);
						JSONObject jsonObject = new JSONObject(notifyRoom);
						PostMan.postJson(url, "POST", "UTF-8", jsonObject.toString());
						// System.out.println("return postJson-->" + postJson);
						url = ConfigUtils.get("StaticsNofityUrl");
						// 先循环告诉统计系统用户登出
						String postUrl = url + "/service/user/enter";
						ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = ApplicationCache
								.getUserListMap();
						ConcurrentHashMap<String, UserBean> userMap = userListMap.get(room.getRoomId());
						logger.info("开始统计人数:" + userMap.size());
						if (userMap != null && !userMap.isEmpty()) {
							int size = userMap.size();
							if (size > 5) {
								JSONArray jsonArray = new JSONArray();
								int left = size % 5;
								if (left == 0) {
									int count = 0;
									for (Map.Entry<String, UserBean> entry : userMap.entrySet()) {
										String userSessionId = entry.getKey();
										UserBean user = entry.getValue();
										Integer webType = 0;
										String clientIP = "";
										Long liveEventId = room.getLiveEvent().getLiveEventId();
										if (user.getWebSocketSession() != null) {
											WebSocketSession webSocketSession = user.getWebSocketSession();
											clientIP = (String) webSocketSession.getAttributes().get("realIpAddr");
											webType = (Integer) webSocketSession.getAttributes().get("webType");
											if (webType == null) {
												webType = 1;
											}

										} else if (user.getSession() != null) {
											IoSession session = user.getSession();
											clientIP = (String) session.getAttribute("KEY_SESSION_CLIENT_IP");
											webType = (Integer) session.getAttribute("webType");
											if (webType == null) {
												webType = 1;
											}
										}
										int transWebType = 0;
										if (webType == 1) {
											transWebType = 4;
										} else if (webType == 2) {
											transWebType = 3;
										} else if (webType == 5) {
											transWebType = 1;
										} else if (webType == 6) {
											transWebType = 2;
										}
										UserBaseInfo ubi = new UserBaseInfo();
										ubi.setIp(clientIP);
										ubi.setLiveEventId(liveEventId);
										ubi.setRoomId(room.getRoomId());
										ubi.setType(transWebType);
										ubi.setUserId(userSessionId);
										jsonArray.put(ubi.buildJson());
										if (count != 0 && count % 5 == 0) {
											PostMan.postJson(postUrl, "POST", "UTF-8", jsonArray.toString());
											jsonArray = null;
											jsonArray = new JSONArray();
										}
										count++;
									}
								} else {
									int count = 0;
									JSONArray leftArray = new JSONArray();
									for (Map.Entry<String, UserBean> entry : userMap.entrySet()) {
										String userSessionId = entry.getKey();
										UserBean user = entry.getValue();
										Integer webType = 1;
										String clientIP = "";
										Long liveEventId = room.getLiveEvent().getLiveEventId();
										if (user.getWebSocketSession() != null) {
											WebSocketSession webSocketSession = user.getWebSocketSession();
											clientIP = (String) webSocketSession.getAttributes().get("realIpAddr");
											webType = (Integer) webSocketSession.getAttributes().get("webType");
											if (webType == null) {
												webType = 1;
											}

										} else if (user.getSession() != null) {
											IoSession session = user.getSession();
											clientIP = (String) session.getAttribute("KEY_SESSION_CLIENT_IP");
											webType = (Integer) session.getAttribute("webType");
											if (webType == null) {
												webType = 1;
											}
										}
										UserBaseInfo ubi = new UserBaseInfo();
										ubi.setIp(clientIP);
										ubi.setLiveEventId(liveEventId);
										ubi.setRoomId(room.getRoomId());
										int transWebType = 0;
										if (webType == 1) {
											transWebType = 3;
										} else if (webType == 2) {
											transWebType = 4;
										} else if (webType == 5) {
											transWebType = 1;
										} else if (webType == 6) {
											transWebType = 2;
										}
										ubi.setType(transWebType);
										ubi.setUserId(userSessionId);
										if (count <= (size - left)) {
											jsonArray.put(ubi.buildJson());
										}
										if (count != 0 && count % 5 == 0) {
											PostMan.postJson(postUrl, "POST", "UTF-8", jsonArray.toString());
											jsonArray = null;
											jsonArray = new JSONArray();
										} else if (count > (size - left)) {
											leftArray.put(ubi);
										}
										count++;
									}
									if (leftArray.length() > 0) {
										PostMan.postJson(postUrl, "POST", "UTF-8", leftArray.toString());
									}
								}
							} else {
								JSONArray jsonArray = new JSONArray();
								for (Map.Entry<String, UserBean> entry : userMap.entrySet()) {
									String userSessionId = entry.getKey();
									UserBean user = entry.getValue();
									Integer webType = 1;
									String clientIP = "";
									Long liveEventId = room.getLiveEvent().getLiveEventId();
									if (user.getWebSocketSession() != null) {
										WebSocketSession webSocketSession = user.getWebSocketSession();
										clientIP = (String) webSocketSession.getAttributes().get("realIpAddr");
										webType = (Integer) webSocketSession.getAttributes().get("webType");
										if (webType == null) {
											webType = 0;
										}

									} else if (user.getSession() != null) {
										IoSession session = user.getSession();
										clientIP = (String) session.getAttribute("KEY_SESSION_CLIENT_IP");
										webType = (Integer) session.getAttribute("webType");
										if (webType == null) {
											webType = 1;
										}
									}
									int transWebType = 0;
									if (webType == 1) {
										transWebType = 3;
									} else if (webType == 2) {
										transWebType = 4;
									} else if (webType == 5) {
										transWebType = 1;
									} else if (webType == 6) {
										transWebType = 2;
									}
									UserBaseInfo ubi = new UserBaseInfo();
									ubi.setIp(clientIP);
									ubi.setLiveEventId(liveEventId);
									ubi.setRoomId(room.getRoomId());
									ubi.setType(transWebType);
									ubi.setUserId(userSessionId);
									jsonArray.put(ubi.buildJson());
								}
								logger.info("login 开始提交:" + jsonArray.toString());
								PostMan.postJson(postUrl, "POST", "UTF-8", jsonArray.toString());
							}
							logger.info("开始通知统计系统直播开始直播的人已经存在哪些人了");
						}
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 结束直播
	 */
	public void stopLive(final ILiveLiveRoom room, final String ipAddr) {
		String OpenStatisticSwitch = ConfigUtils.get("OpenStatisticSwitch");
		if (OpenStatisticSwitch != null && OpenStatisticSwitch.equals("1")) {
			try {
				WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
				if (null == context) {
					return;
				}
				TaskExecutor executor = (TaskExecutor) context.getBean("threadPoolTaskExecutor");
				final NotifyStaticsLiveRoom notifyRoom = this.convertRoom2NotifyStaticsLiveRoom(room);
				executor.execute(new Runnable() {
					@Override
					public void run() {
						try {
							String url = ConfigUtils.get("StaticsNofityUrl");
							long nowTime = System.currentTimeMillis();
							String sign = SignUtils.getMD5(nowTime + "__jwzt_chinaNet");
							url = url + "/service/live/end?time=" + nowTime + "&ip=" + ipAddr + "&sign=" + sign;
							logger.info("stopLive {}", url);
							JSONObject jsonObject = new JSONObject(notifyRoom);
							String postJson = PostMan.postJson(url, "POST", "UTF-8", jsonObject.toString());
							logger.info("return postJson {}", postJson);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * 直播点赞通知
	 * 
	 * @param userId
	 * @param roomId
	 * @param type
	 */
	public void likeRoom(final String userId, final Integer roomId, final Long liveEventId, final Long fileId,
			final String ipAddr, final String terminalType) {
		String OpenStatisticSwitch = ConfigUtils.get("OpenStatisticSwitch");
		if (OpenStatisticSwitch != null && OpenStatisticSwitch.equals("1")) {
			WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
			TaskExecutor executor = (TaskExecutor) context.getBean("threadPoolTaskExecutor");
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						String url = ConfigUtils.get("StaticsNofityUrl");
						url = url + "/service/user/like";
						UserBaseInfo ubi = new UserBaseInfo();
						ubi.setUserId(userId);
						ubi.setRoomId(roomId);
						ubi.setLiveEventId(liveEventId);
						ubi.setFileId(fileId);
						ubi.setIp(ipAddr);
						int type = judgeTerminalType(terminalType);
						ubi.setType(type);
						JSONArray jArr = new JSONArray();
						jArr.put(ubi.buildJson());
						PostMan.postJson(url, "POST", "UTF-8", jArr.toString());
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			});
		}
	}

	/**
	 * 直播评论通知
	 * 
	 * @param room
	 * @return
	 */
	public void commentRoom(final String userId, final Integer roomId, final Long liveEventId, final Long fileId,
			final String ipAddr, final String terminalType) {
		String OpenStatisticSwitch = ConfigUtils.get("OpenStatisticSwitch");
		if (OpenStatisticSwitch != null && OpenStatisticSwitch.equals("1")) {
			// 加入缓存，达到一定数量进行通知
			WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
			TaskExecutor executor = (TaskExecutor) context.getBean("threadPoolTaskExecutor");
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						String url = ConfigUtils.get("StaticsNofityUrl");
						url = url + "/service/user/comment";
						UserBaseInfo ubi = new UserBaseInfo();
						ubi.setUserId(userId);
						ubi.setRoomId(roomId);
						ubi.setLiveEventId(liveEventId);
						ubi.setFileId(fileId);
						ubi.setIp(ipAddr);
						int type = judgeTerminalType(terminalType);
						ubi.setType(type);
						JSONArray jArr = new JSONArray();
						jArr.put(ubi.buildJson());
						PostMan.postJson(url, "POST", "UTF-8", jArr.toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	/**
	 * 直播签到通知
	 * 
	 * @param room
	 * @return
	 */
	public void sign(final String userId, final long liveEventId, final String ipAddr, final String terminalType) {
		String OpenStatisticSwitch = ConfigUtils.get("OpenStatisticSwitch");
		if (OpenStatisticSwitch != null && OpenStatisticSwitch.equals("1")) {
			// 加入缓存，达到一定数量进行通知
			WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
			TaskExecutor executor = (TaskExecutor) context.getBean("threadPoolTaskExecutor");
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						String url = ConfigUtils.get("StaticsNofityUrl");
						url = url + "/service/user/sign";
						UserBaseInfo ubi = new UserBaseInfo();
						ubi.setUserId(userId);
						ubi.setLiveEventId(liveEventId);
						ubi.setIp(ipAddr);
						int type = judgeTerminalType(terminalType);
						ubi.setType(type);
						JSONArray jArr = new JSONArray();
						jArr.put(ubi.buildJson());
						PostMan.postJson(url, "POST", "UTF-8", jArr.toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	/**
	 * 关注企业通知
	 * 
	 * @param room
	 * @return
	 */
	public void concernEnterprise(final String userId, final long enterpriseId, final String ipAddr,
			final String terminalType) {
		String OpenStatisticSwitch = ConfigUtils.get("OpenStatisticSwitch");
		if (OpenStatisticSwitch != null && OpenStatisticSwitch.equals("1")) {
			// 加入缓存，达到一定数量进行通知
			WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
			TaskExecutor executor = (TaskExecutor) context.getBean("threadPoolTaskExecutor");
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						String url = ConfigUtils.get("StaticsNofityUrl");
						url = url + "/service/user/concern";
						UserBaseInfo ubi = new UserBaseInfo();
						ubi.setUserId(userId);
						ubi.setEnterpriseId(enterpriseId);
						ubi.setIp(ipAddr);
						int type = judgeTerminalType(terminalType);
						ubi.setType(type);
						JSONArray jArr = new JSONArray();
						jArr.put(ubi.buildJson());
						PostMan.postJson(url, "POST", "UTF-8", jArr.toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	/**
	 * 取消企业通知
	 * 
	 * @param room
	 * @return
	 */
	public void unconcernEnterprise(final String userId, final long enterpriseId, final String ipAddr,
			final String terminalType) {
		String OpenStatisticSwitch = ConfigUtils.get("OpenStatisticSwitch");
		if (OpenStatisticSwitch != null && OpenStatisticSwitch.equals("1")) {
			// 加入缓存，达到一定数量进行通知
			WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
			TaskExecutor executor = (TaskExecutor) context.getBean("threadPoolTaskExecutor");
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						String url = ConfigUtils.get("StaticsNofityUrl");
						url = url + "/service/user/unconcern";
						UserBaseInfo ubi = new UserBaseInfo();
						ubi.setUserId(userId);
						ubi.setEnterpriseId(enterpriseId);
						ubi.setIp(ipAddr);
						int type = judgeTerminalType(terminalType);
						ubi.setType(type);
						JSONArray jArr = new JSONArray();
						jArr.put(ubi.buildJson());
						PostMan.postJson(url, "POST", "UTF-8", jArr.toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	/**
	 * 回看文件预览通知
	 * 
	 * @param room
	 * @return
	 */
	public void fileViewNotify(final String userId, final Integer roomId, final Long liveEventId, final Long fileId,
			final String ipAddr, final String terminalType) {
		String OpenStatisticSwitch = ConfigUtils.get("OpenStatisticSwitch");
		if (OpenStatisticSwitch != null && OpenStatisticSwitch.equals("1")) {
			WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
			TaskExecutor executor = (TaskExecutor) context.getBean("threadPoolTaskExecutor");
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						int type = judgeTerminalType(terminalType);
						String url = ConfigUtils.get("StaticsNofityUrl");
						url = url + "/service/user/view";
						UserBaseInfo ubi = new UserBaseInfo();
						ubi.setUserId(userId);
						ubi.setRoomId(roomId);
						ubi.setLiveEventId(liveEventId);
						ubi.setFileId(fileId);
						ubi.setIp(ipAddr);
						ubi.setType(type);
						JSONArray jArr = new JSONArray();
						jArr.put(ubi.buildJson());
						PostMan.postJson(url, "POST", "UTF-8", jArr.toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	/**
	 * 送礼物通知
	 * 
	 * @return
	 */
	public void sendGift(final String userId, final Integer roomId, final Long liveEventId, final Long fileId,
			final String ipAddr, final String terminalType, final Double money, final Integer number) {
		String OpenStatisticSwitch = ConfigUtils.get("OpenStatisticSwitch");
		if (OpenStatisticSwitch != null && OpenStatisticSwitch.equals("1")) {
			WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
			TaskExecutor executor = (TaskExecutor) context.getBean("threadPoolTaskExecutor");
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						int type = judgeTerminalType(terminalType);
						String url = ConfigUtils.get("StaticsNofityUrl");
						url = url + "/service/user/sendGift";
						UserBaseInfo ubi = new UserBaseInfo();
						ubi.setUserId(userId);
						ubi.setRoomId(roomId);
						ubi.setLiveEventId(liveEventId);
						ubi.setIp(ipAddr);
						ubi.setType(type);
						ubi.setFileId(fileId);
						JSONArray jArr = new JSONArray();
						JSONObject buildJson = ubi.buildJson();
						buildJson.put("money", money);
						buildJson.put("number", number);
						jArr.put(buildJson);
						PostMan.postJson(url, "POST", "UTF-8", jArr.toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	/**
	 * 打赏通知
	 * 
	 * @return
	 */
	public void reward(final String userId, final Integer roomId, final Long liveEventId, final Long fileId,
			final String ipAddr, final String terminalType, final Double money) {
		String OpenStatisticSwitch = ConfigUtils.get("OpenStatisticSwitch");
		if (OpenStatisticSwitch != null && OpenStatisticSwitch.equals("1")) {
			WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
			TaskExecutor executor = (TaskExecutor) context.getBean("threadPoolTaskExecutor");
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						int type = judgeTerminalType(terminalType);
						String url = ConfigUtils.get("StaticsNofityUrl");
						url = url + "/service/user/reward";
						UserBaseInfo ubi = new UserBaseInfo();
						ubi.setUserId(userId);
						ubi.setRoomId(roomId);
						ubi.setLiveEventId(liveEventId);
						ubi.setIp(ipAddr);
						ubi.setType(type);
						ubi.setFileId(fileId);
						ubi.setIp(ipAddr);
						JSONArray jArr = new JSONArray();
						JSONObject buildJson = ubi.buildJson();
						buildJson.put("money", money);
						jArr.put(buildJson);
						PostMan.postJson(url, "POST", "UTF-8", jArr.toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	protected int judgeTerminalType(String terminalType) {
		// 0、其他 1、安卓 2、IOS 3、PC 4、WAP
		if (!StringUtils.isEmpty(terminalType)) {
			if ("android".equals(terminalType)) {
				return 1;
			} else if ("ios".equals(terminalType)) {
				return 2;
			} else if ("pc".equals(terminalType)) {
				return 3;
			} else if ("h5".equals(terminalType)) {
				return 4;
			}
		}
		return 0;
	}

	/**
	 * 新增企业通知
	 * 
	 * @param room
	 * @return
	 */
	public void addEnterpriseNotify(final ILiveEnterprise enterprise) {
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		TaskExecutor executor = (TaskExecutor) context.getBean("threadPoolTaskExecutor");
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					String url = ConfigUtils.get("StaticsNofityUrl");
					url = url + "/service/enterprise/save";
					ILiveAppEnterprise appEnterprise = new ILiveAppEnterprise();
					JSONObject resultJson = buildEnterpriseJson(enterprise);
					String downloadUrl = PostMan.postJson(url, "POST", "UTF-8", resultJson.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 构建企业信息
	 * 
	 * @param enterprise
	 * @return
	 */
	private JSONObject buildEnterpriseJson(ILiveEnterprise enterprise) {
		ILiveAppEnterprise appEnterprise = new ILiveAppEnterprise();
		appEnterprise.setEnterpriseDesc(StringPatternUtil.convertEmpty(enterprise.getEnterpriseDesc()));
		appEnterprise.setEnterpriseId(enterprise.getEnterpriseId());
		appEnterprise.setEnterpriseName(StringPatternUtil.convertEmpty(enterprise.getEnterpriseName()));
		appEnterprise.setEnterpriseImg(StringPatternUtil.convertEmpty(enterprise.getEnterpriseImg()));
		appEnterprise.setCertStatus(enterprise.getCertStatus());
		appEnterprise.setHomePageUrl("");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		appEnterprise.setApplyTime(enterprise.getApplyTime() == null ? "" : sdf.format(enterprise.getApplyTime()));
		appEnterprise.setCertTime(enterprise.getCertTime() == null ? "" : sdf.format(enterprise.getCertTime()));
		return new JSONObject(appEnterprise);
	}

	/**
	 * 构建回看文件
	 * 
	 * @param iLiveMediaFile
	 * @return
	 */
	private JSONObject buildMediaJson(ILiveMediaFile mediaFile) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		AppMediaFile appFile = new AppMediaFile();
		appFile.setFileName(mediaFile.getMediaFileName());
		appFile.setCreateTime(sdf.format(mediaFile.getMediaCreateTime()));
		appFile.setFileId(mediaFile.getFileId());
		appFile.setFileImg(mediaFile.getFileCover() == null ? "" : mediaFile.getFileCover());
		appFile.setFileSize(String.valueOf(mediaFile.getFileSize() == null ? 0 : mediaFile.getFileSize()));
		appFile.setFileDuration(String.valueOf(mediaFile.getDuration() == null ? 0 : mediaFile.getDuration()));
		appFile.setCommentsNum(
				mediaFile.getCommentsCount() == null ? "0" : String.valueOf(mediaFile.getCommentsCount()));
		appFile.setViewNum(0L);
		appFile.setRoomId(mediaFile.getLiveRoomId());
		appFile.setEnterpriseId(mediaFile.getEnterpriseId().intValue());
		appFile.setUserId(mediaFile.getUserId());
		appFile.setWidthHeight(mediaFile.getWidthHeight() == null ? "" : mediaFile.getWidthHeight());
		Integer liveRoomId = mediaFile.getLiveRoomId();
		if (liveRoomId == null || liveRoomId == 0) {
			liveRoomId = 0;
		}
		appFile.setRoomId(liveRoomId);
		appFile.setLiveEventId(mediaFile.getLiveEventId());
		return new JSONObject(appFile);
	}

	/**
	 * 修改企业通知
	 * 
	 * @param room
	 * @return
	 */
	public void updateEnterpriseNotify(final ILiveEnterprise enterprise) {
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		TaskExecutor executor = (TaskExecutor) context.getBean("threadPoolTaskExecutor");
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					String url = ConfigUtils.get("StaticsNofityUrl");
					url = url + "/service/enterprise/update";
					ILiveAppEnterprise appEnterprise = new ILiveAppEnterprise();
					JSONObject resultJson = buildEnterpriseJson(enterprise);
					String downloadUrl = PostMan.postJson(url, "POST", "UTF-8", resultJson.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 新增文件通知
	 * 
	 * @param room
	 * @return
	 */
	public void addFileNotify(final ILiveMediaFile iLiveMediaFile) {
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		TaskExecutor executor = (TaskExecutor) context.getBean("threadPoolTaskExecutor");
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					String url = ConfigUtils.get("StaticsNofityUrl");
					url = url + "/service/video/save";
					JSONObject jsonObject = buildMediaJson(iLiveMediaFile);
					String downloadUrl = PostMan.postJson(url, "POST", "UTF-8", jsonObject.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 修改文件通知
	 * 
	 * @param room
	 * @return
	 */
	public void updateFileNotify(final ILiveMediaFile iLiveMediaFile) {
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		TaskExecutor executor = (TaskExecutor) context.getBean("threadPoolTaskExecutor");
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					String url = ConfigUtils.get("StaticsNofityUrl");
					url = url + "/service/video/update";
					JSONObject jsonObject = buildMediaJson(iLiveMediaFile);
					String downloadUrl = PostMan.postJson(url, "POST", "UTF-8", jsonObject.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private NotifyStaticsLiveRoom convertRoom2NotifyStaticsLiveRoom(ILiveLiveRoom room) {
		ILiveEvent liveEvent = room.getLiveEvent();
		Long liveEventId = liveEvent.getLiveEventId();
		Integer roomId = room.getRoomId();
		String liveTitle = StringPatternUtil.convertEmpty(liveEvent.getLiveTitle());
		String coverAddr = StringPatternUtil.convertEmpty(liveEvent.getConverAddr());
		int openDecorate = liveEvent.getOpenDataBeautifySwitch() == null ? 0 : liveEvent.getOpenDataBeautifySwitch();
		int baseNum = liveEvent.getBaseNum() == null ? 0 : liveEvent.getBaseNum();
		Double multipleNum = liveEvent.getMultiple() == null ? 0 : liveEvent.getMultiple().doubleValue();
		NotifyStaticsLiveRoom notifyRoom = new NotifyStaticsLiveRoom();
		notifyRoom.setBaseNum((long) baseNum);
		notifyRoom.setCoverAddr(coverAddr);
		notifyRoom.setRoomId(roomId);
		notifyRoom.setLiveEventId(liveEventId);
		notifyRoom.setLiveTitle(liveTitle);
		notifyRoom.setOpenDecorate(openDecorate);
		notifyRoom.setMultipleNum(multipleNum);
		notifyRoom.setLiveBeginTime(System.currentTimeMillis());
		ILiveEnterprise enterprise = room.getLiveEvent().getEnterprise();
		ILiveAppEnterprise appEnterprise = this.convertPo2Vo(enterprise);
		notifyRoom.setEnterprise(appEnterprise);
		return notifyRoom;
	}

	private ILiveAppEnterprise convertPo2Vo(ILiveEnterprise enterprise) {
		ILiveAppEnterprise appEnterprise = new ILiveAppEnterprise();
		appEnterprise.setEnterpriseDesc(StringPatternUtil.convertEmpty(enterprise.getEnterpriseDesc()));
		appEnterprise.setEnterpriseId(enterprise.getEnterpriseId());
		appEnterprise.setEnterpriseName(StringPatternUtil.convertEmpty(enterprise.getEnterpriseName()));
		appEnterprise.setEnterpriseImg(StringPatternUtil.convertEmpty(enterprise.getEnterpriseImg()));
		return appEnterprise;
	}

}
