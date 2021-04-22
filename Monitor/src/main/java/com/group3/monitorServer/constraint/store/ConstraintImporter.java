package com.group3.monitorServer.constraint.store;

import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.group3.monitorServer.constraint.Constraint;
import com.group3.monitorServer.constraint.validator.ConstraintUserInputValidation;

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
		
		ConstraintUserInputValidation inputValidation = new ConstraintUserInputValidation();
		inputValidation.validate(path);

		//the file is correct -- read all data from file and put into a array of strings
		String[] constraints  = readData(path);

		//convert String[] to ConstraintStore
		//ConstraintStore constraintStore = convertData(constraints);
		
		//add each constraint to the constraintStore and returns it
		//return constraintStore;
		return null;
	}
	
	/**
	 * Get the path as specified in the configuration.properties file to the specification of the {@link Constraint}s.
	 * 
	 * @return The path to the {@link Constraint} specification as defined in the configuration.properties file.
	 */
	public String getDefaultConstraintPath() {
		//get the path in the configuration.properties (needs to be created)
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
		//read json and convert them to a string using private method convertData(String[] data)
		
		Constraint[] constraints = null;	
		String data = "";
		
		Gson gson = new Gson();


		

		try {

			JsonReader reader2 = new JsonReader(new FileReader(path));

			data = JsonParser.parseReader(reader2).toString();

			constraints = gson.fromJson(data, Constraint[].class);

			for (int i = 0; i< constraints.length; i++) {
				System.out.println(constraints[i]);
			}

		}
		catch (IOException e) {
			//TODO return a custom error?
		}



		
		//convert each constrint into a string[] and return it
		


		//TODO check if it is null before returning
		return null;
		
		//open file and read it
		//spilt the constraint into each string
		//store them in a array[string]
	}
	
	/**
	 * Converts the {@link Constraint} data encoded in the String array into instance of the {@link Constraint} class and returns a {@link ConstraintStore} containing all the {@link Constraint}s.
	 * 
	 * @param data String array of the String encoding of the specified {@link Constraint}s
	 * 
	 * @return A {@link ConstraintStore} containing information about the {@link Constraint}s encoded in the data.
	 */
	private ConstraintStore convertData(String[] data) {
		//convert all strings into a ConstraintStore
	
		ConstraintStore constraintStore = new ConstraintStore();
		Constraint constraint;

		//foreach string in string array
		constraint = convertData(data[0]);
		constraintStore.addConstraint(constraint);

		//once done return the store
		return constraintStore;
	}
	
	/**
	 * Converts a single encoding of a {@link Constraint} into its class representation.
	 * 
	 * @param data The {@link Constraint} encoded as a String.
	 * 
	 * @return A {@link Constraint} instance based on the provided data.
	 */
	private Constraint convertData(String data) {

		String endpoint = "";
		Integer max = 0;

		Constraint constraint = new Constraint(endpoint, max);
		return constraint;
		//TODO Make sure min value cant be greater then max value.
		
		//convert a single line in the string array into a Constraint
	}
}
