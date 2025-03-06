package com.bvRadio.iLive.iLive.constants;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ILiveStaticsCache {

	/**
	 * 最大观看人数
	 */
	public static Map<Long, Long> maxViewNumCache = new HashMap<>();

	/**
	 * 观看总人数
	 */
	// public static ConcurrentHashMap<Integer, Long> timeUserNum = new ConcurrentHashMap<>();

	/**
	 * 最大观看数状态
	 */
	public static ConcurrentHashMap<Long, Boolean> maxViewState = new ConcurrentHashMap<>();

}
