package com.waitme.exception;

/**
 * Class to represent an exception where a duplicate exists
 * @author Fernando Dos Santos
 * @version 1.0 2019-07-26
 * @since 1.0 2019-07-26
 */
public class DuplicateException extends Exception {

	private static final long serialVersionUID = 1L;

	public DuplicateException(String message) {
		super(message);
	}
	
}
