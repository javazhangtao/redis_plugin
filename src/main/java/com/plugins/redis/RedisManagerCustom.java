//package com.plugins.redis;
//
//import org.springframework.context.annotation.Scope;
//
//import com.plugins.serializer.GenericSerializer;
//import com.plugins.serializer.ProtocbuffSerializer;
//public abstract class RedisManagerCustom implements RedisManager {
//	
//	static GenericSerializer serializer;
//	
//	/* (non-Javadoc)
//	 * @see com.plugins.redis.RedisManager#serializer(java.lang.Object)
//	 * 序列化		默认二进制
//	 */
//	@Override
//	public static <T> byte[] serializer(final T object) throws Exception{
//		if(null==serializer)
//			serializer=new ProtocbuffSerializer();
//		return serializer.serialize(object);
//	}
//	
//	/* (non-Javadoc)
//	 * @see com.plugins.redis.RedisManager#deserialize(byte[], java.lang.Class)
//	 * 反序列化		默认二进制
//	 */
//	@Override
//	public static <T>T deserialize(final byte[] bytes, final Class<T> cls) throws Exception{
//		if(null==serializer)
//			serializer=new ProtocbuffSerializer();
//		return serializer.deserialize(bytes, cls);
//	}
//
//	@Override
//	public static abstract void addObjectToRedis(String key, Object obj) throws Exception; 
//
//	@Override
//	public static abstract void addObjectToRedis(String key, Object obj, Integer seconds) throws Exception;
//
//	@Override
//	public static abstract <T> T getObjectFromRedis(String key, Class<T> clazz) throws Exception;
//
//	@Override
//	public static abstract boolean removeObjectFromRedis(String key) throws Exception;
//
//	@Override
//	public static abstract void expire(String key, Integer seconds) throws Exception;
//
//	@Override
//	public static abstract boolean exists(String key) throws Exception;
//}
