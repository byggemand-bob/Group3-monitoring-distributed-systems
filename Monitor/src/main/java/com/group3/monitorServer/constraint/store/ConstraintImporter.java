package com.group3.monitorServer.constraint.store;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
		
		String constraintsSpecificationFilePath = "";
		
		try {
			constraintsSpecificationFilePath = ConfigurationManager.getInstance().getProperty(ConfigurationManager.constraintsSpecificationFilePath);
		} catch (Exception e){}
		
		return constraintsSpecificationFilePath;
	}
	
	/**
	 * Read all the data from the {@link Constraint} specification file at the given path.
	 * 
	 * @param path The path to the {@link Constraint} specification file for import.
	 * 
	 * @return An array containing each {@link Constraint}.
	 * @throws FileNotFoundException Throws an exception if the file can't be found using @param Path
	 */
	private Constraint[] readData(String path) throws FileNotFoundException {
		Constraint[] constraints = null;	
		String data = "";
		Gson gson = new Gson();
		JsonReader reader2 = new JsonReader(new FileReader(path));
		data = JsonParser.parseReader(reader2).toString();
		constraints = gson.fromJson(data, Constraint[].class);

		return constraints;
	}
	
	/**
	 * Converts the {@link Constraint} data encoded in the String array into instance of the {@link Constraint} class and returns a {@link ConstraintStore} containing all the {@link Constraint}s.
	 * 
	 * @param data String array of the String encoding of the specified {@link Constraint}s
	 * 
	 * @return A {@link ConstraintStore} containing information about the {@link Constraint}s encoded in the data.
	 */
	private ConstraintStore convertData(Constraint[] constraints) {	
		ConstraintStore constraintStore = new ConstraintStore();
		for (int i = 0; i < constraints.length; i++) {
			Constraint constraint = constraints[i];

			if(isMaxGreaterThanMin(constraint.getMin(), constraint.getMax())) {
				constraintStore.addConstraint(constraint);
			}
		}

		return constraintStore;
	}
	
	/**
	 * Method to check if the {@link Constraint} min value is less then the max value.
	 * @param min is the min value of the {@link Constraint}.
	 * @param max is the max value of the {@link Constraint}.
	 * @return return true if min value is less than max value, if that is not true it will return false.
	 */
	private boolean isMaxGreaterThanMin(Integer min, Integer max) {
		if (min == null) {return true;} //if there is not set a min value return true
		if (max < min) { return false;} //if max is less than min return false
		
		return true; //max is grater than min return true
	}
}
