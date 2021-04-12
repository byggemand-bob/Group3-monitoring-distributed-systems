package com.group3.scheduling;

public class TaskAlreadyExistsExecption extends RuntimeException {
	
	/**
	 * Version ID used for serialization
	 */
	private static final long serialVersionUID = 1L;

	public TaskAlreadyExistsExecption(String errorMessage) {
		super(errorMessage);
	}
}
