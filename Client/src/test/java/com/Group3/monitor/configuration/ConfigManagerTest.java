package com.Group3.monitor.configuration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.Group3.monitor.AbstractMonitorTest;

public class ConfigManagerTest extends AbstractMonitorTest {


	@Test
	public void testGetPropertyAsLongWithLongValuePositivePass() {
		// Setup
		String propertyName = "test";
		long propertyValue = 3L;
		addPropertiesToConfig(propertyName + "=" + propertyValue);

		// Act
		long actual = ConfigurationManager.getInstance().getPropertyAsLong(propertyName);

		// Assert
		Assertions.assertEquals(propertyValue, actual);
	}

	@Test
	public void testGetPropertyAsLongWithLongValueNegativePass() {
		// Setup
		String propertyName = "test";
		long propertyValue = -7L;
		addPropertiesToConfig(propertyName + "=" + propertyValue);

		// Act
		long actual = ConfigurationManager.getInstance().getPropertyAsLong(propertyName);

		// Assert
		Assertions.assertEquals(propertyValue, actual);
	}	
	
	@Test
	public void testGetPropertyAsLongWithLongValueZeroPass() {
		// Setup
		String propertyName = "test";
		long propertyValue = 0L;
		addPropertiesToConfig(propertyName + "=" + propertyValue);

		// Act
		long actual = ConfigurationManager.getInstance().getPropertyAsLong(propertyName);

		// Assert
		Assertions.assertEquals(propertyValue, actual);
	}
	
	@Test
	public void testGetPropertyAsLongWithStringValueFail() {
		// Setup
		String propertyName = "test";
		String propertyValue = "ThisIsDefinitelyNotANumber";
		addPropertiesToConfig(propertyName + "=" + propertyValue);

		// Act
		// Assert
		Assertions.assertThrows(NumberFormatException.class, () -> {
			ConfigurationManager.getInstance().getPropertyAsLong(propertyName);
		});
	}
	
	@Test
	public void testGetPropertyAsLongWithFloatValueFail() {
		// Setup
		String propertyName = "test";
		float propertyValue = 7.3f;
		addPropertiesToConfig(propertyName + "=" + propertyValue);

		// Act
		// Assert
		Assertions.assertThrows(NumberFormatException.class, () -> {
			ConfigurationManager.getInstance().getPropertyAsLong(propertyName);
		});
	}

	@Test
	public void testGetPropertyAsLongWithFloatWithoutFractionValueFail() {
		// Setup
		String propertyName = "test";
		float propertyValue = 7.0f;
		addPropertiesToConfig(propertyName + "=" + propertyValue);

		// Act
		// Assert
		Assertions.assertThrows(NumberFormatException.class, () -> {
			ConfigurationManager.getInstance().getPropertyAsLong(propertyName);
		});
	}	
	
	
	@Test
	public void testGetPropertyAsLongWithDoubleValueFail() {
		// Setup
		String propertyName = "test";
		double propertyValue = 17.033;
		addPropertiesToConfig(propertyName + "=" + propertyValue);

		// Act
		// Assert
		Assertions.assertThrows(NumberFormatException.class, () -> {
			ConfigurationManager.getInstance().getPropertyAsLong(propertyName);
		});
	}

	@Test
	public void testGetPropertyAsLongWithDoubleWithoutFractionValueFail() {
		// Setup
		String propertyName = "test";
		double propertyValue = 17.0;
		addPropertiesToConfig(propertyName + "=" + propertyValue);

		// Act
		// Assert
		Assertions.assertThrows(NumberFormatException.class, () -> {
			ConfigurationManager.getInstance().getPropertyAsLong(propertyName);
		});
	}
	
	@Test
	public void testGetPropertyAsLongWithIntValuePass() {
		// Setup
		String propertyName = "test";
		int propertyValue = 1337;
		addPropertiesToConfig(propertyName + "=" + propertyValue);

		// Act
		long actual = ConfigurationManager.getInstance().getPropertyAsLong(propertyName);

		// Assert
		Assertions.assertEquals(propertyValue, actual);
	}
	
