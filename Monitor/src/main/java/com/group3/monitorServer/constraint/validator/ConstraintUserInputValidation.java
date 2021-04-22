package com.group3.monitorServer.constraint.validator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class ConstraintUserInputValidation {

	private final String schemaJSON = "..\\Monitor\\src\\main\\resources\\cstTemplates\\validation\\constraintSchema.json";
	
	public void validate(String jsonFilePath) {

		InputStream schemaInputStream = null;
		//TODO fix this 
		InputStream dataInputStream = null;

		System.out.println(jsonFilePath);

		try {
			schemaInputStream = new FileInputStream(schemaJSON);
			dataInputStream = new FileInputStream(jsonFilePath);
		}
		catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		}
		
		System.out.println("Started to validate JSON file");
	    JSONObject jsonSchema = new JSONObject(schemaInputStream);
		JSONArray jsonSubject = new JSONArray(new JSONTokener(ConstraintUserInputValidation.class.getResourceAsStream("/testData.json")));

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
 