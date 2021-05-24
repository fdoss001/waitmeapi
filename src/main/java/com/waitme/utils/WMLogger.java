package com.waitme.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WMLogger {

	Logger log;
	Class<?> loggingClass;
	
	public WMLogger(Class<?> loggingClass) {
		this.loggingClass = loggingClass;
//		log = LoggerFactory.getLogger(loggingClass);
	}
	
	public void debug(String msg) {
//		log.debug(msg);
		System.out.println(msg);
	}
	
	public void info(String msg) {
//		log.info(msg);
		System.out.println(msg);
	}
	
	public void warn(String msg) {
//		log.warn(msg);
		System.out.println(msg);
	}
	
	public void error(String msg) {
//		log.error(msg);
		System.err.println(msg);
	}
	
	public void error(String msg, Exception e) {
//		log.error(msg);
		System.err.println(msg);
		e.printStackTrace();
	}
}
