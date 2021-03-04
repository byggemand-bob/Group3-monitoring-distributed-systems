package com.Group3.monitor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import com.Group3.monitor.configuration.ConfigurationManager;

public abstract class AbstractMonitorTest {

	protected static String configPath;
	protected static List<String> savedProperties = new ArrayList<String>();
	
	@BeforeAll
	public static void setupTests() {
		configPath = ConfigurationManager.getInstance().getPropertiesPath();
		savedProperties = saveConfigProperties(configPath);
	}
	
	@BeforeEach
	public void beforeEachTest() {
		resetConfigurationManagerSingleton();
	}
	
	@AfterAll
	public static void destroyTestEnvironment() {
		cleanConfigProperties(configPath, savedProperties);
	}
	
	// ################### HELPER METHODS ###################
	protected static void addPropertiesToConfig(String... properties) {
		try (FileWriter writer = new FileWriter(new File(configPath))){
			for (String property : properties) {
				writer.write(property + "\r\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected static void cleanConfigProperties(String path, List<String> savedProperties) {
		addPropertiesToConfig(savedProperties.toArray(new String[0]));
	}

	protected static List<String> saveConfigProperties(String path) {
		List<String> propertiesList = new ArrayList<String>();
		try (Scanner input = new Scanner(new File(path))) {
			while (input.hasNextLine()) {
				propertiesList.add(input.nextLine());
			}
		} catch (FileNotFoundException e) {
			// Do something
		}

		return propertiesList;
	}
	
	protected static void destroySingleton(Class singletonClazz, Object instans, String field, Object newValue) {
		java.lang.reflect.Field f = null;
		try {
			f = singletonClazz.getDeclaredField(field);
		} catch (NoSuchFieldException | SecurityException e1) {
			e1.printStackTrace();
		}
		
		if (f != null) {
			f.setAccessible(true);			
		}
		
		try {
			f.set(instans, newValue);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		f.setAccessible(false);
	}
	
	protected static void resetConfigurationManagerSingleton() {
		destroySingleton(ConfigurationManager.class, null, "instance", null);
	}
}
