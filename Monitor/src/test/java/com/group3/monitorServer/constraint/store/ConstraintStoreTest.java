package com.group3.monitorServer.constraint.store;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.quartz.ObjectAlreadyExistsException;

import com.group3.monitorServer.constraint.Constraint;
import com.group3.monitorServer.constraint.ConstraintKey;

class ConstraintStoreTest {

	Constraint constraint;
	ConstraintStore constraintStore = new ConstraintStore();
	
	@BeforeEach
	void Initialize() {
		constraint = new Constraint("/monitor/error", 10);
		constraintStore = new ConstraintStore();
	}
	
	@Test
	void addConstraintTest() throws ObjectAlreadyExistsException {
		//Setup
		java.lang.reflect.Field field = null;
		int SizeOfConstraintStore = 0;
		
		//Act
		constraintStore.addConstraint(constraint);
		try {
			SizeOfConstraintStore = GetSizeOfHashMap(field);
		} catch (Exception e) {}
		
		//Assert
		assertEquals(1, SizeOfConstraintStore);
	}
	
	@Test
	void findConstraintTest() throws ObjectAlreadyExistsException {
		
		//Setup
		ConstraintKey key = new ConstraintKey(constraint.getEndpoint(), constraint.getNodeID());
		constraintStore.addConstraint(constraint);
		
		//Act
		Constraint foundConstraint = constraintStore.findConstraint(key);
		
		//Assert
		assertSame(constraint, foundConstraint);
	}
	
	@Test
	void removeConstraintTest() throws ObjectAlreadyExistsException {
		//Setup
		java.lang.reflect.Field field = null;
		int SizeOfConstraintStore = 0;
		ConstraintKey key = new ConstraintKey(constraint.getEndpoint(), constraint.getNodeID());
		constraintStore.addConstraint(constraint);	
		
		//Act
		constraintStore.removeConstraint(key);
		try {
			SizeOfConstraintStore = GetSizeOfHashMap(field);
		} catch (Exception e) {}
		
		//Assert
		assertEquals(0, SizeOfConstraintStore);
	}
	
	@Test
	void removeConstraintDoWeGetABooleanTest() throws ObjectAlreadyExistsException {
		//Setup
		ConstraintKey key = new ConstraintKey(constraint.getEndpoint(), constraint.getNodeID());
		constraintStore.addConstraint(constraint);
		boolean isDeleted = false;
		
		//Act
		isDeleted = constraintStore.removeConstraint(key);
		
		//Assert
		assertTrue(isDeleted);
	}
	
	@Test
	void addConstraintThatAlreadyExistsThrowsError() throws ObjectAlreadyExistsException {
		//Setup
		constraintStore.addConstraint(constraint);
				
		//Assert
		Assertions.assertThrows(ObjectAlreadyExistsException.class, () -> {
			constraintStore.addConstraint(constraint);
		});
	}
	
	@Test 
	void removeConstraintsThatDoesNotExists() {
		//Setup
		ConstraintKey key = new ConstraintKey(constraint.getEndpoint(), constraint.getNodeID());
		boolean isDeleted = false;
		
		System.out.println(key);
		System.out.println(constraint);
		
		//Act
		isDeleted = constraintStore.removeConstraint(key);
		
		//Assert
		assertFalse(isDeleted);
	}
	
	@Test
	void findConstraintThatDoesNotExists() {
		//Setup
		ConstraintKey key = new ConstraintKey(constraint.getEndpoint(), constraint.getNodeID());
		
		//Act
		Constraint foundConstraint = constraintStore.findConstraint(key);
		
		//Assert
		assertNull(foundConstraint);
	}
	
	private int GetSizeOfHashMap(java.lang.reflect.Field field) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Map<ConstraintKey, Constraint> constraints;
		field = constraintStore.getClass().getDeclaredField("constraints");
		field.setAccessible(true);
		constraints = (HashMap<ConstraintKey, Constraint>)field.get(constraintStore);
		field.setAccessible(false);
		return constraints.size();
	}

}
