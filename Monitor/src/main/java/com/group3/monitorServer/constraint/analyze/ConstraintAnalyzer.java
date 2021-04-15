package com.group3.monitorServer.constraint.analyze;

import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.temporal.ChronoUnit;

import com.group3.monitorServer.constraint.Constraint;
import com.group3.monitorServer.constraint.ConstraintKey;
import com.group3.monitorServer.constraint.store.ConstraintStore;

public class ConstraintAnalyzer {

	private ConstraintStore constraints;
	
	public ConstraintAnalyzer(ConstraintStore constraints) {
		this.constraints = constraints;
	}
	
	public boolean analyzeTimings(OffsetDateTime timestampOne, OffsetDateTime timestampTwo, String endpoint) {
		return analyzeTimings(timestampOne, timestampTwo, endpoint, null);
	}
	
	public boolean analyzeTimings(OffsetDateTime timestampOne, OffsetDateTime timestampTwo, String endpoint, Integer nodeID) {
		// Check if there is a constraint
		ConstraintKey key = new ConstraintKey(endpoint, nodeID);
		Constraint constraint = constraints.findConstraint(key);
		
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
	
	private Long calculateDifference(OffsetDateTime start, OffsetDateTime end) {
		return ChronoUnit.MILLIS.between(start, end);
	}
}
