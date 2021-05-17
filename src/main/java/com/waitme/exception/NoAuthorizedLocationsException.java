package com.waitme.exception;

/**
 * Class to represent an exception where a user has no authorized locations
 * @author Fernando Dos Santos
 * @version 1.0 2019-05-16
 * @since 1.0 2019-05-16
 */
public class NoAuthorizedLocationsException extends NoResultException {

	private static final long serialVersionUID = 1L;

	public NoAuthorizedLocationsException(String message) {
		super(message);
	}
	
}
