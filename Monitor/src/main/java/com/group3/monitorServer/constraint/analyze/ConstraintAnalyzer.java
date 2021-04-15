package com.group3.monitorServer.constraint.analyze;

import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.temporal.ChronoUnit;

import com.group3.monitorServer.constraint.Constraint;
import com.group3.monitorServer.constraint.ConstraintKey;
import com.group3.monitorServer.constraint.store.ConstraintStore;

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
	 * Analyzes the timings between two timestamps on an endpoint to determine whether any {@link Constraints} defined for the endpoint are violated.
	 * <br><br>
	 * The timestamps provided to this method does not need to be any kind of order, this is handled within the execution.
	 * 
	 * @param timestampOne The first timestamp used for analyzing the timing for a call.
	 * @param timestampTwo The second timestamp used for analysing the timing for a call.
	 * @param endpoint The endpoint path the call to analyze was made to.
	 * 
	 * @return True if difference between timestamps are within the boundaries of the {@link Constraint} or if a {@link Constraint} is not defined for the endpoint.
	 * False if difference between timestamps are not within the defined minimum (optional) and maximum differences defined for the {@link Constraint} (both minimum and maximum is inclusive).
	 */
	public boolean analyzeTimings(OffsetDateTime timestampOne, OffsetDateTime timestampTwo, String endpoint) {
		return analyzeTimings(timestampOne, timestampTwo, endpoint, false , null);
	}
	
	/**
	 * Analyzes the timings between two timestamps on an endpoint to determine whether any {@link Constraints} defined for the endpoint are violated.
	 * <br><br>
	 * The timestamps provided to this method does not need to be any kind of order, this is handled within the execution.
	 * 
	 * @param timestampOne The first timestamp used for analyzing the timing for a call.
	 * @param timestampTwo The second timestamp used for analysing the timing for a call.
	 * @param endpoint The endpoint path the call to analyze was made to.
	 * @param checkForGeneralConstraint Determines if a {@link Constraint} could not be found for a specific node, if instead a general {@link Constraint} for the endpoint should be searched for.
	 * @param nodeID The ID of the node the call was received on.
	 * 
	 * @return True if difference between timestamps are within the boundaries of the {@link Constraint} or if a {@link Constraint} is not defined for the endpoint.
	 * False if difference between timestamps are not within the defined minimum (optional) and maximum differences defined for the {@link Constraint} (both minimum and maximum is inclusive).
	 */
	public boolean analyzeTimings(OffsetDateTime timestampOne, OffsetDateTime timestampTwo, String endpoint, boolean checkForGeneralConstraint, Integer nodeID) {
		// Check if there is a constraint
		ConstraintKey key = new ConstraintKey(endpoint, nodeID);
		Constraint constraint = constraints.findConstraint(key, checkForGeneralConstraint);
		
		//  If constraint does not exists then return true
		if (constraint == null) {
			return true;
		}
		
		// Calculate the difference between the timestamps
		long diffInMillis = 0;
		if (timestampOne.isBefore(timestampTwo)) {
			diffInMillis = calculateDifference(timestampOne, timestampTwo);
		} else if (timestampOne.isAfter(timestampTwo)) {
			diffInMillis = calculateDifference(timestampTwo, timestampOne);
		}
		
		// Check whether the difference conforms to the constraint
		return (constraint.getMin() == null || constraint.getMin() <= diffInMillis) &&
				constraint.getMax() >= diffInMillis;
	}
	
	/**
	 * Calculates the difference between two timestamps in milliseconds.
	 * 
	 * @param start The earliest timestamp.
	 * @param end The latest timestamp.
	 * 
	 * @return The difference in milliseconds.
	 * 
	 * @see org.threeten.bp.temporal.ChronoUnit#between
	 */
	private Long calculateDifference(OffsetDateTime start, OffsetDateTime end) {
		return ChronoUnit.MILLIS.between(start, end);
	}
}
