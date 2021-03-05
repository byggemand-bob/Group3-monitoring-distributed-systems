package com.Group3.monitor.test.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.Group3.monitor.client.MonitorClientInterface;
import com.Group3.monitor.configuration.ConfigurationManager;
import com.Group3.monitor.exception.MonitorConfigException;
import com.Group3.monitor.test.AbstractMonitorTest;


public class MonitorClientInterfaceTest extends AbstractMonitorTest{
	private static String propertyNameID;
	private static String propertyName;
	
	@BeforeAll
	public static void setupTests () {
		propertyNameID = ConfigurationManager.getInstance().IDProp;
		propertyName = ConfigurationManager.getInstance().monitorServerAddressProp;
		AbstractMonitorTest.setupTests();

	}
	
	@Test
	public void testValidateAndSetMonitorIPWithHTTPPrefixPass () {
		// Setup
		long propertyValueID = 1L;
		String propertyValue = "http://123.456.789.101:8080";
		addPropertiesToConfig(propertyNameID + "=" + propertyValueID, propertyName + "=" + propertyValue);
		
		// Act
		
		
		// Assert
		Assertions.assertDoesNotThrow(() -> {
			new MonitorClientInterface();
		});
	}
	@Test
	public void testValidateAndSetMonitorIPWithoutHTTPPrefixPass () {
		// Setup
		long propertyValueID = 1L;
		String propertyValue = "123.456.789.101:8080";
		addPropertiesToConfig(propertyNameID + "=" + propertyValueID, propertyName + "=" + propertyValue);
		
		// Act
		
		
		// Assert
		Assertions.assertDoesNotThrow(() -> {
			new MonitorClientInterface();
		});
	}
	@Test
	public void testValidateAndSetMonitorIPWithoutHTTPPrefixMissingPortFail () {
		// Setup
		long propertyValueID = 1L;
		String propertyValue = "123.456.789.101:";
		addPropertiesToConfig(propertyNameID + "=" + propertyValueID, propertyName + "=" + propertyValue);
		
		// Act
		
		
		// Assert
		
		Assertions.assertThrows(MonitorConfigException.class, () -> {
			new MonitorClientInterface();
		});
	}
	@Test
	public void testValidateAndSetMonitorIPWithoutHTTPPrefixWithoutPortFail () {
		// Setup
		long propertyValueID = 1L;
		String propertyValue = "123.456.789.101";
		addPropertiesToConfig(propertyNameID + "=" + propertyValueID, propertyName + "=" + propertyValue);
		
		// Act
		
		
		// Assert
		Assertions.assertThrows(MonitorConfigException.class, () -> {
			new MonitorClientInterface();
		});
	}@Test
	public void testValidateAndSetMonitorIPWithHTTPPrefixWithoutPortFail () {
		// Setup
		long propertyValueID = 1L;
		String propertyValue = "http://123.456.789.101";
		addPropertiesToConfig(propertyNameID + "=" + propertyValueID, propertyName + "=" + propertyValue);
		
		// Act
		
		
		// Assert
		Assertions.assertThrows(MonitorConfigException.class, () -> {
			new MonitorClientInterface();
		});
	}

}
