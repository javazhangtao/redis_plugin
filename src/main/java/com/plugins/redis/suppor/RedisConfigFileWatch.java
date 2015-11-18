package com.plugins.redis.suppor;

import java.nio.file.FileSystems;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

import com.plugins.redis.suppor.config.RedisConfig;
import com.plugins.redis.suppor.config.RefreshFile;

public class RedisConfigFileWatch extends Thread{
	
	String configpath;//默认查询根路径下redis.properties文件
	RedisConfig redisConfig;
	RefreshFile refreshFile;
	
	public RedisConfigFileWatch(RedisConfig redisConfig,String _configpath,RefreshFile refreshFile) throws Exception{
		this.configpath=_configpath;
		this.redisConfig=redisConfig;
		this.refreshFile=refreshFile;
		setDaemon(true);
		this.start();
	}
	
	@Override
	public void run(){
		try {
			System.out.println("begin watch");
			Path path=Paths.get(this.configpath);
			if(null==path)
				throw new NoSuchFileException("file not found : redis properties file not fount");
			WatchService watchService=FileSystems.getDefault().newWatchService();
			path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY,StandardWatchEventKinds.ENTRY_CREATE);//添加创建，更新时监听
			WatchKey watckKey = watchService.take();
			List<WatchEvent<?>> events = watckKey.pollEvents();
			for (WatchEvent event : events) {  
	            if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE || event.kind() == StandardWatchEventKinds.ENTRY_MODIFY)  
	            	refreshFile.refresh2Memory(this.redisConfig, this.configpath);
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	

}
