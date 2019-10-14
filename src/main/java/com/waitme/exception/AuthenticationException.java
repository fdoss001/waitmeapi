package com.waitme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class to represent a generic exception where no result was retrieved from the DB
 * @author Fernando Dos Santos
 * @version 1.0 2019-05-16
 * @since 1.0 2019-05-16
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationException extends Exception {

	private static final long serialVersionUID = 1L;

	public AuthenticationException(String message) {
		super(message);
	}
	
}
