package com.plugins.redis;

/**
 *	redis 操作类接口
 */
public interface RedisManager {

	
	public void addObjectToRedis(final String key, final Object obj) throws Exception;
	
	public void addObjectToRedis(final String key, final Object obj, final Integer seconds) throws Exception;
	
	public <T> T getObjectFromRedis(final String key,final Class<T> clazz)  throws Exception;
	
	public boolean removeObjectFromRedis(final String key)  throws Exception;
	
	public void expire(final String key, final Integer seconds) throws Exception;
	
	public boolean exists(final String key) throws Exception;
	
	public <T> byte[] serializer(T object) throws Exception;
	
	public <T>T deserialize(byte[] bytes, Class<T> cls) throws Exception;
	
}
