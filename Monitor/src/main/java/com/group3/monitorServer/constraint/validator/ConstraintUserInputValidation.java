package com.group3.monitorServer.constraint.validator;

import org.everit.json.schema.ArraySchema;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class ConstraintUserInputValidation {
	
	public void validate(String jsonFilePath) {


		//TODO change to use the method parameter path needs to be converted to stream
		System.out.println("Started to validate JSON file");
	    JSONObject jsonSchema = new JSONObject(new JSONTokener(ConstraintUserInputValidation.class.getResourceAsStream("/constraintSchema.json")));
		JSONArray jsonSubject = new JSONArray(new JSONTokener(ConstraintUserInputValidation.class.getResourceAsStream("/testData.json")));

		SchemaLoader loader = SchemaLoader.builder()
			.schemaJson(jsonSchema).draftV7Support().build();

		
		//ArraySchema arraySchema = SchemaLoader.builder();


	    Schema schema = loader.load().build();
		schema.validate(jsonSubject);

		//ArraySchema arraySchema = (ArraySchema) schema;

	    //arraySchema.validate(jsonSubject);
		System.out.println("The JSON file passed!");
	}
}
 