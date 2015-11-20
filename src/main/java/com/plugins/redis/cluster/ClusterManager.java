package com.plugins.redis.cluster;

import java.util.Map;

import com.google.common.base.Strings;
import com.plugins.redis.RedisManager;
import com.plugins.redis.suppor.config.RedisConfig;
import com.plugins.serializer.GenericSerializer;
import com.plugins.serializer.ProtocbuffSerializer;

import redis.clients.jedis.JedisCluster;

/**
 *	redis 集群工具类
 */
public class ClusterManager implements RedisManager{
	RedisConfig redisConfig;
	GenericSerializer serializer;
	JedisCluster jedisCluster;
	
	public ClusterManager(final RedisConfig redisConfig)  throws Exception{
		JedisClusterFactory._initJedisCluster(redisConfig);
	}
	@Override
	public void addObjectToRedis(final String key, final Object obj) throws Exception {
		addObjectToRedis(key, obj,null);
	}

	@Override
	public void addObjectToRedis(final String key, final Object obj, final Integer seconds) throws Exception {
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
	public void addObjectToRedisBatch(final Map<String, Object> objs) throws Exception {
		
	}

	@Override
	public void addObjectToRedisBatch(final Map<String, Object> objs, final Integer seconds) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> T getObjectFromRedis(final String key, final Class<T> clazz) throws Exception {
		if(!Strings.isNullOrEmpty(key)){
			byte[] bytes=jedisCluster.get(serializer(key));
			return (null==bytes)?null:deserialize(bytes, clazz);
		}else{
			throw new NullPointerException();
		}
	}

	@Override
	public boolean removeObjectFromRedis(final String key) throws Exception {
		if(!Strings.isNullOrEmpty(key)){
			return (jedisCluster.del(serializer(key))>0)?true:false;
		}else{
			throw new NullPointerException();
		}
	}

	@Override
	public void expire(final String key, final Integer seconds) throws Exception {
		if(!Strings.isNullOrEmpty(key)){
			jedisCluster.expire(serializer(key),seconds);
		}else{
			throw new NullPointerException();
		}
	}

	@Override
	public boolean exists(final String key) throws Exception {
		if(!Strings.isNullOrEmpty(key)){
			return jedisCluster.exists(serializer(key));
		}else{
			throw new NullPointerException();
		}
	}

	@Override
	public <T> byte[] serializer(final T object) throws Exception {
		if(null==this.serializer)
			serializer=new ProtocbuffSerializer();
		return serializer.serialize(object);
	}

	@Override
	public <T> T deserialize(final byte[] bytes, final Class<T> cls) throws Exception {
		if(null==this.serializer)
			serializer=new ProtocbuffSerializer();
		return serializer.deserialize(bytes, cls);
	}
}
