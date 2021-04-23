package com.group3.monitorServer.constraint.store;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.group3.monitorServer.constraint.Constraint;
import com.group3.monitorServer.constraint.ConstraintKey;

public class ConstraintImporterTest {
	
	private final String path = ".."+ File.separator + "Monitor"+ File.separator +"src"+ File.separator +"test"+ File.separator +"java"+ File.separator +"com"+ File.separator +"group3"+ File.separator +"monitorServer"+ File.separator +"constraint"+ File.separator +"validator"+ File.separator +"testFiles"+ File.separator +"";
	ConstraintImporter importer = new ConstraintImporter();
	ConstraintStore constraintStore = new ConstraintStore();

	@BeforeEach
	void Initialize() {
		constraintStore = new ConstraintStore();
	}
	
	@Test
	void importConstraintsWithMaxGreaterThanMin() throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		//setup
		final String file = path + "MaxGreaterThanMin.json";
		final int expected = 1;
		int SizeOfConstraintStore = 0;
		//act
		constraintStore = importer.importConstraints(file);
		SizeOfConstraintStore = GetConstraintHashMap().size();
		//assert
		assertEquals(expected, SizeOfConstraintStore);
	}
	
	@Test
	void importConstraintsWithMaxLessThanMin() throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		//setup
		final String file = path + "MaxLessThanMin.json";
		final int expected = 0;
		int SizeOfConstraintStore = 0;
		//act
		constraintStore = importer.importConstraints(file);
		SizeOfConstraintStore = GetConstraintHashMap().size();
		//assert
		assertEquals(expected, SizeOfConstraintStore);
	}
	
	@Test
	void importConstraintsWithMaxSameAsMin() throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		//setup
		final String file = path + "MaxSameAsMin.json";
		final int expected = 1;
		int SizeOfConstraintStore = 0;
		//act
		constraintStore = importer.importConstraints(file);
		SizeOfConstraintStore = GetConstraintHashMap().size();
		//assert
		assertEquals(expected, SizeOfConstraintStore);
	}
	
	// Utility methods
	@SuppressWarnings({"unchecked"})
	private HashMap<ConstraintKey, Constraint> GetConstraintHashMap() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Map<ConstraintKey, Constraint> constraints;
		java.lang.reflect.Field field = constraintStore.getClass().getDeclaredField("constraints");
		field.setAccessible(true);
		constraints = (HashMap<ConstraintKey, Constraint>)field.get(constraintStore);
		field.setAccessible(false);
		return (HashMap<ConstraintKey, Constraint>) constraints;
	}
}
