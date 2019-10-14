package com.waitme.domain.web;

import java.util.Map;

/**
 * Class to represent a rest response to the client
 * Every controller handling an rest call should wrap its reponse in this class
 * @author Fernando Dos Santos
 * @version 1.0 2019-05-30
 * @since 1.0 2019-05-30
 */
public class RestResponse {
	private String message;
	private Map<String, Object> payload;
	
	public RestResponse() {} //needed for JSON
	
	/**
	 * Build a response object with a response message and objects
	 * @param message the response message to send to client
	 * @param payload the objects to send to client in response
	 */
	public RestResponse(String message, Map<String, Object> payload) {
		this.message = message;
		this.payload = payload;
	}

	/**
	 * Gets the response message
	 * @return the message to send to client
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Gets the list of objects to respond to client
	 * @return the list of objects in the response
	 */
	public Map<String, Object> getPayload() {
		return payload;
	}	
}
