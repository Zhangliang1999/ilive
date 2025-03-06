/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.bvRadio.iLive.iLive.util;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.bvRadio.iLive.iLive.web.ConfigUtils;
import com.google.common.collect.Lists;
import com.sun.mail.util.UUDecoderStream;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

/**
 * Jedis Cache 工具类
 * 
 * @author jeeplus
 * @version 2014-6-29
 */
public class JedisUtils {

	// 1. 创建连接池JedisPool
	 static JedisPoolConfig  jedisPoolConfig =new JedisPoolConfig();
	 static JedisPool jedisPool = new JedisPool(jedisPoolConfig,ConfigUtils.get("redis_server_add"), 6379);
     
	/**
	 * 获取资源
	 * 
	 * @return
	 * @throws JedisException
	 */
	public static Jedis getResource() throws JedisException {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
//			logger.debug("getResource.", jedis);
		} catch (JedisException e) {
			returnBrokenResource(jedis);
			throw e;
		}
		return jedis;
	}

	/**
	 * 归还资源
	 * 
	 * @param jedis
	 * @param isBroken
	 */
	public static void returnBrokenResource(Jedis jedis) {
		if (jedis != null) {

			jedisPool.returnBrokenResource(jedis);
		}
	}
	
	/**
	 * 清除数据库
	 * 
	 * @param jedis
	 * @param isBroken
	 */
	public static void flushall() {
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.flushAll();
		} catch (Exception e) {

		} finally {
			returnResource(jedis);
		}
	}

	/**
	 * 释放资源
	 * 
	 * @param jedis
	 * @param isBroken
	 */
	public static void returnResource(Jedis jedis) {
		if (jedis != null) {
			jedisPool.returnBrokenResource(jedis);
		}
	}

	/**
	 * 获取缓存
	 * 
	 * @param key 键
	 * @return 值
	 */
	public static String get(String key) {
		String value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)) {
				value = jedis.get(key);
				value = StringUtils.isNotBlank(value) && !"nil".equalsIgnoreCase(value) ? value : null;

			}
		} catch (Exception e) {

		} finally {
			returnResource(jedis);
		}
		return value;
	}

	/**
	 * 获取缓存
	 * 
	 * @param key 键
	 * @return 值
	 */
	public static Object getObject(String key) {
		Object value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(getBytesKey(key))) {
				value = toObject(jedis.get(getBytesKey(key)));

			}
		} catch (Exception e) {

		} finally {
			returnResource(jedis);
		}
		return value;
	}

	/**
	 * 设置缓存
	 * 
	 * @param key          键
	 * @param value        值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static String set(String key, String value, int cacheSeconds) {
		String result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.set(key, value);
			if (cacheSeconds != 0) {
				jedis.expire(key, cacheSeconds);
			}

		} catch (Exception e) {

		} finally {
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 设置缓存
	 * 
	 * @param key          键
	 * @param value        值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static String setByte(byte[] key, byte[] value, int cacheSeconds) {
		String result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if(value!=null) {
				result = jedis.set(key, value);
			}
			if (cacheSeconds != 0) {
				jedis.expire(key, cacheSeconds);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 获取byte[]类型Key
	 * 
	 * @param key
	 * @return
	 */
	public static byte[] getByte(byte[] key) {
		byte[] value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)) {
				value = jedis.get(key);

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return value;
	}

	/**
	 * 设置缓存
	 * 
	 * @param key          键
	 * @param value        值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static String setObject(String key, Object value, int cacheSeconds) {
		String result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if(value!=null) {
				result = jedis.set(getBytesKey(key), toBytes(value));
			}
			if (cacheSeconds != 0) {
				jedis.expire(key, cacheSeconds);
			}

		} catch (Exception e) {

		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 获取set缓存
	 * 
	 * @param key 键
	 * @return 值
	 */
	public static Set<String> getSet(String key) {
		Set<String> value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)) {
				value = jedis.smembers(key);

			}
		} catch (Exception e) {

		} finally {
			returnResource(jedis);
		}
		return value;
	}

	/**
	 * 获取List缓存
	 * 
	 * @param key 键
	 * @return 值
	 */
	public static List<String> getList(String key) {
		List<String> value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)) {
				value = jedis.lrange(key, 0, -1);

			}
		} catch (Exception e) {

		} finally {
			returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * 删除set
	 * 
	 * @param key 键
	 * @return 值
	 */
	public static long removeSet(String key,String value) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)) {
				result=	jedis.srem(key, value);
			}
		} catch (Exception e) {

		} finally {
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 获取List缓存
	 * 
	 * @param key 键
	 * @return 值
	 */
	public static List<Object> getObjectList(String key) {
		List<Object> value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(getBytesKey(key))) {
				List<byte[]> list = jedis.lrange(getBytesKey(key), 0, -1);
				value = Lists.newArrayList();
				for (byte[] bs : list) {
					value.add(toObject(bs));
				}

			}
		} catch (Exception e) {

		} finally {
			returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * 向set缓存中添加值
	 * 
	 * @param key   键
	 * @param value 值
	 * @return
	 */
	public static long setAdd(String key, String... value) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if(value!=null) {
				result = jedis.sadd(key, value);
			}
		} catch (Exception e) {

		} finally {
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 设置List缓存
	 * 
	 * @param key          键
	 * @param value        值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static long setList(String key, List<String> value, int cacheSeconds) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)) {
				jedis.del(key);
			}
			for (String string : value) {
				result = jedis.rpush(key, string);
			}

			if (cacheSeconds != 0) {
				jedis.expire(key, cacheSeconds);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	/**
	 * 设置set缓存
	 * 
	 * @param key          键
	 * @param value        值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static long setSet(String key, Set<String> value, int cacheSeconds) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)) {
				jedis.del(key);
			}
			for (String string : value) {
				result = jedis.sadd(key, string);
			}

			if (cacheSeconds != 0) {
				jedis.expire(key, cacheSeconds);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 设置List缓存
	 * 
	 * @param key          键
	 * @param value        值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static long setObjectList(String key, List<Object> value, int cacheSeconds) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(getBytesKey(key))) {
				jedis.del(key);
			}
			List<byte[]> list = Lists.newArrayList();
			for (Object o : value) {
				list.add(toBytes(o));
			}
			result = jedis.rpush(getBytesKey(key), (byte[][]) list.toArray());
			if (cacheSeconds != 0) {
				jedis.expire(key, cacheSeconds);
			}

		} catch (Exception e) {

		} finally {
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 向List缓存中添加值
	 * 
	 * @param key   键
	 * @param value 值
	 * @return
	 */
	public static long listAdd(String key, String... value) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if(value!=null) {
				result = jedis.rpush(key, value);
			}
		} catch (Exception e) {

		} finally {
			returnResource(jedis);
		}
		return result;
	}
	/**
	 * 删除List缓存中添加值
	 * 
	 * @param key   键
	 * @param value 值
	 * @return
	 */
	public static long delList(String key, String value,int num) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.lrem(key, num, value);

		} catch (Exception e) {

		} finally {
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 向List缓存中添加值
	 * 
	 * @param key   键
	 * @param value 值
	 * @return
	 */
	public static long listObjectAdd(String key, Object... value) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			List<byte[]> list = Lists.newArrayList();
			for (Object o : value) {
				list.add(toBytes(o));
			}
			result = jedis.rpush(getBytesKey(key), (byte[][]) list.toArray());

		} catch (Exception e) {

		} finally {
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 删除缓存
	 * 
	 * @param key 键
	 * @return
	 */
	public static long del(String key) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)) {
				result = jedis.del(key);

			} else {

			}
		} catch (Exception e) {

		} finally {
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 删除缓存
	 * 
	 * @param key 键
	 * @return
	 */
	public static long delObject(String key) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(getBytesKey(key))) {
				result = jedis.del(getBytesKey(key));

			} else {

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 缓存是否存在
	 * 
	 * @param key 键
	 * @return
	 */
	public static boolean exists(String key) {
		boolean result = false;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.exists(key);

		} catch (Exception e) {

		} finally {
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 缓存是否存在
	 * 
	 * @param key 键
	 * @return
	 */
	public static boolean existsObject(String key) {
		boolean result = false;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.exists(getBytesKey(key));

		} catch (Exception e) {

		} finally {
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 获取byte[]类型Key
	 * 
	 * @param key
	 * @return
	 */
	public static byte[] getBytesKey(Object object) {
		if (object instanceof String) {
			return StringUtils.getBytes((String) object);
		} else {
			return ObjectUtils.serialize(object);
		}
	}

	/**
	 * Object转换byte[]类型
	 * 
	 * @param key
	 * @return
	 */
	public static byte[] toBytes(Object object) {
		return ObjectUtils.serialize(object);
	}

	/**
	 * byte[]型转换Object
	 * 
	 * @param key
	 * @return
	 */
	public static Object toObject(byte[] bytes) {
		return ObjectUtils.unserialize(bytes);
	}

	public static boolean exists(byte[] bytes) {
		boolean result = false;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.exists(bytes);

		} catch (Exception e) {

		} finally {
			returnResource(jedis);
		}
		return result;
	}

	private static final String LOCK_SUCCESS = "OK";
	private static final String SET_IF_NOT_EXIST = "NX";
	private static final String SET_WITH_EXPIRE_TIME = "EX";
	private static final Long RELEASE_SUCCESS = 1L;
        /**
	     * 尝试获取分布式锁
	     * @param jedis Redis客户端
	     * @param lockKey 锁
	     * @param requestId 请求标识
	     * @param expireTime 超期时间
	     * @return 是否获取成功
	     */
		public static boolean tryGetDistributedLock( String lockKey, String requestId, int expireTime) {
					String result = null;
					Jedis jedis = null;
					try {
						jedis = getResource();
						result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);

					} catch (Exception e) {

					} finally {
						returnResource(jedis);
					}
					if (LOCK_SUCCESS.equals(result)) {
			            return true;
			        }
			        return false;
			
		}
		 /**
	     * 释放分布式锁
	     * @param jedis Redis客户端
	     * @param lockKey 锁
	     * @param requestId 请求标识
	     * @return 是否释放成功
	     */
	    public static boolean releaseDistributedLock(String lockKey, String requestId) {
	    	Jedis jedis = getResource();
	        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
	        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
	        returnResource(jedis);
	        if (RELEASE_SUCCESS.equals(result)) {
	            return true;
	        }
	        return false;
	 
	    }

}
