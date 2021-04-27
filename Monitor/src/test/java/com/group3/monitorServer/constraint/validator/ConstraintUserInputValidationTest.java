package com.group3.monitorServer.constraint.validator;

import java.io.File;
import java.io.FileNotFoundException;

import org.everit.json.schema.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConstraintUserInputValidationTest {
	
	private final String path = ".."+ File.separator + "Monitor"+ File.separator +"src"+ File.separator +"main"+ File.separator +"resources"+ File.separator +"validation"+ File.separator +"testFiles"+ File.separator +"";
	ConstraintUserInputValidation inputValidation = new ConstraintUserInputValidation();
	
	@Test
	void validateCorrectFile() throws FileNotFoundException {
		//setup
		final String file = path + "CorrectData.json";
		//act
		inputValidation.validate(file);
	}
	
	@Test
	void validateIncorrectFile() {
		//setup
		final String file = path + "InCorrectData.json";
		//assert
		Assertions.assertThrows(ValidationException.class, () -> {
			inputValidation.validate(file);
		});
	}
	
	@Test
	void ValidateEmptyFile() throws FileNotFoundException {
		//setup
		final String file = path + "EmptyArrayData.json";
		//act
		inputValidation.validate(file);
	}
	
	@Test
	void ValidateCorrectFileWithOptionalValue() throws FileNotFoundException {
		//setup
		final String file = path + "CorrectDataWithOptionalValue.json";
		//act
		inputValidation.validate(file);
	}
	
	@Test
	void ValidateInCorrectFileWithOptionalValue() {
		//setup
		final String file = path + "InCorrectDataWithOptionalValue.json";
		//assert
		Assertions.assertThrows(ValidationException.class, () -> {
			inputValidation.validate(file);
		});
	}
	
	@Test
	void ValidateCantFindFile() {
		//setup
		final String file = path + "CantFindThisFile.json";
		//assert
		Assertions.assertThrows(FileNotFoundException.class, () -> {
			inputValidation.validate(file);
		});
	}
	
	@Test
	void ValidateEndpointUnderMinLength() {
		//setup
		final String file = path + "EndpointUnderMinLength.json";
		Assertions.assertThrows(ValidationException.class, () -> {
			inputValidation.validate(file);
		});
	}
}
