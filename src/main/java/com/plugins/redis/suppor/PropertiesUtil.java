package com.plugins.redis.suppor;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;


public class PropertiesUtil {
	Properties props;
	public PropertiesUtil(String ...fileNames)  throws Exception{
		readProperties(fileNames);
	}

	private void readProperties(String ...fileNames) throws Exception{
		InputStream fis = null ;
		try {
			props = new Properties();
			if(fileNames != null && fileNames.length > 0) {
				for(String fileName : fileNames) {
					fis = getClass().getResourceAsStream(fileName);
					this.props.load(fis);
				}
			}
		} catch (Exception e) {
			throw new Exception();
		}finally{
			if(null!=fis)
				fis.close();
		}
		
	}

	/**
	 * 获取某个属性
	 */
	public String getProperty(String key) {
		return this.props.getProperty(key);
	}

	/**
	 * 获取所有属性，返回一个map,不常用 可以试试props.putAll(t)
	 */
	public Map<String, String> getAllProperty() {
		Map map =Maps.newHashMap();
		Enumeration enu = this.props.propertyNames();
		while (enu.hasMoreElements()) {
			String key = (String) enu.nextElement();
			String value = Strings.emptyToNull(this.props.getProperty(key));
			if(Strings.isNullOrEmpty(key))
				continue ;
			map.put(key, value);
		}
		return map;
	}
}
