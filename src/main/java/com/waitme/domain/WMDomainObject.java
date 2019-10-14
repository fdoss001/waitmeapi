package com.waitme.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Base class to every domain object in WaitMe
 * @author Fernando Dos Santos
 * @version 1.0 2019-07-26
 * @since 1.0 2019-07-26
 */
public abstract class WMDomainObject {	
	@JsonIgnore
	Logger log = LoggerFactory.getLogger(WMDomainObject.class);
	
	public WMDomainObject() {} //needed for JSON
	
	@Override
	public String toString() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage());
		}
		return null;
	}
}