	@Test
	public void testGetPropertyWithExistingPropertyWithValuePass() {
		// Setup
		String propertyName = "testExist";
		String propertyValue = "ValueExist";
		addPropertiesToConfig(propertyName + "=" + propertyValue);
		
		// Act
		String actual = ConfigurationManager.getInstance().getProperty(propertyName);
		
		// Assert
		Assertions.assertEquals(propertyValue, actual);
	}
	
	@Test
	public void testGetPropertyWithExistingPropertyWithoutValuePass() {
		// Setup
		String propertyName = "testExist";
		
		addPropertiesToConfig(propertyName + "=");
		
		// Act
		String actual = ConfigurationManager.getInstance().getProperty(propertyName);
		
		// Assert
		Assertions.assertNull(actual);
	}
	
	@Test
	public void testGetPropertyWithExistingPropertyWithoutValueWithDefaultValueNullPass() {
		// Setup
		String propertyName = "testExist";
		String defaultValue = null;
		
		addPropertiesToConfig(propertyName + "=");
		
		// Act
		String actual = ConfigurationManager.getInstance().getProperty(propertyName, defaultValue);
		
		// Assert
		Assertions.assertNull(actual);
	}
	
	@Test
	public void testGetPropertyWithExistingPropertyWithoutValueWithDefaultValueEmptyStringPass() {
		// Setup
		String propertyName = "testExist";
		String defaultValue = "";
		
		addPropertiesToConfig(propertyName + "=");
		
		// Act
		String actual = ConfigurationManager.getInstance().getProperty(propertyName, defaultValue);
		
		// Assert
		Assertions.assertEquals(defaultValue, actual);
	}
	
	@Test
	public void testGetPropertyWithExistingPropertyWithoutValueWithDefaultValueNotNullOrEmptyStringPass() {
		// Setup
		String propertyName = "testExist";
		String defaultValue = "the Default";
		
		addPropertiesToConfig(propertyName + "=");
		
		// Act
		String actual = ConfigurationManager.getInstance().getProperty(propertyName, defaultValue);
		
		// Assert
		Assertions.assertEquals(defaultValue, actual);
	}
	
	@Test
	public void testGetPropertyWithExistingPropertyWithValueWithDefaultValueNullPass() {
		// Setup
		String propertyName = "testExist";
		String propertyValue = "This is Val";
		String defaultValue = null;
		
		addPropertiesToConfig(propertyName + "=" + propertyValue);
		
		// Act
		String actual = ConfigurationManager.getInstance().getProperty(propertyName, defaultValue);
		
		// Assert
		Assertions.assertNotNull(actual);
		Assertions.assertEquals(propertyValue, actual);
	}
	
	@Test
	public void testGetPropertyWithExistingPropertyWithValueWithDefaultValueEmptyStringPass() {
		// Setup
		String propertyName = "testExist";
		String propertyValue = "This is Val";
		String defaultValue = "";
		
		addPropertiesToConfig(propertyName + "=" + propertyValue);
		
		// Act
		String actual = ConfigurationManager.getInstance().getProperty(propertyName, defaultValue);
		
		// Assert
		Assertions.assertEquals(propertyValue, actual);
	}
	
	@Test
	public void testGetPropertyWithExistingPropertyWithValueWithDefaultValueNotNullOrEmptyStringPass() {
		// Setup
		String propertyName = "testExist";
		String propertyValue = "This is Val";
		String defaultValue = "the Default";
		
		addPropertiesToConfig(propertyName + "=" + propertyValue);
		
		// Act
		String actual = ConfigurationManager.getInstance().getProperty(propertyName, defaultValue);
		
		// Assert
		Assertions.assertEquals(propertyValue, actual);
	}
	
	@Test
	public void testGetPropertyWithoutExistingPropertyWithoutDefaultValuePass() {
		// Setup
		String propertyName = "testExist";
		
		// Act
		String actual = ConfigurationManager.getInstance().getProperty(propertyName);
		
		// Assert
		Assertions.assertNull(actual);
	}
	
	@Test
	public void testGetPropertyWithoutExistingPropertyWithDefaultValuePass() {
		// Setup
		String propertyName = "testExist";
		String defaultValue = "the Default";
		
		// Act
		String actual = ConfigurationManager.getInstance().getProperty(propertyName, defaultValue);
		
		// Assert
		Assertions.assertNotNull(actual);
		Assertions.assertEquals(defaultValue, actual);
	}
}
