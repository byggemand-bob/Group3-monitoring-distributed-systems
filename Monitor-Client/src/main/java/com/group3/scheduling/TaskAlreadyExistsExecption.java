package com.group3.scheduling;

public class TaskAlreadyExistsExecption extends RuntimeException {
	public TaskAlreadyExistsExecption(String errorMessage) {
		super(errorMessage);
	}
}
