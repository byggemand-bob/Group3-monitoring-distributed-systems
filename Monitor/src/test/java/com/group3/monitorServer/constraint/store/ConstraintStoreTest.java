package com.group3.monitorServer.constraint.store;

import com.group3.monitorServer.constraint.Constraint;
import com.group3.monitorServer.constraint.ConstraintKey;
import com.group3.monitorServer.constraint.exception.ConstraintAlreadyExistsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ConstraintStoreTest {

	Constraint constraint;
	ConstraintStore constraintStore = new ConstraintStore();
	
	@BeforeEach
	void Initialize() {
		constraint = new Constraint("/monitor/error", 10);
		constraintStore = new ConstraintStore();
	}
	
	// Add constraint tests
	@Test
	void addConstraintSuccessTest() {
		//Setup
		int SizeOfConstraintStore = 0;
		
		//Act
		constraintStore.addConstraint(constraint);
		try {
			SizeOfConstraintStore = GetConstraintHashMap().size();
		} catch (Exception e) {}
		
		//Assert
		assertEquals(1, SizeOfConstraintStore);
	}
	
	@Test
	void addConstraintThatAlreadyExistsThrowsError()  {
		//Setup
		constraintStore.addConstraint(constraint);
				
		//Assert
		Assertions.assertThrows(ConstraintAlreadyExistsException.class, () -> {
			constraintStore.addConstraint(constraint);
		});
	}
	
	@Test
	void addConstraintDoesNotMutateInformationTest() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		// Setup
		String expectedEndpoint = "/monitor/test";
		String expectedDescription = "This be description";
		String expectedName = "42";
		Integer expectedMin = null;
		Integer expectedMax = 7;
		Integer expectedNodeID = 2;
		
		Constraint con = new Constraint(expectedEndpoint, expectedMax)
				.withDescription(expectedDescription)
				.withName(expectedName)
				.withMin(expectedMin)
				.withNodeID(expectedNodeID);
		
		// Act
		constraintStore.addConstraint(con);
		ConstraintKey key = new ConstraintKey(con.getEndpoint(), con.getNodeID());
		Constraint storedConstraint = GetConstraintHashMap().get(key);
		
		// Assert
		assertEquals(con, storedConstraint);
		assertEquals(expectedEndpoint, storedConstraint.getEndpoint());
		assertEquals(expectedDescription, storedConstraint.getDescription());
		assertEquals(expectedName, storedConstraint.getName());
		assertEquals(expectedMax.intValue(), storedConstraint.getMax().intValue());
		assertEquals(expectedNodeID.intValue(), storedConstraint.getNodeID().intValue());
		assertNull(storedConstraint.getMin());
		assertEquals(expectedMin, storedConstraint.getMin());
	}
	
	// Find constraint tests
	@Test
	void findConstraintSuccessTest() {
		
		//Setup
		ConstraintKey key = new ConstraintKey(constraint.getEndpoint(), constraint.getNodeID());
		constraintStore.addConstraint(constraint);
		
		//Act
		Constraint foundConstraint = constraintStore.findConstraint(key);
		
		//Assert
		assertSame(constraint, foundConstraint);
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
	
	@Test
	void findConstraintDoesNotFindGeneralIfNotAllowedIfSpecificExist() {
		//Setup
		String endpoint = "/test/endpoint";
		Integer max = 13;
		Integer nodeID = 7;
		Constraint conSpecific = new Constraint(endpoint, max).withNodeID(nodeID);
		Constraint conGeneral = new Constraint(endpoint, max);
		
		constraintStore.addConstraint(conSpecific);
		constraintStore.addConstraint(conGeneral);
		
		//Act
		ConstraintKey specificKey = new ConstraintKey(conSpecific.getEndpoint(), conSpecific.getNodeID());
		Constraint foundConstraint = constraintStore.findConstraint(specificKey);
		
		//Assert
		assertSame(conSpecific, foundConstraint);
	}
	
	@Test
	void findConstraintDoesNotFindGeneralIfNotAllowedIfSpecificDoesNotExist() {
		//Setup
		String endpoint = "/endpoint/test";
		Integer max = 13;
		Integer nodeID = 7;
		Constraint conGeneral = new Constraint(endpoint, max);
		
		constraintStore.addConstraint(conGeneral);
		
		//Act
		ConstraintKey specificKey = new ConstraintKey(endpoint, nodeID);
		Constraint foundConstraint = constraintStore.findConstraint(specificKey);
		
		//Assert
		assertNull(foundConstraint);
	}
	
	@Test
	void findConstraintDoesNotFindGeneralIfAllowedIfSpecificDoesExist() {
		//Setup
		String endpoint = "/test/endpoint";
		Integer max = 13;
		Integer nodeID = 7;
		Constraint conSpecific = new Constraint(endpoint, max).withNodeID(nodeID);
		Constraint conGeneral = new Constraint(endpoint, max);
		boolean isGeneralAllowed = true;
		
		constraintStore.addConstraint(conSpecific);
		constraintStore.addConstraint(conGeneral);
		
		//Act
		ConstraintKey specificKey = new ConstraintKey(conSpecific.getEndpoint(), conSpecific.getNodeID());
		Constraint foundConstraint = constraintStore.findConstraint(specificKey, isGeneralAllowed);
		
		//Assert
		assertSame(conSpecific, foundConstraint);
	}
	
	@Test
	void findConstraintFindGeneralIfAllowedIfSpecificDoesNotExist() {
		//Setup
		String endpoint = "/test/endpoint";
		Integer max = 13;
		Integer nodeID = 7;
		Constraint conGeneral = new Constraint(endpoint, max);
		boolean isGeneralAllowed = true;
		
		constraintStore.addConstraint(conGeneral);
		
		//Act
		ConstraintKey specificKey = new ConstraintKey(endpoint, nodeID);
		Constraint foundConstraint = constraintStore.findConstraint(specificKey, isGeneralAllowed);
		
		//Assert
		assertSame(conGeneral, foundConstraint);
	}
	
	// Remove constraint tests
	@Test
	void removeConstraintSuccessTest() {
		//Setup
		int SizeOfConstraintStore = 0;
		ConstraintKey key = new ConstraintKey(constraint.getEndpoint(), constraint.getNodeID());
		constraintStore.addConstraint(constraint);	
		
		//Act
		boolean removed = constraintStore.removeConstraint(key);
		try {
			SizeOfConstraintStore = GetConstraintHashMap().size();
		} catch (Exception e) {}
		
		//Assert
		assertEquals(0, SizeOfConstraintStore);
		assertTrue(removed);
	}
	
	@Test 
	void removeConstraintsThatDoesNotExists() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		//Setup
		ConstraintKey key = new ConstraintKey(constraint.getEndpoint(), constraint.getNodeID());
		boolean isDeleted = false;
		final int expectedSize = GetConstraintHashMap().size();
		
		//Act
		isDeleted = constraintStore.removeConstraint(key);
		final int actualSize = GetConstraintHashMap().size();
		
		//Assert
		assertFalse(isDeleted);
		assertEquals(expectedSize, actualSize);
	}
	
	
	// Utility methods
	@SuppressWarnings({ "unchecked", "unused" })
	private HashMap<ConstraintKey, Constraint> GetConstraintHashMap() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Map<ConstraintKey, Constraint> constraints;
		java.lang.reflect.Field field = constraintStore.getClass().getDeclaredField("constraints");
		field.setAccessible(true);
		constraints = (HashMap<ConstraintKey, Constraint>)field.get(constraintStore);
		field.setAccessible(false);
		return (HashMap<ConstraintKey, Constraint>) constraints;
	}

}
