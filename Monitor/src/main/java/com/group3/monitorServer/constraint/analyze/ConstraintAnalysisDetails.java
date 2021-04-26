package com.group3.monitorServer.constraint.analyze;

import com.group3.monitorServer.constraint.Constraint;

public class ConstraintAnalysisDetails {

	private boolean violated;
	private Constraint constraint;
	private long diff;
	
	public ConstraintAnalysisDetails(boolean violated, Constraint constraint, long diff) {
		this.violated = violated;
		this.constraint = constraint;
		this.diff = diff;
	}

	public boolean isViolated() {
		return violated;
	}

	public Constraint getConstraint() {
		return constraint;
	}

	public long getDiff() {
		return diff;
	}
	
	
	
}
