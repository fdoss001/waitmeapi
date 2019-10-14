package com.waitme.exception;

/**
 * Class to represent a generic exception where no result was retrieved from the DB
 * @author Fernando Dos Santos
 * @version 1.0 2019-05-16
 * @since 1.0 2019-05-16
 */
public class NoResultException extends Exception {

	private static final long serialVersionUID = 1L;

	public NoResultException(String message) {
		super(message);
	}
	
}
