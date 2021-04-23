package com.group3.monitorServer.constraint.analyse;

import com.group3.monitorServer.constraint.Constraint;
import com.group3.monitorServer.constraint.analyze.ConstraintAnalyzer;
import com.group3.monitorServer.constraint.store.ConstraintStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConstraintAnalyserTest {
	
	private ConstraintAnalyzer analyzer;
	private ConstraintStore store;
	
	@BeforeEach
	void setup() {
		store = new ConstraintStore();
		analyzer = new ConstraintAnalyzer(store);
	}

	// Analyze timings tests
	@Test
	void AnalyzeTimingConstraintNotfound() {
		//Setup
		final String endpoint = "/monitor/test";
		OffsetDateTime first = OffsetDateTime.now();
		OffsetDateTime second = first.minusMinutes(3L);

		//Act
		boolean isChecked = analyzer.analyzeTimings(first, second, endpoint);
		
		//Assert
		assertTrue(isChecked);
	}
	
	@Test
	void AnalyseTimingCheckGeneralNotFound() {
		//Setup		
		OffsetDateTime first = OffsetDateTime.now();
		OffsetDateTime second = first.minusSeconds(3);
		final String endpoint = "/monitor/test";
		final Integer max = 3;
		final Integer nodeID = 52;
		Constraint constraint = new Constraint(endpoint, max);
		store.addConstraint(constraint);
		
		//Act
		boolean isChecked = analyzer.analyzeTimings(first, second, endpoint, false, nodeID);
		
		//Assert
		assertTrue(isChecked);
	}
	
	@Test
	void AnalyseTimingMinIsNullMaxIsGreaterThenDiffInMillis() {
		//Setup
		OffsetDateTime first = OffsetDateTime.now();
		OffsetDateTime second = first.minusSeconds(3);
		final String endpoint = "/monitor/test";
		final Integer max = 4000;
		Constraint constraint = new Constraint(endpoint, max);
		store.addConstraint(constraint);
		
		//Act
		boolean isChecked = analyzer.analyzeTimings(first, second, endpoint);
		
		//Assert
		assertTrue(isChecked);
	}

	@Test
	void AnalyseTimingMinLessThenDiffInMillisAndMaxGreaterThenDiffInMillis() {
		//Setup
		OffsetDateTime first = OffsetDateTime.now();
		OffsetDateTime second = first.minusSeconds(3);
		final String endpoint = "/monitor/test";
		final Integer max = 4000;
		final Integer min = 2000;
		Constraint constraint = new Constraint(endpoint, max).withMin(min);
		store.addConstraint(constraint);
		
		//Act
		boolean isChecked = analyzer.analyzeTimings(first, second, endpoint);
		
		//Assert
		assertTrue(isChecked);
	}
	
	@Test
	void AnalyzseTimingMinIsGreaterThenDiffInMillisAndMaxGreaterThenDiffInMillis() {
		//Setup
		OffsetDateTime first = OffsetDateTime.now();
		OffsetDateTime second = first.minusSeconds(2);
		final String endpoint = "/monitor/test";
		final Integer max = 4000;
		final Integer min = 3000;
		Constraint constraint = new Constraint(endpoint, max).withMin(min);
		store.addConstraint(constraint);
		
		//Act
		boolean isChecked = analyzer.analyzeTimings(first, second, endpoint);
		
		//Assert
		assertFalse(isChecked);
	}
		
	@Test
	void AnalyseTimingMinTheSameAsDiffInMillis() {
		//Setup
		OffsetDateTime first = OffsetDateTime.now();
		OffsetDateTime second = first.minusSeconds(2);
		final String endpoint = "/monitor/test";
		final Integer max = 4000;
		final Integer min = 2000;
		Constraint constraint = new Constraint(endpoint, max).withMin(min);
		store.addConstraint(constraint);
		
		//Act
		boolean isChecked = analyzer.analyzeTimings(first, second, endpoint);
		
		//Assert
		assertTrue(isChecked);
	}
	
	@Test
	void AnalyzseTimingMaxTheSameAsDiffInMillis() {
		//Setup
		OffsetDateTime first = OffsetDateTime.now();
		OffsetDateTime second = first.minusSeconds(3);
		final String endpoint = "/monitor/test";
		final Integer max = 3000;
		final Integer min = 2000;
		Constraint constraint = new Constraint(endpoint, max).withMin(min);
		store.addConstraint(constraint);
		
		//Act
		boolean isChecked = analyzer.analyzeTimings(first, second, endpoint);
		
		//Assert
		assertTrue(isChecked);
	}
	
	@Test
	void AnalyzseTimingMinAndMaxIsTheSameAsDiffInMillis() {
		//Setup
		OffsetDateTime first = OffsetDateTime.now();
		OffsetDateTime second = first.minusSeconds(2);
		final String endpoint = "/monitor/test";
		final Integer max = 2000;
		final Integer min = 2000;
		Constraint constraint = new Constraint(endpoint, max).withMin(min);
		store.addConstraint(constraint);
		
		//Act
		boolean isChecked = analyzer.analyzeTimings(first, second, endpoint);
		
		//Assert
		assertTrue(isChecked);
	}
	
	@Test
	void AnalyzseTimingMinIsNullAndMaxIsLessThenDiffInMillis() {
		//Setup
		OffsetDateTime first = OffsetDateTime.now();
		OffsetDateTime second = first.minusSeconds(3);
		final String endpoint = "/monitor/test";
		final Integer max = 2000;
		Constraint constraint = new Constraint(endpoint, max);
		store.addConstraint(constraint);
		
		//Act
		boolean isChecked = analyzer.analyzeTimings(first, second, endpoint);
		
		//Assert
		assertFalse(isChecked);
	}
	
	// Calculate Difference Tests
	@Test
	void CalculateDifferenceIfTimestamp1Before2ThenPositiveDifference() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		//Setup
		java.lang.reflect.Method calcDiff = setCalculateDifferenceAccessible(true);
		OffsetDateTime first = OffsetDateTime.now();
		OffsetDateTime second = first.minusMinutes(3L);
		
		//Act
		long difference = (long) calcDiff.invoke(analyzer, first, second);
		
		//Assert
		assertTrue(difference >= 0);
		
		//Cleanup
		setCalculateDifferenceAccessible(false);
	}
	
	@Test
	void CalculateDifferenceIfTimestamp1After2ThenPositiveDifference() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		//Setup
		java.lang.reflect.Method calcDiff = setCalculateDifferenceAccessible(true);
		OffsetDateTime first = OffsetDateTime.now();
		OffsetDateTime second = first.plusMinutes(3L);
		
		//Act
		long difference = (long) calcDiff.invoke(analyzer, first, second);
		
		//Assert
		assertTrue(difference >= 0);
		
		//Cleanup
		setCalculateDifferenceAccessible(false);
	}
	
	// Utility methods
	private java.lang.reflect.Method setCalculateDifferenceAccessible(boolean accessible){
		java.lang.reflect.Method calculateDifference = null;
		
		try {
			calculateDifference = ConstraintAnalyzer.class.getDeclaredMethod("calculateDifference", OffsetDateTime.class, OffsetDateTime.class);
			calculateDifference.setAccessible(accessible);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		
		return calculateDifference;
		
	}
}
