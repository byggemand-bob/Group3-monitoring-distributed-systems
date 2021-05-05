package com.dummy.netbanking.database;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.openapitools.model.User;

public class UserDBManager extends DBManager {	
	// DB Columns
	public static final String col_name = "NAME";
	public static final String col_password = "PASSWORD";	
	public static final String col_primary = col_name;	
	
	// Queries
	private static final String insert = "INSERT INTO User (" + col_name + ", " + col_password + ") VALUES (?,?)";
	private static final String delete = "DELETE FROM User WHERE " + col_name + " = ?";
	private static final String select = "SELECT * FROM User WHERE " + col_name + " = ?";
	private static final String algorithm = "SHA-256";

	@Override
	protected void createTableIfNotExist() {
		final String sql = "CREATE TABLE IF NOT EXISTS User(" + 
				col_name +" TEXT PRIMARY KEY, " + 
				col_password + " TEXT NOT NULL" +
				")";
		PreparedStatement statement = createPreparedStatement(sql);
		executeUpdatePreparedStatement(statement);
	}
	
	public void insertUser(User user) throws SQLException {
		final String hashedPassword = hashPassword(user.getPassword());
		
		PreparedStatement insertStatement = createPreparedStatement(insert);
		insertStatement.setString(1, user.getName());
		insertStatement.setString(2, hashedPassword);
		executeUpdatePreparedStatement(insertStatement);
	}
	
	public User selectUser(final String userName) throws SQLException {
		PreparedStatement selectStatement = createPreparedStatement(select);
		selectStatement.setString(1, userName);
		
		ResultSet result = executePreparedStatement(selectStatement);
		if (!result.next()) {
			return null;			
		}
		
		User user = new User();
		user.setName(result.getString(col_name));
		user.setPassword(result.getString(col_password));
		
		return user;
	}
	
	public boolean deleteUser(final String userName) throws SQLException {
		PreparedStatement deleteStatement = createPreparedStatement(delete);
		deleteStatement.setString(1, userName);
		
		final int deletedRows = executeUpdatePreparedStatement(deleteStatement);
		
		return (deletedRows > 0);
	}
	
	private String hashPassword(final String password) {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			// Intentially left blank...
		}
		
		byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
		
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < hash.length; i++) {
			sb.append(hash[i]);
		}
		
		return sb.toString();
	}
}
