package com.bvRadio.iLive.iLive.web;

import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;

import com.bvRadio.iLive.iLive.entity.ILiveEstoppel;
import com.bvRadio.iLive.iLive.entity.ILiveEvent;
import com.bvRadio.iLive.iLive.entity.ILiveMessage;
import com.bvRadio.iLive.iLive.entity.UserBean;

public class ApplicationCache {
	/**
	 * session缓存
	 * 
	 * @key sessionId
	 * @value session
	 */
	private static Hashtable<Long, IoSession> sessionMap = new Hashtable<Long, IoSession>();
	/**
	 * 场次缓存
	 */
	private static Hashtable<Integer, ILiveEvent> onlineNumber = new Hashtable<Integer, ILiveEvent>();

	/**
	 * 用户缓存
	 * 
	 * @key:liveId
	 * @value: userId_sessionId , userBean
	 */
	private static ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> userListMap = new ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>>();

	/**
	 * 直播间缓存
	 * 
	 * @key:liveId
	 * @value: autoCheckSecond
	 */
	private static Hashtable<Integer, Integer> roomListMap = new Hashtable<Integer, Integer>();
	/**
	 * 聊天互动 审核通过
	 * 
	 * @key liveId
	 * @value ILiveMessage
	 */
	private static Hashtable<Integer, List<ILiveMessage>> chatInteractiveMap = new Hashtable<Integer, List<ILiveMessage>>();
	/**
	 * 图文直播
	 * 
	 * @key liveId
	 * @value ILiveMessage
	 */
	// private static Hashtable<Integer, List<ILiveMessage>> frameLiveMap = new
	// Hashtable<Integer, List<ILiveMessage>>();
	/**
	 * 问答
	 * 
	 * @key liveId
	 * @value ILiveMessage
	 */
	private static Hashtable<String, Integer> handler = new Hashtable<String, Integer>();

	/**
	 * 问答
	 * 
	 * @key liveId
	 * @value ILiveMessage
	 */
	private static Hashtable<Integer, List<ILiveMessage>> quizLiveMap = new Hashtable<Integer, List<ILiveMessage>>();

	/**
	 * 聊天互动 未审核
	 * 
	 * @key liveId
	 * @value ILiveMessage
	 */
	private static Hashtable<Integer, List<ILiveMessage>> chatInteractiveMapNO = new Hashtable<Integer, List<ILiveMessage>>();
	/**
	 * 禁言缓存
	 * 
	 * @key liveId
	 * @value userId 禁言实体
	 */
	private static Hashtable<Integer, Hashtable<Long, ILiveEstoppel>> userEstopMap = new Hashtable<Integer, Hashtable<Long, ILiveEstoppel>>();

	/**
	 * 自动审核初始化 缓存
	 * 
	 * @key liveId
	 * @value
	 */
	public static Hashtable<Integer, Integer> checkTimeMap = new Hashtable<Integer, Integer>();

	/**
	 * 用户缓存
	 * 
	 * @key:liveId 直播间ID
	 * @value:userId_sessionId 用户ID_SessionId , userBean 用户
	 */
	public static ConcurrentHashMap<Integer, ConcurrentHashMap<String, UserBean>> getUserListMap() {
		return userListMap;
	}

	/**
	 * 聊天互动 审核通过
	 * 
	 * @key liveId
	 * @value ILiveMessage
	 */
	public static Hashtable<Integer, List<ILiveMessage>> getChatInteractiveMap() {
		return chatInteractiveMap;
	}

	/**
	 * 图文直播
	 * 
	 * @key liveId
	 * @value ILiveMessage
	 */
	// public static Hashtable<Integer, List<ILiveMessage>> getFrameLiveMap() {
	// return frameLiveMap;
	// }
	/**
	 * 问答
	 * 
	 * @key liveId
	 * @value ILiveMessage
	 */
	public static Hashtable<Integer, List<ILiveMessage>> getQuizLiveMap() {
		return quizLiveMap;
	}
	
	public static Hashtable<String, Integer> getHandler() {
		return handler;
	}

	/**
	 * 聊天互动 未审核
	 * 
	 * @key liveId
	 * @value ILiveMessage
	 */
	public static Hashtable<Integer, List<ILiveMessage>> getChatInteractiveMapNO() {
		return chatInteractiveMapNO;
	}

	/**
	 * 直播间缓存
	 * 
	 * @key liveId
	 * @value autoCheckSecond
	 */
	public static Hashtable<Integer, Integer> getRoomListMap() {
		return roomListMap;
	}

	/**
	 * 禁言缓存
	 * 
	 * @key liveId 禁言实体
	 * @value 用户ID 禁言体
	 */
	public static Hashtable<Integer, Hashtable<Long, ILiveEstoppel>> getUserEstopMap() {
		return userEstopMap;
	}

	public static Hashtable<Long, IoSession> getSessionMap() {
		return sessionMap;
	}

	/**
	 * 场次
	 * 
	 * @return
	 */
	public static Hashtable<Integer, ILiveEvent> getOnlineNumber() {
		return onlineNumber;
	}

	public static ConcurrentHashMap<Long, Long> LiveEventUserCache = new ConcurrentHashMap<>();

	public void removeRoom(Integer roomId) {
		synchronized (ApplicationCache.LiveEventUserCache) {
			ApplicationCache.getOnlineNumber().remove(roomId);
		}
	}

}
