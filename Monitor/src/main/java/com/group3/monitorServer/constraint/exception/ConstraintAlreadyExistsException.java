package com.group3.monitorServer.constraint.exception;

/**
 * Exception thrown when trying to add a {@link Constraint} that already exist in a {@link ConstraintStore}.
 */
public class ConstraintAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for the ConstraintAlreadyExistsException
	 * 
	 * @param msg The message that is to be displayed along with the Stacktrace.
	 */
	public ConstraintAlreadyExistsException(String msg) {
		super(msg);
	}

}
