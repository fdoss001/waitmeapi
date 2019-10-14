package com.waitme.exception;

import com.waitme.exception.NoResultException;

/**
 * Class to represent an exception where a company doesn't exist
 * @author Fernando Dos Santos
 * @version 1.0 2019-06-25
 * @since 1.0 2019-06-25
 */
public class NoCompanyException extends NoResultException {

	private static final long serialVersionUID = 1L;

	public NoCompanyException(String message) {
		super(message);
	}
	
}
