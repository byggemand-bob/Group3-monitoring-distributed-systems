package com.dummy.netbanking.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.openapitools.model.Account;

public class AccountDBManager extends DBManager {
	
	// DB Columns
	public static final String col_name = "NAME";
	public static final String col_balance = "BALANCE";
	public static final String col_owner = "OWNER";
	public static final String col_primary = col_name;

	// Queries
	private static final String insert = "INSERT INTO Account (" + col_name + ", " + col_balance + ", " + col_owner + ") VALUES (?,?,?)";
	private static final String delete = "DELETE FROM Account WHERE " + col_name + " = ?";
	private static final String select = "SELECT * FROM Account WHERE " + col_owner + " = ?";
	private static final String selectBalance = "SELECT " + col_balance + " FROM Account WHERE " + col_name + " = ?";
	private static final String updateBalance = "UPDATE Account SET " + col_balance + " = ? WHERE " + col_name + " = ?";

	@Override
	protected void createTableIfNotExist() {
		final String sql = "CREATE TABLE IF NOT EXISTS Account(" + 
							col_name + " TEXT PRIMARY KEY, " + 
							col_balance + " REAL DEFAULT 0, " +
							col_owner + " TEXT NOT NULL, " +
							"FOREIGN KEY (" + col_owner + ") REFERENCES User (" + UserDBManager.col_primary + ")" +
							")";
		PreparedStatement statement = createPreparedStatement(sql);
		try {
			executeUpdatePreparedStatement(statement);
		} catch (SQLException e) {
			System.err.println("Failed to create table <Account> with error message <" + e.getMessage() + ">");
		}
	}
	
	public void insertAccount(Account account) throws SQLException {
		PreparedStatement insertStatement = createPreparedStatement(insert);
		insertStatement.setString(1, account.getName());
		insertStatement.setDouble(2, account.getBalance());
		insertStatement.setString(3, account.getOwner());
		
		executeUpdatePreparedStatement(insertStatement);
	}
	
	public boolean deleteAccount(final String accountName) throws SQLException {
		PreparedStatement deleteStatement = createPreparedStatement(delete);
		deleteStatement.setString(1, accountName);
		
		final int deletedRows = executeUpdatePreparedStatement(deleteStatement);
		return (deletedRows > 0);
	}
	
	public Account[] selectAccountsForUser(final String userName) throws SQLException {
		PreparedStatement selectMultiStatement = createPreparedStatement(select);
		selectMultiStatement.setString(1, userName);
		
		ResultSet result = executePreparedStatement(selectMultiStatement);
		
		List<Account> accounts = new ArrayList<Account>();
		while (result.next()) {
			Account account = new Account();
			account.setName(result.getString(col_name));
			account.setBalance(result.getDouble(col_balance));
			account.setOwner(result.getString(col_owner));
			accounts.add(account);
		}
		
		return accounts.toArray(new Account[0]);
	}
	
	public Double selectBalance(final String accountName) throws SQLException {
		PreparedStatement balanceStatement = createPreparedStatement(selectBalance);
		balanceStatement.setString(1, accountName);
		
		ResultSet result = executePreparedStatement(balanceStatement);
		if (result.next()) {
			return result.getDouble(col_balance);
		}
		
		return null;
	}
	
	public boolean updateBalance(final double newBalance, final String accountName) throws SQLException {
		PreparedStatement updateBalanceStatement = createPreparedStatement(updateBalance);
		updateBalanceStatement.setDouble(1, newBalance);
		updateBalanceStatement.setString(2, accountName);
		
		final int updatedRows = executeUpdatePreparedStatement(updateBalanceStatement);
		return (updatedRows > 0);
	}
	
	
}
