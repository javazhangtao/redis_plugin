package com.plugins.redis.suppor.config;

import java.util.Map;

import org.springframework.beans.factory.InitializingBean;

import com.google.common.collect.Maps;
import com.plugins.redis.suppor.RedisConfigFileWatch;

/**
 *	redis config 配置类
 */
public class RedisConfig implements InitializingBean{
	//通用变量
	public final String  IFCLUSTER_KEY="ifCluster";
	public final String DATABASE_KEY="database";
	private Map<String,String> hostAndPorts=Maps.newHashMap();//地址集合
	String configpath="/redis.properties";//默认文件路径
	boolean ifCluster=true;//默认集群
	boolean ifWatch=false;//是否监听文件更改
	
	//非集群配置
	int database = 0;//单点
	//非集群连接池配置信息
	Integer maxActive=-1;//一个pool分配最多jedis实例，-1 表示无 限制
	Integer maxIdle=10;//一个pool最大空闲连接数
	Integer maxWait=(1000*100);//最大等待时间
	boolean testOnBorrow=true;//取出jedis实例前是否验证实例可用，验证，确保每个jedis实例可连接
	
	@Override
	public void afterPropertiesSet() throws Exception {
		_init();
	}
	
	void _init(){
		try {
			if(this.hostAndPorts.isEmpty()){
				if(this.ifWatch){
					new RedisConfigFileWatch(this, this.configpath,new RefreshRedisConfigFile());
				}else{
					new RefreshRedisConfigFile().refresh2Memory(this, this.configpath);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		};
	}

	public Map<String, String> getHostAndPorts() {
		return hostAndPorts;
	}

	public void setHostAndPorts(Map<String, String> hostAndPorts) {
		this.hostAndPorts = hostAndPorts;
	}

	public boolean isIfCluster() {
		return ifCluster;
	}

	public void setIfCluster(boolean ifCluster) {
		this.ifCluster = ifCluster;
	}

	public boolean getIfWatch() {
		return ifWatch;
	}

	public void setIfWatch(boolean ifWatch) {
		this.ifWatch = ifWatch;
	}

	public int getDatabase() {
		return database;
	}

	public void setDatabase(int database) {
		this.database = database;
	}

	public String getConfigpath() {
		return configpath;
	}

	public void setConfigpath(String configpath) {
		this.configpath = configpath;
	}
	
	public Integer getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(Integer maxActive) {
		this.maxActive = maxActive;
	}

	public Integer getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(Integer maxIdle) {
		this.maxIdle = maxIdle;
	}

	public Integer getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(Integer maxWait) {
		this.maxWait = maxWait;
	}

	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}
}
