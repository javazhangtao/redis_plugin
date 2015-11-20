package com.plugins.redis;

import java.util.Map;

/**
 *	redis 操作类接口
 */
public interface RedisManager {

	
	/**
	 * 	添加无有效期限制key-value
	 * @param key
	 * @param obj
	 * @throws Exception
	 */
	public void addObjectToRedis(final String key, final Object obj) throws Exception;
	
	
	/**
	 * 添加有效期限制key-value
	 * @param key
	 * @param obj
	 * @param seconds
	 * @throws Exception
	 */
	public void addObjectToRedis(final String key, final Object obj, final Integer seconds) throws Exception;
	
	
	/**
	 * 	批量写入无有效期限制key-value
	 * @param key
	 * @param obj
	 * @throws Exception
	 */
	public void addObjectToRedisBatch(final Map<String, Object> objs) throws Exception;
	
	/**
	 * 	批量写入有效期限制key-value
	 * @param key
	 * @param obj
	 * @throws Exception
	 */
	public void addObjectToRedisBatch(final Map<String, Object> objs, final Integer seconds) throws Exception;
	
	/**
	 * 根据key值获取元素并反序列化为指定元素
	 * @param key
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public <T> T getObjectFromRedis(final String key,final Class<T> clazz)  throws Exception;
	
	/**
	 * 根据key删除
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public boolean removeObjectFromRedis(final String key)  throws Exception;
	
	/**
	 * 给指定key对应数据续期
	 * @param key
	 * @param seconds
	 * @throws Exception
	 */
	public void expire(final String key, final Integer seconds) throws Exception;
	
	/**
	 * 判断数据
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public boolean exists(final String key) throws Exception;
	
	/**
	 * 序列化
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public <T> byte[] serializer(T object) throws Exception;
	
	/**
	 * 反序列化
	 * @param bytes
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	public <T>T deserialize(byte[] bytes, Class<T> cls) throws Exception;
}
