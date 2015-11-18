package com.plugins.redis;

import com.plugins.serializer.GenericSerializer;
import com.plugins.serializer.ProtocbuffSerializer;

public abstract class RedisManagerCustom implements RedisManager {
	
	GenericSerializer serializer;
	
	@Override
	public <T> byte[] serializer(final T object) throws Exception{
		if(null==this.serializer)
			serializer=new ProtocbuffSerializer();
		return serializer.serialize(object);
	}
	
	@Override
	public <T>T deserialize(final byte[] bytes, final Class<T> cls) throws Exception{
		if(null==this.serializer)
			serializer=new ProtocbuffSerializer();
		return serializer.deserialize(bytes, cls);
	}

	@Override
	public abstract void addObjectToRedis(String key, Object obj) throws Exception; 

	@Override
	public abstract void addObjectToRedis(String key, Object obj, Integer seconds) throws Exception;

	@Override
	public abstract <T> T getObjectFromRedis(String key, Class<T> clazz) throws Exception;

	@Override
	public abstract boolean removeObjectFromRedis(String key) throws Exception;

	@Override
	public abstract void expire(String key, Integer seconds) throws Exception;

	@Override
	public abstract boolean exists(String key) throws Exception;
}
