package com.waitme.exception;

/**
 * Class to represent an exception for a waiting function
 * @author Fernando Dos Santos
 * @version 1.0 2019-08-22
 * @since 1.0 2019-08-22
 */
public class WaitingException extends Exception {

	private static final long serialVersionUID = 1L;

	public WaitingException(String message) {
		super(message);
	}
	
}
