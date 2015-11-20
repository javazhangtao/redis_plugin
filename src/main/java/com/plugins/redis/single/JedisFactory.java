package com.plugins.redis.single;

import java.util.Map.Entry;

import com.google.common.base.Strings;
import com.plugins.redis.suppor.config.RedisConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.exceptions.JedisException;

public class JedisFactory {

	static JedisPool jedisPool;
	/**
	 * 初始化连接池
	 * @throws Exception
	 */
	public static void _initJedisPool(RedisConfig redisConfig) throws JedisException {
		if(null==jedisPool){
			if(null==redisConfig)
				throw new NullPointerException(" redis config is null ");
			JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
			jedisPoolConfig.setMaxTotal(redisConfig.getMaxActive());
			jedisPoolConfig.setMaxIdle(redisConfig.getMaxIdle());
			jedisPoolConfig.setMaxWaitMillis(redisConfig.getMaxWait());
			jedisPoolConfig.setTestOnBorrow(redisConfig.isTestOnBorrow());
			if(!redisConfig.getHostAndPorts().isEmpty()){
				for (Entry<String, String> e:redisConfig.getHostAndPorts().entrySet()) {
					String host=e.getKey();
					String port=e.getValue();
					if(Strings.isNullOrEmpty(host) || Strings.isNullOrEmpty(port))
						continue;
					try {
						jedisPool=new JedisPool(jedisPoolConfig,host,Integer.valueOf(port));
						return ;
					} catch (Exception e2) {
						continue;
					}
				}
			}
		}
	}
	
	/**
	 * 获取jedis实例
	 * @return
	 * @throws Exception
	 */
	public static Jedis _getJedis() throws JedisException {
		if(null!=jedisPool)
			synchronized (jedisPool) {
				return jedisPool.getResource();
			}
		else
			throw new JedisException(" Failed to get jedis : jedispool is null ");
	}
	
	/**
	 * 获取管道对象
	 * @param _jedis
	 * @return
	 * @throws Exception
	 */
	public static Pipeline _getPipeline(Jedis _jedis) throws JedisException {
		if(null!=_jedis)
			return _jedis.pipelined();
		else
			throw new JedisException(" Failed to get pipeline : jedis is null ");
	}
	
	public static boolean handleJedisException(JedisException jedisException) throws JedisException{
		boolean conectionBroken=true;
		if (jedisException instanceof JedisConnectionException) {
			throw new JedisException("Redis connection  lost.", jedisException);
		} else if (jedisException instanceof JedisDataException) {
			if ((jedisException.getMessage() != null) && (jedisException.getMessage().indexOf("READONLY") != -1)) {
				throw new JedisException("Redis connection  are read-only slave.", jedisException);
			} else {
				conectionBroken= false;
			}
		} else {
			throw new JedisException("Jedis exception happen.", jedisException);
		}
		return conectionBroken;
	}
	
	
	public static void closeResource(Jedis jedis, boolean conectionBroken) {
		try {
			if (conectionBroken) {
				jedisPool.returnBrokenResource(jedis);
			} else {
				jedisPool.returnResource(jedis);
			}
		} catch (Exception e) {
			destroyJedis(jedis);
		}
	}
	
	/**
	 * 在Pool以外强行销毁Jedis.
	 */
	static void destroyJedis(Jedis jedis) {
		if ((jedis != null) && jedis.isConnected()) {
			try {
				try {
					jedis.quit();
				} catch (Exception e) {
				}
				jedis.disconnect();
			} catch (Exception e) {}
		}
	}
}
