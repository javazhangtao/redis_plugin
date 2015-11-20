package com.plugins.redis;

import org.springframework.beans.factory.FactoryBean;

import com.plugins.redis.cluster.ClusterManager;
import com.plugins.redis.single.SingleManager;
import com.plugins.redis.suppor.config.RedisConfig;

/**
 *	redis工具生产工厂
 */
public class RedisManagerFactory implements FactoryBean<RedisManager>{

	private RedisConfig redisConfig;

	@Override
	public RedisManager getObject() throws Exception {
		if(redisConfig.isIfCluster())
			return new ClusterManager(redisConfig); 
		else 
			return new SingleManager(redisConfig);
	}

	@Override
	public Class<?> getObjectType() {
		if(redisConfig.isIfCluster())
			return ClusterManager.class;
		else
			return SingleManager.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public RedisConfig getRedisConfig() {
		return redisConfig;
	}

	public void setRedisConfig(RedisConfig redisConfig) {
		this.redisConfig = redisConfig;
	}
}
