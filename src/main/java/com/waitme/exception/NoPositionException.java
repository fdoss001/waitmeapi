package com.waitme.exception;

/**
 * Class to represent an exception where no position exists
 * @author Fernando Dos Santos
 * @version 1.0 2019-05-16
 * @since 1.0 2019-05-16
 */
public class NoPositionException extends NoResultException {

	private static final long serialVersionUID = 1L;

	public NoPositionException(String message) {
		super(message);
	}
	
}
