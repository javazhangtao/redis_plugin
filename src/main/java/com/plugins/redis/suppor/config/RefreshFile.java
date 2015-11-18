package com.plugins.redis.suppor.config;

public interface RefreshFile {
	
	/**
	 * 刷新文件内容到内存
	 * @param configpath
	 */
	void refresh2Memory(final RedisConfig redisConfig , final String configpath) throws Exception;
}
