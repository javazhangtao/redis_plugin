package com.plugins.serializer;

import redis.clients.jedis.exceptions.JedisException;

public interface GenericSerializer {

	
	public <T> byte[] serialize(T obj) throws JedisException;
	
	public <T>T deserialize(byte[] bytes, Class<T> cls) throws JedisException;
}
