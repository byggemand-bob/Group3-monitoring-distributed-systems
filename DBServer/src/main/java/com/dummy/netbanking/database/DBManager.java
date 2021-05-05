package com.dummy.netbanking.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class DBManager {

	private static Connection connection;
	private static final String dbPath = "db" + File.separator;
	private static final String dbName = "bank.db";
	private static final String driver = "jdbc:sqlite:";
	private boolean tableCreated = false;
	
	private void connect(String path) {
		try {
			connection = DriverManager.getConnection(driver + path);
		} catch (SQLException e) {
			System.err.println("Failed to create a connection to DB: <" + path + ">, with driver <" + driver + "> because of error <" + e.getMessage() + ">");
		}
	}
	
	protected DBManager() {
		if (connection == null) {
			final String path = dbPath + dbName;
			createDirectoryIfNotExist(dbPath);
			
			connect(path);			
		}
		if (!tableCreated) {
			createTableIfNotExist();
			tableCreated = true;
		}
	}
	
	protected abstract void createTableIfNotExist();
	
	private void createDirectoryIfNotExist(String directory) {
		File dir = new File(directory);
		if (!dir.exists()) dir.mkdir();
	}
	
	protected PreparedStatement createPreparedStatement(final String sql) {
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(sql);
		} catch (SQLException e) {
			System.err.println("Failed to create prepared statement for query <" + sql + "> with error message <" + e.getMessage() + ">");
		}
		return statement;
	}
	
	protected synchronized int executeUpdatePreparedStatement(PreparedStatement statement) {
		int updatedRows = -1;
		try {
			updatedRows = statement.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Failed to execute update for prepared statement <" + statement + "> with error message <" + e.getMessage() + ">");
		}
		
		return updatedRows;
	}
	
	protected synchronized ResultSet executePreparedStatement(PreparedStatement statement) {
		ResultSet queryResult = null;
		try {
			queryResult = statement.executeQuery();
		} catch (SQLException e) {
			System.err.println("Failed to execute prepared statement <" + statement + "> with error message <" + e.getMessage() + ">");
		}
		return queryResult;
	}
}