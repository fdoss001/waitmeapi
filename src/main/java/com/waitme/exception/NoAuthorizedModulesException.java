package com.waitme.exception;

/**
 * Class to represent an exception where user doesn't have any modules
 * @author Fernando Dos Santos
 * @version 1.0 2019-05-16
 * @since 1.0 2019-05-16
 */
public class NoAuthorizedModulesException extends NoResultException {

	private static final long serialVersionUID = 1L;

	public NoAuthorizedModulesException(String message) {
		super(message);
	}
	
}
