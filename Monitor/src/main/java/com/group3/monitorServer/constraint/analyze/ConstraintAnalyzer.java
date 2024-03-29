package com.group3.monitorServer.constraint.analyze;

import com.group3.monitorServer.constraint.Constraint;
import com.group3.monitorServer.constraint.ConstraintKey;
import com.group3.monitorServer.constraint.store.ConstraintStore;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Is the class responsible for analyzing the data that are received by the Monitor Server.
 * <br>
 * Provides methods for analyzing different types of data.
 *
 */
public class ConstraintAnalyzer {

	private ConstraintStore constraints;
	
	/**
	 * Constructor for the ContraintAnalyzer class
	 * 
	 * @param constraints A {@link ConstraintStore} containing the constraints for the endpoints.
	 */
	public ConstraintAnalyzer(ConstraintStore constraints) {
		this.constraints = constraints;
	}
	
	/**
	 * Analyzes the timings between two timestamps on an endpoint to determine whether any {@link Constraint} defined for the endpoint are violated.
	 * <br><br>
	 * The timestamps provided to this method does not need to be any kind of order, this is handled within the execution.
	 * 
	 * @param timestampOne The first timestamp used for analyzing the timing for a call.
	 * @param timestampTwo The second timestamp used for analysing the timing for a call.
	 * @param endpoint The endpoint path the call to analyze was made to.
	 * 
	 * @return An instance of {@link ConstraintAnalysisDetails} which includes all information about the result of the analysis
	 */
	public ConstraintAnalysisDetails analyzeTimings(OffsetDateTime timestampOne, OffsetDateTime timestampTwo, String endpoint) {
		return analyzeTimings(timestampOne, timestampTwo, endpoint, false , null);
	}
	
	/**
	 * Analyzes the timings between two timestamps on an endpoint to determine whether any {@link Constraint} defined for the endpoint are violated.
	 * <br><br>
	 * The timestamps provided to this method does not need to be any kind of order, this is handled within the execution.
	 * 
	 * @param timestampOne The first timestamp used for analyzing the timing for a call.
	 * @param timestampTwo The second timestamp used for analysing the timing for a call.
	 * @param endpoint The endpoint path the call to analyze was made to.
	 * @param checkForGeneralConstraint Determines if a {@link Constraint} could not be found for a specific node, if instead a general {@link Constraint} for the endpoint should be searched for.
	 * @param nodeID The ID of the node the call was received on.
	 * 
	 * @return An instance of {@link ConstraintAnalysisDetails} which includes all information about the result of the analysis
	 */
	public ConstraintAnalysisDetails analyzeTimings(OffsetDateTime timestampOne, OffsetDateTime timestampTwo, String endpoint, boolean checkForGeneralConstraint, Integer nodeID) {		
		// Check if there is a constraint
		ConstraintKey key = new ConstraintKey(endpoint, nodeID);
		Constraint constraint = constraints.findConstraint(key, checkForGeneralConstraint);
		
		//  If constraint does not exists then return true
		if (constraint == null) {
			return new ConstraintAnalysisDetails(false, constraint, 0L);
		}
		
		// Calculate the difference between the timestamps
		long diffInMillis = calculateDifference(timestampOne, timestampTwo);
		
		// Check whether the difference conforms to the constraint
		final boolean withinBounds = (constraint.getMin() == null || constraint.getMin() <= diffInMillis) &&
				constraint.getMax() >= diffInMillis;
		return new ConstraintAnalysisDetails(!withinBounds, constraint, diffInMillis);
	}
	
	/**
	 * Calculates the positive difference between the two timestamps in milliseconds.
	 * 
	 * @param first The first timestamp.
	 * @param second The second timestamp.
	 * 
	 * @return The postive difference in milliseconds.
	 * 
	 * @see java.time.temporal.ChronoUnit#between
	 */
	private Long calculateDifference(OffsetDateTime first, OffsetDateTime second) {
		if (first.isBefore(second)) {
			return ChronoUnit.MILLIS.between(first, second);			
		} else if (first.isAfter(second)) {
			return ChronoUnit.MILLIS.between(second, first);
		}
		
		return 0L;
	}
}
