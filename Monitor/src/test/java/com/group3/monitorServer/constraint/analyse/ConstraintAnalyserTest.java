package com.group3.monitorServer.constraint.analyse;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.time.OffsetDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.group3.monitorServer.constraint.analyze.ConstraintAnalyzer;
import com.group3.monitorServer.constraint.store.ConstraintStore;

class ConstraintAnalyserTest {
	
	private ConstraintAnalyzer analyzer;
	private ConstraintStore store;
	
	@BeforeEach
	void setup() {
		store = new ConstraintStore();
		analyzer = new ConstraintAnalyzer(store);
	}

	// Analyze timings tests
	
	// CheckForGeneral=False,NodeID=Null,GeneralExisits=True,FindsSpecific (Make sure that Constraint is satisfied)
	// CheckForGeneral=False,NodeID=Null,GeneralExisits=True,DoesNotFindSpecificReturnTrue (because constraint does not exist)
	// CheckForGeneral=False,NodeID=Null,GeneralExisits=False,FindsSpecific (Make sure that Constraint is satisfied)
	// CheckForGeneral=False,NodeID=Null,GeneralExisits=False,DoesNotFindSpecificReturnTrue (because constraint does not exist)
	// CheckForGeneral=True,NodeID=Null,GeneralExisits=True,FindsSpecific (Make sure that Constraint is satisfied)
	// CheckForGeneral=True,NodeID=Null,GeneralExisits=True,DoesNotFindSpecificReturnTrue (because constraint does not exist)
	// CheckForGeneral=True,NodeID=Null,GeneralExisits=False,FindsSpecific (Make sure that Constraint is satisfied)
	// CheckForGeneral=True,NodeID=Null,GeneralExisits=False,DoesNotFindSpecificReturnTrue (because constraint does not exist)
	
	// ConstraintNotFound (return true by default)
	
	// 
	
	@Test
	void test() {
		fail("Not yet implemented");
		//Setup
		
		//Act
		
		//Assert
	}

	
	// Calculate Difference Tests
	// CalculateDifferenceIfTimestamp1Before2ThenPositiveDifference
	// CalculateDifferenceIfTimestamp1After2ThenPositiveDifference
	
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
