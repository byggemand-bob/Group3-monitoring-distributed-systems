package com.group3.scheduling;

public class TaskAlreadyExistsExecption extends RuntimeException {
	
	/**
	 * Version ID used for serialization
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for the TaskAlreadyExistsException.
	 * This extends the Java built-in {@link RuntimeException}.
	 * 
	 * @param errorMessage The error message displayed with the exception.
	 */
	public TaskAlreadyExistsExecption(String errorMessage) {
		super(errorMessage);
	}
}