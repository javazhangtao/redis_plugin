package com.plugins.redis.single;

import com.google.common.base.Strings;
import com.plugins.redis.RedisManagerCustom;
import com.plugins.redis.suppor.config.RedisConfig;

import redis.clients.jedis.Jedis;

/**
 *	单点redis  操作工具类
 */
public class SingleManager extends RedisManagerCustom{
	
	RedisConfig redisConfig;
	JedisFactory jedisFactory;
	
	public SingleManager(RedisConfig redisConfig){
		this.redisConfig=redisConfig;
	}
	
	
	@Override
	public void addObjectToRedis(String key, Object obj) throws Exception {
		addObjectToRedis(key, obj,null);
	}

	@Override
	public void addObjectToRedis(final String key, final Object obj, final Integer seconds) throws Exception {
		if(null!=obj && !Strings.isNullOrEmpty(key)){
			new Thread(){
				public void run() {
					try {
						if(null==seconds)
							_getJedis().set(serializer(key), serializer(obj));
						else
							_getJedis().setex(serializer(key), seconds, serializer(obj));
					} catch(Exception e){}
				};
			}.start();
		}else{
			throw new NullPointerException();
		}
	}

	@Override
	public <T> T getObjectFromRedis(String key, Class<T> clazz) throws Exception {
		if(!Strings.isNullOrEmpty(key)){
			byte[] bytes=_getJedis().get(serializer(key));
			return (null==bytes)?null:deserialize(bytes, clazz);
		}else{
			throw new NullPointerException();
		}
	}

	@Override
	public boolean removeObjectFromRedis(String key) throws Exception {
		if(!Strings.isNullOrEmpty(key)){
			return (_getJedis().del(serializer(key))>0)?true:false;
		}else{
			throw new NullPointerException();
		}
	}

	@Override
	public void expire(String key, Integer seconds) throws Exception {
		if(!Strings.isNullOrEmpty(key)){
			_getJedis().expire(serializer(key),seconds);
		}else{
			throw new NullPointerException();
		}
	}

	@Override
	public boolean exists(String key) throws Exception {
		if(!Strings.isNullOrEmpty(key)){
			return _getJedis().exists(serializer(key));
		}else{
			throw new NullPointerException();
		}
	}

	public RedisConfig getRedisConfig() {
		return redisConfig;
	}

	public void setRedisConfig(RedisConfig redisConfig) {
		this.redisConfig = redisConfig;
	}
	
	Jedis _getJedis() throws Exception{
		if(null==this.jedisFactory)
			this.jedisFactory=new JedisFactory(this.redisConfig);
		return this.jedisFactory.getJedis();
	}


}
