package com.bvRadio.iLive.iLive.web.pc;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.util.Assert;

/**
 * 
 * @author administrator 用户session记录类
 */
public class SocketSessionRegistry {

	// this map save every session
	// 这个集合存储session
	private final ConcurrentMap<String, Set<String>> userSessionIds = new ConcurrentHashMap<String, Set<String>>();

	private final Object lock = new Object();

	public SocketSessionRegistry() {
	}

	/**
	 *
	 * 获取sessionId
	 * 
	 * @param user
	 * @return
	 */
	public Set<String> getSessionIds(String user) {
		Set<String> set = (Set<String>) this.userSessionIds.get(user);
		if(set==null) {
			set = new HashSet<>(0);
		}
		return set;
	}

	/**
	 * 获取所有session
	 * 
	 * @return
	 */
	public ConcurrentMap<String, Set<String>> getAllSessionIds() {
		return this.userSessionIds;
	}

	/**
	 * 挂载session到本地application Cache register session
	 * 
	 * @param user
	 * @param sessionId
	 */
	public void registerSessionId(String user, String sessionId) {
		Assert.notNull(user, "用户不能为空");
		Assert.notNull(sessionId, "sessionId 不能为空");
		Object var3 = this.lock;
		synchronized (this.lock) {
			Object set = (Set) this.userSessionIds.get(user);
			if (set == null) {
				set = new CopyOnWriteArraySet();
				this.userSessionIds.put(user, (Set<String>) set);
			}
			((Set) set).add(sessionId);
		}
	}

	/**
	 * 卸载session对应的用户
	 * 
	 * @param userName
	 * @param sessionId
	 */
	public void unregisterSessionId(String userName, String sessionId) {
		Assert.notNull(userName, "用户不能输入为空");
		Assert.notNull(sessionId, "Session ID 不能为空");
		Object var3 = this.lock;
		synchronized (this.lock) {
			Set set = (Set) this.userSessionIds.get(userName);
			if (set != null && set.remove(sessionId) && set.isEmpty()) {
				this.userSessionIds.remove(userName);
			}
		}
	}

}
