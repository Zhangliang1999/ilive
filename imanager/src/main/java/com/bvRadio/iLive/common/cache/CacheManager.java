package com.bvRadio.iLive.common.cache;

import java.util.*;
import java.util.Map.Entry;

/**
 * 缓存管理
 * 
 * @author JWZT-YSF
 * 
 */
public class CacheManager {
	public final static String mobile_auth_ = "mobile_auth_";
	public final static String mobile_token_ = "mobile_token_";
	public final static String web_token_ = "web_token_";
	public static String third_token_="third_token_";
	public final static String web_last_access_token_ = "web_last_access_token_";
	public final static String host_collect_num_ = "host_collect_num_";

	private static HashMap<String, Cache> cacheMap = new HashMap<String, Cache>();

	/**
	 * 单例构造方法
	 */
	private CacheManager() {
		super();
	}

	/**
	 * 载入缓存
	 * 
	 * @param key
	 * @param cache
	 */
	public synchronized static void putCache(String key, Cache cache) {
		cacheMap.put(key, cache);
	}

	/**
	 * 得到缓存。同步静态方法
	 * 
	 * @param key
	 * @return
	 */
	private synchronized static Cache getCache(String key) {
		return (Cache) cacheMap.get(key);
	}

	/**
	 * 判断是否存在一个缓存
	 */
	private synchronized static boolean hasCache(String key) {
		return cacheMap.containsKey(key);
	}

	/**
	 * 清除所有缓存
	 */
	public synchronized static void clearAll() {
		cacheMap.clear();
	}

	/**
	 * 清除key以str开头的一类特定缓存
	 * 
	 * @param str
	 */
	public synchronized static void clearAll(String str) {
		Iterator<Entry<String, Cache>> i = cacheMap.entrySet().iterator();
		String key;
		ArrayList<String> arr = new ArrayList<String>();
		try {
			while (i.hasNext()) {
				Entry<String, Cache> entry = i.next();
				key = (String) entry.getKey();
				if (key.startsWith(str)) { // 如果匹配则删除掉
					arr.add(key);
				}
			}
			for (int k = 0; k < arr.size(); k++) {
				clearOnly(arr.get(k));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 清除指定的缓存
	 * 
	 * @param key
	 */
	public synchronized static void clearOnly(String key) {
		cacheMap.remove(key);
	}

	/**
	 * 获取缓存信息
	 * 
	 * @param key
	 * @return
	 */
	public static Cache getCacheInfo(String key) {
		if (hasCache(key)) {
			Cache cache = getCache(key);
			if (cacheExpired(cache)) { // 调用判断是否终止方法
				cache.setExpired(true);
			}
			return cache;
		} else {
			return null;
		}
	}

	/**
	 * 载入缓存信息
	 * 
	 * @param key
	 * @param obj
	 *            缓存值
	 * @param dt
	 *            缓存有效时间，单位：ms
	 * @param expired
	 *            缓存终止状态
	 */
	public static void putCacheInfo(String key, Object obj, long dt, boolean expired) {
		Cache cache = new Cache();
		cache.setKey(key);
		cache.setTimeOut(dt + System.currentTimeMillis()); // 设置多久后更新缓存
		cache.setValue(obj);
		cache.setExpired(expired); // 缓存默认载入时，终止状态为FALSE
		cacheMap.put(key, cache);
	}

	/**
	 * 重写载入缓存信息方法，默认终止状态为FALSE
	 * 
	 * @param key
	 * @param obj
	 *            缓存值
	 * @param dt
	 *            缓存有效时间，单位：ms
	 */
	public static void putCacheInfo(String key, Object obj, long dt) {
		Cache cache = new Cache();
		cache.setKey(key);
		cache.setTimeOut(dt + System.currentTimeMillis());
		cache.setValue(obj);
		cache.setExpired(false);
		cacheMap.put(key, cache);
	}

	/**
	 * 判断缓存是否终止
	 * 
	 * @param cache
	 * @return
	 */

	public static boolean cacheExpired(Cache cache) {
		if (null == cache) { // 传入的缓存不存在
			return false;
		}
		long nowDt = System.currentTimeMillis(); // 系统当前的毫秒数
		long cacheDt = cache.getTimeOut(); // 缓存内的过期毫秒数
		if (cacheDt <= 0 || cacheDt > nowDt) { // 过期时间小于等于零时,或者过期时间大于当前时间时，则为FALSE
			return false;
		} else { // 大于过期时间 即过期
			return true;
		}
	}

	/**
	 * 获取缓存中的大小
	 * 
	 * @return
	 */
	public static int getCacheSize() {
		return cacheMap.size();
	}

	/**
	 * 获取key包含str的一类缓存的大小
	 * 
	 * @param str
	 * @return
	 */
	public static int getCacheSize(String str) {
		int k = 0;
		Iterator<Entry<String, Cache>> i = cacheMap.entrySet().iterator();
		String key;
		try {
			while (i.hasNext()) {
				Entry<String, Cache> entry = i.next();
				key = (String) entry.getKey();
				if (key.indexOf(str) != -1) { // 如果匹配则删除掉
					k++;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return k;
	}

	/**
	 * 获取缓存对象中的所有键值名称
	 * 
	 * @return
	 */
	public static ArrayList<String> getCacheAllkey() {
		ArrayList<String> a = new ArrayList<String>();
		try {
			Iterator<Entry<String, Cache>> i = cacheMap.entrySet().iterator();
			while (i.hasNext()) {
				Entry<String, Cache> entry = i.next();
				a.add((String) entry.getKey());
			}
		} catch (Exception ex) {
		}
		return a;
	}

	/**
	 * 获取缓存对象中key包含str的一类缓存的键值名称
	 * 
	 * @param type
	 * @return
	 */
	public static ArrayList<String> getCacheListkey(String type) {
		ArrayList<String> a = new ArrayList<String>();
		String key;
		try {
			Iterator<Entry<String, Cache>> i = cacheMap.entrySet().iterator();
			while (i.hasNext()) {
				Entry<String, Cache> entry = i.next();
				key = (String) entry.getKey();
				if (key.indexOf(type) != -1) {
					a.add(key);
				}
			}
		} catch (Exception ex) {
		}
		return a;
	}

}