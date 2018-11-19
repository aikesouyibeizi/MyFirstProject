package com.briup.log.impl;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.briup.log.Log;

public class LogImpl implements Log{
	//java工程会在src目录下寻找.prperties文件、maven工程就会在src/main/java下寻找.properties文件
	//定义为static 可以保证LogImpl类都是用的同一个对象
	private static Logger log = Logger.getLogger(LogImpl.class);
	

	@Override
	public void init(Properties properties) throws Exception {
		
	}

	@Override
	public void debug(String message) {
		log.debug(message);
	}

	@Override
	public void info(String message) {
		log.info(message);
	}

	@Override
	public void warn(String message) {
		log.warn(message);
	}

	@Override
	public void error(String message) {
		log.error(message);
	}

	@Override
	public void fatal(String message) {
		log.fatal(message);
	}


}
