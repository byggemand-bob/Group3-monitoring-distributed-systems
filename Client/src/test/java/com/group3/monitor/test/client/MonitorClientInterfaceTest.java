package com.group3.monitor.test.client;

import com.group3.monitor.test.AbstractMonitorTest;
import com.group3.monitorClient.MonitorClientInterface;
import com.group3.monitorClient.configuration.ConfigurationManager;
import com.group3.monitorClient.exception.MonitorConfigException;
import com.group3.monitorClient.messenger.messages.SQLManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;


public class MonitorClientInterfaceTest extends AbstractMonitorTest{
	private static String propertyNameID;
	private static String propertyName;

	@BeforeAll
	public static void setupTests () {
		File path = new File("src/main/resources/sqlite/db/");
		path.mkdirs();
		SQLManager sqlManager = SQLManager.getInstance();
		sqlManager.Connect("src/main/resources/sqlite/db","test.db");
		propertyNameID = ConfigurationManager.getInstance().IDProp;
		propertyName = ConfigurationManager.getInstance().monitorServerAddressProp;
		AbstractMonitorTest.setupTests();
	}

	@AfterAll
	public static void tearDown () {
		SQLManager sqlManager = SQLManager.getInstance();

		File db = new File(sqlManager.getPath() + sqlManager.getFileName());

		sqlManager.CloseConnection();

		if (!db.delete()) {
			System.out.println(MonitorClientInterfaceTest.class.toString() + ": Could not access database file. It was therefore not deleted!");
		}
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
