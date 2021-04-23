package com.group3.monitorServer.constraint.store;

import com.group3.monitorServer.constraint.Constraint;
import org.apache.commons.lang3.NotImplementedException;

/**
 * Responsible for importing the {@link Constraint}s specified in a file into the Monitor Server to make them available to use in the system when analyzing the data.
 * 
 * @see ConstraintStore
 * @see com.group3.monitorServer.constraint.analyze.ConstraintAnalyzer
 * 
 */
public class ConstraintImporter {
	
	/**
	 * Imports the {@link Constraint}s specified in the given OS path.
	 * 
	 * @param path The path the the file specifying the {@link Constraint}s for the Monitor Server.
	 * 
	 * @return A {@link ConstraintStore} containing all the {@link Constraint}s specified in the file.
	 */
	public ConstraintStore importConstraints(String path) {
		throw new NotImplementedException();
	}
	
	/**
	 * Get the path as specified in the configuration.properties file to the specification of the {@link Constraint}s.
	 * 
	 * @return The path to the {@link Constraint} specification as defined in the configuration.properties file.
	 */
	public String getDefaultConstraintPath() {
		throw new NotImplementedException();
	}
	
	/**
	 * Read all the data from the {@link Constraint} specification file at the given path.
	 * 
	 * @param path The path to the {@link Constraint} specification file for import.
	 * 
	 * @return An array containing each {@link Constraint} encoded as a String.
	 */
	private String[] readData(String path) {
		throw new NotImplementedException();
	}
	
	/**
	 * Converts the {@link Constraint} data encoded in the String array into instance of the {@link Constraint} class and returns a {@link ConstraintStore} containing all the {@link Constraint}s.
	 * 
	 * @param data String array of the String encoding of the specified {@link Constraint}s
	 * 
	 * @return A {@link ConstraintStore} containing information about the {@link Constraint}s encoded in the data.
	 */
	private ConstraintStore convertData(String[] data) {
		throw new NotImplementedException();
	}
	
	/**
	 * Converts a single encoding of a {@link Constraint} into its class representation.
	 * 
	 * @param data The {@link Constraint} encoded as a String.
	 * 
	 * @return A {@link Constraint} instance based on the provided data.
	 */
	private Constraint convertData(String data) {
		//TODO Make sure min value cant be greater then max value.
		throw new NotImplementedException();
	}
}
