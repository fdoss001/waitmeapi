package com.waitme.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom properties class that reads from the given prop file
 * @author Fernando Dos Santos
 * @version 1.0 2019-02-08 
 * @since 1.0 2019-01-18
 */
public class WMProperties extends Properties {
	private static final long serialVersionUID = -519172386901524410L;
	private InputStream inputStream;
	Logger log = LoggerFactory.getLogger(WMProperties.class);
 
	public WMProperties(String propFileName) {
 
		this.inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
 
		try {
			this.load(inputStream);
			inputStream.close();
		} catch (IOException e) {
			log.error("Failed to open or load property file: " + propFileName, e);
		}
	}
	
	public InputStream getInputStream() {
		return this.inputStream;
	}
}
