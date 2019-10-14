package com.waitme.domain.web;

import java.util.Map;

/**
 * Class to represent a rest request from the client
 * Every controller handling an rest call should receive its request in this class
 * @author Fernando Dos Santos
 * @version 1.0 2019-10-03
 * @since 1.0 2019-10-03
 */
public class RestRequest {
	private String type;
	private Map<String, Object> payload;
	
	public RestRequest() {}
	
	public RestRequest(String type, Map<String, Object> payload) {
		super();
		this.type = type;
		this.payload = payload;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, Object> getPayload() {
		return payload;
	}

	public void setPayload(Map<String, Object> payload) {
		this.payload = payload;
	}
}
