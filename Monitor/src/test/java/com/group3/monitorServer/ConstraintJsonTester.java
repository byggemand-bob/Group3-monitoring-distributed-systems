package com.group3.monitorServer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.group3.monitorServer.constraint.store.ConstraintImporter;

import org.apache.tomcat.util.json.ParseException;

public class ConstraintJsonTester {

	//TODO delete this, only used for testing
	
	public static void main(String[] args) throws JsonProcessingException, IOException, ParseException {
		ConstraintImporter importer = new ConstraintImporter();
		importer.importConstraints("C:\\Users\\Stensgaard\\git\\Group3-monitoring-distributed-systems\\Monitor\\src\\main\\java\\testData.json");
	}
}
