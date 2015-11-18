package com.plugins.serializer;

public interface GenericSerializer {

	
	public <T> byte[] serialize(T obj) throws Exception;
	
	public <T>T deserialize(byte[] bytes, Class<T> cls) throws Exception;
}
