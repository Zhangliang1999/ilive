package com.bvRadio.iLive.iLive.util;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class JedisPoolConfig extends GenericObjectPoolConfig{
	public JedisPoolConfig() {
	    setTestWhileIdle(true);
	    //
	    setMinEvictableIdleTimeMillis(60000);
	    //
	    setTimeBetweenEvictionRunsMillis(30000);
	    setNumTestsPerEvictionRun(-1);
	    setMaxTotal(5000);
	    setMaxIdle(500);
	    setMinIdle(500);
	    setMaxWaitMillis(500);
	    }
}
