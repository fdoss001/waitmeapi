package com.waitme.exception;

/**
 * Class to represent an exception where no timesheets exist for a user
 * @author Fernando Dos Santos
 * @version 1.0 2019-05-16
 * @since 1.0 2019-05-16
 */
public class NoTimesheetException extends NoResultException {

	private static final long serialVersionUID = 1L;

	public NoTimesheetException(String message) {
		super(message);
	}
	
}
