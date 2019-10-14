package com.waitme.exception;

/**
 * Class to represent an exception with deleting from DB.
 * @author Fernando Dos Santos
 * @version 1.0 2019-06-10
 * @since 1.0 2019-06-10
 */
public class DeleteException extends Exception {

	private static final long serialVersionUID = 1L;

	public DeleteException(String message) {
		super(message);
	}
	
}
