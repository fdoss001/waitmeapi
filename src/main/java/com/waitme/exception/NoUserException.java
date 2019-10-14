package com.waitme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.waitme.exception.NoResultException;

/**
 * Class to represent an exception where a user doesn't exist
 * @author Fernando Dos Santos
 * @version 1.0 2019-05-20
 * @since 1.0 2019-05-20
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoUserException extends NoResultException {

	private static final long serialVersionUID = 1L;

	public NoUserException(String message) {
		super(message);
	}
	
}
