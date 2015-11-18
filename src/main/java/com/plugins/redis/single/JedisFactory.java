package com.plugins.redis.single;

import java.util.Map.Entry;

import com.google.common.base.Strings;
import com.plugins.redis.suppor.config.RedisConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 *	redis 单点  jedis生产工厂
 */
public class JedisFactory {

	JedisPool jedisPool;
	RedisConfig redisConfig;
	
	public JedisFactory(RedisConfig redisConfig) throws Exception{
		this.redisConfig=redisConfig;
		_initJedisPool();
	}
	
	public Jedis getJedis() throws Exception{
		if(null==jedisPool)
			throw new Exception(" JedisPool is null ");
		return jedisPool.getResource();
	}
	
	
	void _initJedisPool() throws Exception{
		if(null==this.redisConfig)
			throw new NullPointerException(" redis properties info is null ");
		if(this.redisConfig.getHostAndPorts().isEmpty())
			throw new NullPointerException(" redis host is null ");
		JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
		jedisPoolConfig.setMaxTotal(this.redisConfig.getMaxActive());
		jedisPoolConfig.setMaxIdle(this.redisConfig.getMaxIdle());
		jedisPoolConfig.setMaxWaitMillis(this.redisConfig.getMaxWait());
		jedisPoolConfig.setTestOnBorrow(this.redisConfig.isTestOnBorrow());
		for (Entry<String, String> e:this.redisConfig.getHostAndPorts().entrySet()) {
			if(!Strings.isNullOrEmpty(e.getKey()) && !Strings.isNullOrEmpty(e.getValue())){
				try {
					jedisPool=new JedisPool(jedisPoolConfig, e.getKey(),Integer.valueOf(e.getValue()));
				} catch (Exception e2) {
					continue ;
				}
			}
		}
	}
	
	
}
