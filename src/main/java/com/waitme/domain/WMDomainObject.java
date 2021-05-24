package com.waitme.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waitme.utils.WMLogger;

/**
 * Base class to every domain object in WaitMe
 * @author Fernando Dos Santos
 * @version 1.0 2019-07-26
 * @since 1.0 2019-07-26
 */
public abstract class WMDomainObject {	
	@JsonIgnore
	WMLogger log = new WMLogger(WMDomainObject.class);
	
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
