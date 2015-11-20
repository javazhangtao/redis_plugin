package com.plugins;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.common.collect.Maps;
import com.plugins.redis.RedisManager;

public class MainTest {
	
	static ExecutorService service=Executors.newFixedThreadPool(1000);

	public static void main(String[] args) {
		ApplicationContext c=new ClassPathXmlApplicationContext("resource.xml");
		RedisManager custom=(RedisManager)c.getBean("redisManager");
		
		try {
			Map<String, Object> s=Maps.newHashMap();
			for (int i = 0; i < 100000; i++) {
				custom.addObjectToRedis(i+"key", i+"value");
//				s.put(i+"key", i+"value");
//				service.execute(new Send(custom, i+"key", i+"value"));
//				service.execute(new Send(i+"key", i+"value"));
			}
//			custom.addObjectToRedisBatch(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	static class Send implements Runnable{
		RedisManager custom;
		String key;
		String value;
		
		public Send(String key,String value){
			this.key=key;
			this.value=value;
		}
		
		public Send(RedisManager custom,String key,String value){
			this.custom=custom;
			this.key=key;
			this.value=value;
		}
		@Override
		public void run() {
			try {
//				custom.addObjectToRedis(key, value);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
}
