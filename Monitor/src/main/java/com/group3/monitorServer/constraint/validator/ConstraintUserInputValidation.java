package com.group3.monitorServer.constraint.validator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.group3.monitorServer.constraint.Constraint;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Responsible for checking the structure of the JSON specification file containing the {@link Constraint}
 */
public class ConstraintUserInputValidation {

	private final String schemaJSON = ".."+ File.separator +"Monitor"+ File.separator +"src"+ File.separator +"main"+ File.separator +"resources"+ File.separator +"validation"+ File.separator +"constraintSchema.json";
	
	/**
	 * Validate the JSON specification file against the schema
	 * Returns an exception if there was a error validating the JSON specification file
	 * 
	 * @param jsonFilePath the file path for the JSON specification file
	 * @throws FileNotFoundException Throws a exception if the JSON files could not be found
	 */
	public void validate(String jsonFilePath) throws FileNotFoundException {

		InputStream schemaInputStream = new FileInputStream(schemaJSON);
		InputStream dataInputStream = new FileInputStream(jsonFilePath);

		System.out.println("Started to validate JSON file");
	    JSONObject jsonSchema = new JSONObject(new JSONTokener(schemaInputStream));
		JSONArray jsonSubject = new JSONArray(new JSONTokener(dataInputStream));

		SchemaLoader loader = SchemaLoader
			.builder()
				.schemaJson(jsonSchema)
				.draftV7Support()
				.build();

	    Schema schema = loader.load().build();
		schema.validate(jsonSubject);
		System.out.println("The JSON file passed!");
	}
}
 