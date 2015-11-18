package com.plugins.redis.suppor.config;

import java.nio.file.NoSuchFileException;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.plugins.redis.suppor.PropertiesUtil;

public class RefreshRedisConfigFile implements RefreshFile{
	
	Map<String, String> fileValue=Maps.newHashMap();//文件键值对集合

	@Override
	public void refresh2Memory(RedisConfig redisConfig,String configpath) throws Exception{
		_readFile(new PropertiesUtil(configpath));
		refreshConfig(redisConfig);
	}
	
	/**
	 * 	填充键值map
	 * @param _propertiesUtil
	 * @throws Exception
	 */
	void _readFile(final PropertiesUtil _propertiesUtil) throws Exception{
		if(null==_propertiesUtil)
			throw new NoSuchFileException("file not fount:redis properties not fount ");
		fileValue=_propertiesUtil.getAllProperty();
	}
	
	
	void refreshConfig(RedisConfig redisConfig){
		Map<String, String> _hostAndPort=Maps.newHashMap();
		synchronized (redisConfig) {
			if(!fileValue.isEmpty()){
				for (Entry<String, String> entry:fileValue.entrySet()) {
					if(redisConfig.IFCLUSTER_KEY.equals(entry.getKey())){
						if(Strings.isNullOrEmpty(entry.getValue())){
							if("true".equals(entry.getValue()))
								redisConfig.setIfCluster(true);
							else
								redisConfig.setIfCluster(false);
						}
					}else if(redisConfig.DATABASE_KEY.equals(entry.getKey())){
						if(Strings.isNullOrEmpty(entry.getValue()))
							redisConfig.setDatabase(Integer.valueOf(entry.getValue()));
					}else{
						if(!Strings.isNullOrEmpty(entry.getValue()))
							_hostAndPort.put(entry.getKey(), entry.getValue());
					}
				}
			}
			redisConfig.setHostAndPorts(_hostAndPort);
		}
	}

}
