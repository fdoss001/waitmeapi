package com.waitme.exception;

/**
 * Class to represent an exception with clocking in, out, etc.
 * @author Fernando Dos Santos
 * @version 1.0 2019-05-20
 * @since 1.0 2019-05-20
 */
public class ClockException extends NoResultException {

	private static final long serialVersionUID = 1L;
	private int code;

	public ClockException(String message, int code) {
		super(message);
		this.code = code;
	}
	
	public int getCode() {
		return this.code;
	}
}
