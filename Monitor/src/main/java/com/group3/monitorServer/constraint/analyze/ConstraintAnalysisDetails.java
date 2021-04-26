package com.group3.monitorServer.constraint.analyze;

import com.group3.monitorServer.constraint.Constraint;

/**
 * Class used to contain the information resulting from analyzing {@link Constraint}s
 */
public class ConstraintAnalysisDetails {

	private boolean violated;
	private Constraint constraint;
	private long diff;
	
	/**
	 * Constructor for the ConstraintAnalysisDetails
	 * 
	 * @param violated Representing if {@link Constraint}s where violated.
	 * @param constraint The {@link Constraint} which was checked.
	 * @param diff The difference in the analyzed values.
	 */
	public ConstraintAnalysisDetails(boolean violated, Constraint constraint, long diff) {
		this.violated = violated;
		this.constraint = constraint;
		this.diff = diff;
	}

	/**
	 * Get the result of the analysis
	 * 
	 * @return Whether a {@link Constraint} were violated
	 */
	public boolean isViolated() {
		return violated;
	}

	/**
	 * Get the {@link Constraint} which was checked against.
	 * 
	 * @return The {@link Constraint} instance which were checked during the analysis.
	 */
	public Constraint getConstraint() {
		return constraint;
	}

	/**
	 * Get the difference in values as calculated during the analysis.
	 * 
	 * @return The resulting difference in values from the analysis.
	 */
	public long getDiff() {
		return diff;
	}
	
	
	
}
