package com.group3.monitorServer.constraint.validator;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import org.everit.json.schema.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConstraintUserInputValidationTest {
	
	private final String data = "..\\Monitor\\src\\test\\java\\com\\group3\\monitorServer\\constraint\\validator\\testFiles\\";
	ConstraintUserInputValidation inputValidation = new ConstraintUserInputValidation();
	
	@Test
	void validateCorrectFile() throws FileNotFoundException {
		//setup
		final String fileName = "CorrectData.json";
		//act
		inputValidation.validate(data + fileName);
		
	}
	
	@Test
	void validateIncorrectFile() throws FileNotFoundException {
		//setup
		final String fileName = "InCorrectData.json";
		//assert
		Assertions.assertThrows(ValidationException.class, () -> {
			inputValidation.validate(data + fileName);
		});
	}
	
	@Test
	void ValidateEmptyFile() throws FileNotFoundException {
		//setup
		final String fileName = "EmptyArrayData.json";
		//act
		inputValidation.validate(data + fileName);
	}
	
	@Test
	void ValidateCorrectFileWithOptionalValue() throws FileNotFoundException {
		//setup
		final String fileName = "CorrectDataWithOptionalValue.json";
		//act
		inputValidation.validate(data + fileName);
	}
	
	@Test
	void ValidateInCorrectFileWithOptionalValue() throws FileNotFoundException {
		//setup
		final String fileName = "InCorrectDataWithOptionalValue.json";
		//assert
		Assertions.assertThrows(ValidationException.class, () -> {
			inputValidation.validate(data + fileName);
		});
	}
	
	@Test
	void ValidateCantFindFile() throws FileNotFoundException {
		//setup
		final String fileName = "CantFindThisFile.json";
		//assert
		Assertions.assertThrows(FileNotFoundException.class, () -> {
			inputValidation.validate(data + fileName);
		});
	}
}
