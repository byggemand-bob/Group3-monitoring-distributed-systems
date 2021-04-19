package com.group3.monitorClient.messenger.messages;

import java.sql.*;

/*
 * SQLManager handles queries to a given database
 */
public class SQLManager {
    private static SQLManager singleton = new SQLManager();
    private String path;
    private String fileName;
    private Connection connection = null;

    public static SQLManager getInstance(){
        return singleton;
    }

    private SQLManager() {

    }

    public synchronized void Connect(String sqlPath, String sqlFileName){
        this.path = sqlPath;
        String url = "jdbc:sqlite:" + path;
        this.fileName = sqlFileName;

        try {
            connection = DriverManager.getConnection(url +fileName);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /* returns the number of elements in a specified table */
    public synchronized int TableSize(String TableName){
        int size = -1;
        ResultSet rs = GenericStmt("SELECT COUNT(*) FROM " + TableName);

        try {
            if(rs.next()){
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return size;
    }

    /* Creates a table with the given name, and columns as given by args */
    public synchronized void CreateNewTable(String tableName, String... args) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS ").append(tableName).append(" (\n");
        for (String arg: args){
            sql.append("\t").append(arg).append(",\n");
        }
        sql = new StringBuilder(sql.substring(0,sql.length()-2)).append("\n");
        sql.append(");");

        ExecutePreparedStmt(sql.toString());
    }

    /* returns the first element of tableName */
    public synchronized ResultSet SelectFirst(String tableName, String... whereArgs){
        return GenericStmt(AppendWhereArgs("SELECT * FROM "+tableName+" LIMIT 1", whereArgs));
    }

    public synchronized ResultSet Select (String tableName, String... whereArgs) {
        return GenericStmt(AppendWhereArgs("SELECT * FROM " + tableName, whereArgs));
    }

    /* Checks if a table with specified name exists in the database */
    public synchronized boolean CheckIfTableExists(String tableName) {
        try {
            return GenericStmt("SELECT name FROM sqlite_master WHERE name = '" + tableName +"'").next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    /* Resets the AutoIncrement to the highest number contained in the column */
    public synchronized void ResetAutoIncrement(String tableName) {
        ExecutePreparedStmt("UPDATE sqlite_sequence SET seq = 0 WHERE name = '" + tableName + "'");
    }

    /* Deletes the first element of a message Table */
    public synchronized void Delete(String tableName, String... whereArgs){
        ExecutePreparedStmt(AppendWhereArgs("DELETE FROM " + tableName, whereArgs));
    }

    private synchronized String AppendWhereArgs(String string, String[] whereArgs) {
        if (whereArgs.length > 0) {
            string += " WHERE ";
            for (String arg: whereArgs){
                string += arg + " AND ";
            }
            String returnString = string.substring(0,string.length()-5);
            return returnString;
        }
        return string;
    }

    public synchronized void DeleteAll(String tableName){
        ExecutePreparedStmt("DELETE FROM " + tableName);
    }

    /* Only statements allowed */
    public synchronized ResultSet GenericStmt(String query){
        try {
            return CreateNewStmt().executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* Only Prepared statements allowed */
    public synchronized void ExecutePreparedStmt(String query){
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    /* Only Prepared statements allowed */
    public synchronized void ExecutePreparedStmt(PreparedStatement pstmt){
        try {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private synchronized Statement CreateNewStmt() throws SQLException {
        return connection.createStatement();
    }

    /* Closes the connection to the database */
    public synchronized void CloseConnection () {
        try { connection.close(); } catch (Exception e) { /* Ignored */ }
    }

    public synchronized String getPath () {
        return this.path;
    }

    public synchronized String getFileName () {
        return this.fileName;
    }

    /*
     * Dangerous! Should always be executed afterwards by the "ExecutePreparedStmt".
     */
    public synchronized PreparedStatement getPreparedStmt (String sql) {
        try {
            return connection.prepareStatement(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }
}
