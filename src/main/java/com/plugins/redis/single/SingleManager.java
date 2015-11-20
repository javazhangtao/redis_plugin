package com.plugins.redis.single;


import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Strings;
import com.plugins.redis.RedisManager;
import com.plugins.redis.suppor.config.RedisConfig;
import com.plugins.serializer.GenericSerializer;
import com.plugins.serializer.ProtocbuffSerializer;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.exceptions.JedisException;


/**
 *	单点redis  操作工具类
 */
public class SingleManager  implements RedisManager{
	
	RedisConfig redisConfig;
	GenericSerializer serializer;
	
	
	public SingleManager(RedisConfig redisConfig) throws JedisException{
		JedisFactory._initJedisPool(redisConfig);
	}
	
	@Override
	public void addObjectToRedis(final String key, final Object obj) throws JedisException {
		addObjectToRedis(key, obj,null);
	}

	@Override
	public void addObjectToRedis(final String key, final Object obj, final Integer seconds) throws JedisException {
		new Thread(){
			Jedis _jedis=null;
			Pipeline pipeline=null;
			boolean broken = false;
			public void run() {
				try {
					_jedis=JedisFactory._getJedis();
					pipeline=JedisFactory._getPipeline(_jedis);
					if(null==_jedis)
						return ;
					pipeline.multi();
					if(null==seconds)
						pipeline.set(serializer(key), serializer(obj));
					else
						pipeline.setex(serializer(key), seconds, serializer(obj));
					pipeline.exec();
				} catch(JedisException e ){
						broken = JedisFactory.handleJedisException(e);
				}finally{
					pipeline.sync();
					JedisFactory.closeResource(_jedis, broken);
				}
			};
		}.start();
	}
	
	@Override
	public void addObjectToRedisBatch(final Map<String, Object> objs) throws JedisException {
		addObjectToRedisBatch(objs,null);
	}

	@Override
	public void addObjectToRedisBatch(final Map<String, Object> objs, final Integer seconds) throws JedisException {
		if(!objs.isEmpty()){
			new Thread(){
				public void run() {
					Jedis _jedis=null;
					Pipeline pipeline=null;
					boolean broken = false;
					try {
						_jedis=JedisFactory._getJedis();
						pipeline=JedisFactory._getPipeline(_jedis);
						if(null==pipeline)
							return ;
						pipeline.multi();
						for (Entry<String, Object> e:objs.entrySet()) {
							if(null==seconds)
								pipeline.set(serializer(e.getKey()), serializer(e.getValue()));
							else
								pipeline.setex(serializer(e.getKey()), seconds, serializer(e.getValue()));
						}
						pipeline.exec();
					} catch(JedisException e){
						broken = JedisFactory.handleJedisException(e);
					}finally{
						pipeline.sync();
						JedisFactory.closeResource(_jedis, broken);
					}
				};
			}.start();
		}
	}

	@Override
	public <T> T getObjectFromRedis(final String key, final Class<T> clazz) throws JedisException {
		if(!Strings.isNullOrEmpty(key)){
			byte[] bytes=JedisFactory._getJedis().get(serializer(key));
			return (null==bytes)?null:deserialize(bytes, clazz);
		}else{
			throw new NullPointerException();
		}
	}

	@Override
	public boolean removeObjectFromRedis(final String key) throws JedisException {
		if(!Strings.isNullOrEmpty(key)){
			return (JedisFactory._getJedis().del(serializer(key))>0)?true:false;
		}else{
			throw new NullPointerException();
		}
	}

	@Override
	public void expire(final String key, final Integer seconds) throws JedisException {
		if(!Strings.isNullOrEmpty(key)){
			JedisFactory._getJedis().expire(serializer(key),seconds);
		}else{
			throw new NullPointerException();
		}
	}

	@Override
	public boolean exists(String key) throws JedisException {
		if(!Strings.isNullOrEmpty(key)){
			return JedisFactory._getJedis().exists(serializer(key));
		}else{
			throw new NullPointerException();
		}
	}

	@Override
	public <T> byte[] serializer(T object) throws JedisException {
		if(null==this.serializer)
			serializer=new ProtocbuffSerializer();
		return serializer.serialize(object);
	}

	@Override
	public <T> T deserialize(byte[] bytes, Class<T> cls) throws JedisException {
		if(null==this.serializer)
			serializer=new ProtocbuffSerializer();
		return serializer.deserialize(bytes, cls);
	}
	
	
}
