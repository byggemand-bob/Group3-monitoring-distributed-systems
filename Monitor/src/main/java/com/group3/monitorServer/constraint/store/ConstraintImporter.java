package com.group3.monitorServer.constraint.store;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.validation.ValidationException;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.group3.monitorClient.configuration.ConfigurationManager;
import com.group3.monitorServer.constraint.Constraint;
import com.group3.monitorServer.constraint.validator.ConstraintUserInputValidation;


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
	 * @throws IOException is thrown if there was a I/O operations that failed
	 */
	public ConstraintStore importConstraints(String path) throws IOException {
		ConstraintUserInputValidation inputValidation = new ConstraintUserInputValidation();
		inputValidation.validate(path);
		Constraint[] constraints  = readData(path);
		ConstraintStore constraintStore = convertData(constraints);;
		return constraintStore;
	}
	
	/**
	 * Get the path as specified in the configuration.properties file to the specification of the {@link Constraint}s.
	 * 
	 * @return The path to the {@link Constraint} specification as defined in the configuration.properties file.
	 */
	public String getDefaultConstraintPath() {
		String constraintsSpecificationFilePath = null;
		
		constraintsSpecificationFilePath = ConfigurationManager
				.getInstance()
				.getProperty(ConfigurationManager.constraintsSpecificationFilePath);
		
		return constraintsSpecificationFilePath;
	}
	
	/**
	 * Read all the data from the {@link Constraint} specification file at the given path.
	 * 
	 * @param path The path to the {@link Constraint} specification file for import.
	 * 
	 * @return An array containing each {@link Constraint}.
	 * @throws FileNotFoundException Throws an exception if the file can't be found using @param path.
	 */
	private Constraint[] readData(String path) throws FileNotFoundException {
		Constraint[] constraints = null;	
		String data = "";
		Gson gson = new Gson();
		JsonReader jsonReader = new JsonReader(new FileReader(path));
		data = JsonParser.parseReader(jsonReader).toString();
		constraints = gson.fromJson(data, Constraint[].class);

		return constraints;
	}
	
	/**
	 * Converts the {@link Constraint} data encoded in the {@link Constraint} into instance of the {@link Constraint} class and returns a {@link ConstraintStore} containing all the {@link Constraint}s.
	 * 
	 * @param data is a array of {@link Constraint}
	 * 
	 * @return A {@link ConstraintStore} containing information about the {@link Constraint}s encoded in the data.
	 */
	private ConstraintStore convertData(Constraint[] constraints) {	
		ConstraintStore constraintStore = new ConstraintStore();
		for (int i = 0; i < constraints.length; i++) {
			Constraint constraint = constraints[i];

			if(constraint.getMin() != null && constraint.getMax() < constraint.getMin()) {
				throw new ValidationException("The constraint max value is less than min value, which is not allowed");
			}
			
			constraintStore.addConstraint(constraint);
		}

		return constraintStore;
	}
}
