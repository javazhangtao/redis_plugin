package com.plugins.redis.cluster;

import java.util.Set;
import java.util.Map.Entry;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.plugins.redis.suppor.config.RedisConfig;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class JedisClusterFactory {
	static JedisCluster jedisCluster;
	
	public static void _initJedisCluster(RedisConfig redisConfig) throws Exception {
		synchronized (redisConfig) {
			if(null==jedisCluster){
				if(null==redisConfig)
					throw new NullPointerException("redisConfig is null ");
				if(redisConfig.getHostAndPorts().isEmpty())
					throw new NullPointerException("redisConfig hosts not found ");
				Set<HostAndPort> hostAndPorts=Sets.newHashSet();
				for (Entry<String, String> e:redisConfig.getHostAndPorts().entrySet()) {
					if(!Strings.isNullOrEmpty(e.getKey()) && !Strings.isNullOrEmpty(e.getValue())){
						hostAndPorts.add(new HostAndPort(e.getKey(), Integer.valueOf(e.getValue())));
					}
				}
				if(hostAndPorts.isEmpty())
					throw new Exception("no find valid host and port");
				jedisCluster=new JedisCluster(hostAndPorts);
			}
		}
	}
	
	public static JedisCluster _getJedisCluster() throws Exception {
		synchronized (jedisCluster){
			if(null!=jedisCluster)
				return jedisCluster;
			else
				throw new Exception(" jedisCluster is null , please init jedisCluster ");
		}
	}
}
