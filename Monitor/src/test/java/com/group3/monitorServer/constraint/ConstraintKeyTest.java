package com.group3.monitorServer.constraint;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConstraintKeyTest {

	// Tests HashCode
	@Test
	void HashCodeOtherIsNotConstraintKey() {
		//Setup
		final String endpoint = "/endpoint";
		ConstraintKey key = new ConstraintKey(endpoint);
		Object obj = new Object();
		
		//Act
		final int keyHash = key.hashCode();
		final int objHash = obj.hashCode();
		
		//Assert
		assertNotEquals(keyHash, objHash);
	}
	
	@Test
	void HashCodeOtherIsConstraintKeyEndpointsSame() {
		//Setup
		final String endpoint = "/test";
		ConstraintKey key1 = new ConstraintKey(endpoint);
		ConstraintKey key2 = new ConstraintKey(endpoint);
		
		//Act
		final int key1Hash = key1.hashCode();
		final int key2Hash = key2.hashCode();
		
		//Assert
		assertEquals(key1Hash, key2Hash);
	}
	
	@Test
	void HashCodeOtherIsConstraintKeyEndpointsDifferent() {
		//Setup
		final String endpoint1 = "/test";
		final String endpoint2 = "/endpoint";
		ConstraintKey key1 = new ConstraintKey(endpoint1);
		ConstraintKey key2 = new ConstraintKey(endpoint2);
		
		//Act
		final int key1Hash = key1.hashCode();
		final int key2Hash = key2.hashCode();
		
		//Assert
		assertNotEquals(key1Hash, key2Hash);
	}
	
	@Test
	void HashCodeOtherIsConstraintKeyEndpointsSameFirstNodeIDNullSecondNotNull() {
		//Setup
		final String endpoint = "/test";
		final Integer nodeID = 2;
		ConstraintKey key1 = new ConstraintKey(endpoint);
		ConstraintKey key2 = new ConstraintKey(endpoint, nodeID);
		
		//Act
		final int key1Hash = key1.hashCode();
		final int key2Hash = key2.hashCode();
		
		//Assert
		assertNotEquals(key1Hash, key2Hash);
	}
	
	@Test
	void HashCodeOtherIsConstraintKeyEndpointsSameFirstNodeIDNotNullSecondNull() {
		//Setup
		final String endpoint = "/test";
		final Integer nodeID = 2;
		ConstraintKey key1 = new ConstraintKey(endpoint);
		ConstraintKey key2 = new ConstraintKey(endpoint, nodeID);
		
		//Act
		final int key1Hash = key1.hashCode();
		final int key2Hash = key2.hashCode();
		
		//Assert
		assertNotEquals(key1Hash, key2Hash);
	}
	
	@Test
	void HashCodeOtherIsConstraintKeyEndpointsSameBothNodeIDNull() {
		//Setup
		final String endpoint = "/test";
		ConstraintKey key1 = new ConstraintKey(endpoint);
		ConstraintKey key2 = new ConstraintKey(endpoint);
		
		//Act
		final int key1Hash = key1.hashCode();
		final int key2Hash = key2.hashCode();
		
		//Assert
		assertEquals(key1Hash, key2Hash);
	}
	
	@Test
	void HashCodeOtherIsConstraintKeyEndpointsSameBothNodeIDNotNullIDSame() {
		//Setup
		final String endpoint = "/test";
		final Integer nodeID = 19;
		ConstraintKey key1 = new ConstraintKey(endpoint, nodeID);
		ConstraintKey key2 = new ConstraintKey(endpoint, nodeID);
		
		//Act
		final int key1Hash = key1.hashCode();
		final int key2Hash = key2.hashCode();
		
		//Assert
		assertEquals(key1Hash, key2Hash);
	}
	
	@Test
	void HashCodeOtherIsConstraintKeyEndpointsSameBothNodeIDNotNullIDDifferent() {
		//Setup
		final String endpoint = "/test";
		final Integer nodeID1 = 1;
		final Integer nodeID2 = 3300;
		ConstraintKey key1 = new ConstraintKey(endpoint, nodeID1);
		ConstraintKey key2 = new ConstraintKey(endpoint, nodeID2);
		
		//Act
		final int key1Hash = key1.hashCode();
		final int key2Hash = key2.hashCode();
				
		//Assert
		assertNotEquals(key1Hash, key2Hash);
	}

	
	// Tests Equals
	@Test
	void EqualOtherIsNotConstraintKey() {
		//Setup
		final String endpoint = "/endpoint";
		ConstraintKey key = new ConstraintKey(endpoint);
		Object obj = new Object();
		
		//Act
		boolean actual = key.equals(obj);
		
		//Assert
		assertFalse(actual);
	}
	
	@Test
	void EqualOtherIsConstraintKeyEndpointsSame() {
		//Setup
		final String endpoint = "/test";
		ConstraintKey key1 = new ConstraintKey(endpoint);
		ConstraintKey key2 = new ConstraintKey(endpoint);
		
		//Act
		final boolean actual1 = key1.equals(key2);
		final boolean actual2 = key2.equals(key1);
		
		//Assert
		assertTrue(actual1);
		assertTrue(actual2);
	}
	
	@Test
	void EqualOtherIsConstraintKeyEndpointsDifferent() {
		//Setup
		final String endpoint1 = "/test";
		final String endpoint2 = "/endpoint";
		ConstraintKey key1 = new ConstraintKey(endpoint1);
		ConstraintKey key2 = new ConstraintKey(endpoint2);
		
		//Act
		final boolean actual1 = key1.equals(key2);
		final boolean actual2 = key2.equals(key1);
		
		//Assert
		assertFalse(actual1);
		assertFalse(actual2);
	}
	
	@Test
	void EqualOtherIsConstraintKeyEndpointsSameFirstNodeIDNullSecondNotNull() {
		//Setup
		final String endpoint = "/test";
		final Integer nodeID = 2;
		ConstraintKey key1 = new ConstraintKey(endpoint);
		ConstraintKey key2 = new ConstraintKey(endpoint, nodeID);
		
		//Act
		final boolean actual = key1.equals(key2);
		
		//Assert
		assertFalse(actual);
	}
	
	@Test
	void EqualOtherIsConstraintKeyEndpointsSameFirstNodeIDNotNullSecondNull() {
		//Setup
		final String endpoint = "/test";
		final Integer nodeID = 2;
		ConstraintKey key1 = new ConstraintKey(endpoint);
		ConstraintKey key2 = new ConstraintKey(endpoint, nodeID);
		
		//Act
		final boolean actual = key2.equals(key1);
		
		//Assert
		assertFalse(actual);	}
	
	@Test
	void EqualOtherIsConstraintKeyEndpointsSameBothNodeIDNull() {
		//Setup
		final String endpoint = "/test";
		ConstraintKey key1 = new ConstraintKey(endpoint);
		ConstraintKey key2 = new ConstraintKey(endpoint);
		
		//Act
		final boolean actual1 = key1.equals(key2);
		final boolean actual2 = key2.equals(key1);
		
		//Assert
		assertTrue(actual1);
		assertTrue(actual2);
	}
	
	@Test
	void EqualOtherIsConstraintKeyEndpointsSameBothNodeIDNotNullIDSame() {
		//Setup
		final String endpoint = "/test";
		final Integer nodeID1 = 19;
		final Integer nodeID2 = 19;
		ConstraintKey key1 = new ConstraintKey(endpoint, nodeID1);
		ConstraintKey key2 = new ConstraintKey(endpoint, nodeID2);
		
		//Act
		final boolean actual1 = key1.equals(key2);
		final boolean actual2 = key2.equals(key1);
		
		//Assert
		assertTrue(actual1);
		assertTrue(actual2);
	}
	
	@Test
	void EqualOtherIsConstraintKeyEndpointsSameBothNodeIDNotNullIDDifferent() {
		//Setup
		final String endpoint = "/test";
		final Integer nodeID1 = 1;
		final Integer nodeID2 = 3300;
		ConstraintKey key1 = new ConstraintKey(endpoint, nodeID1);
		ConstraintKey key2 = new ConstraintKey(endpoint, nodeID2);
		
		//Act
		final boolean actual1 = key1.equals(key2);
		final boolean actual2 = key2.equals(key1);
				
		//Assert
		assertFalse(actual1);
		assertFalse(actual2);
	}

}
