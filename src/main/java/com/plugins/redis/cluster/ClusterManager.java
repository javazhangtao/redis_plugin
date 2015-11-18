package com.plugins.redis.cluster;

import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.plugins.redis.RedisManagerCustom;
import com.plugins.redis.suppor.config.RedisConfig;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

/**
 *	redis 集群工具类
 */
public class ClusterManager extends RedisManagerCustom{
	
	JedisCluster jedisCluster;
	RedisConfig redisConfig;
	
	public ClusterManager(RedisConfig redisConfig){
		this.redisConfig=redisConfig;
	}
	
	@Override
	public void addObjectToRedis(final String key, final Object obj) throws Exception {
		addObjectToRedis(key, obj,null);
	}

	@Override
	public void addObjectToRedis(final String key, final Object obj, final Integer seconds) throws Exception {
		_checkJedisCluster();
		if(null!=obj && !Strings.isNullOrEmpty(key)){
			new Thread(){
				public void run() {
					try {
						if(null==seconds)
							jedisCluster.set(serializer(key), serializer(obj));
						else
							jedisCluster.setex(serializer(key), seconds, serializer(obj));
					} catch(Exception e){}
				};
			}.start();
		}else{
			throw new NullPointerException();
		}
	}

	@Override
	public <T> T getObjectFromRedis(final String key, final Class<T> clazz) throws Exception {
		_checkJedisCluster();
		if(!Strings.isNullOrEmpty(key)){
			byte[] bytes=jedisCluster.get(serializer(key));
			return (null==bytes)?null:deserialize(bytes, clazz);
		}else{
			throw new NullPointerException();
		}
	}

	@Override
	public boolean removeObjectFromRedis(final String key) throws Exception {
		_checkJedisCluster();
		if(!Strings.isNullOrEmpty(key)){
			return (jedisCluster.del(serializer(key))>0)?true:false;
		}else{
			throw new NullPointerException();
		}
		
	}

	@Override
	public void expire(final String key, final Integer seconds) throws Exception {
		_checkJedisCluster();
		if(!Strings.isNullOrEmpty(key)){
			jedisCluster.expire(serializer(key),seconds);
		}else{
			throw new NullPointerException();
		}
	}

	@Override
	public boolean exists(final String key) throws Exception {
		_checkJedisCluster();
		if(!Strings.isNullOrEmpty(key)){
			return jedisCluster.exists(serializer(key));
		}else{
			throw new NullPointerException();
		}
	}
	
	void _checkJedisCluster() throws Exception {
		if(null==this.jedisCluster){
			if(null==this.redisConfig)
				throw new NullPointerException("redisConfig is null ");
			if(this.redisConfig.getHostAndPorts().isEmpty())
				throw new NullPointerException("redisConfig hosts not found ");
			Set<HostAndPort> hostAndPorts=Sets.newHashSet();
			for (Entry<String, String> e:this.redisConfig.getHostAndPorts().entrySet()) {
				if(!Strings.isNullOrEmpty(e.getKey()) && !Strings.isNullOrEmpty(e.getValue())){
					hostAndPorts.add(new HostAndPort(e.getKey(), Integer.valueOf(e.getValue())));
				}
			}
			if(hostAndPorts.isEmpty())
				throw new Exception("no find valid host and port");
			this.jedisCluster=new JedisCluster(hostAndPorts);
			
		}
			
	}
}
