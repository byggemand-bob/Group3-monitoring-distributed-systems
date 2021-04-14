package com.group3.monitorServer.controller.messages;

import java.sql.*;

/*
 * SQLManager handles queries to a given database
 */
public class SQLManager {
    private final String path;
    private final String url;
    private final String fileName;
    private final Connection conn;

    public SQLManager(String path, String fileName) {
        this.path = path;
        this.url = "jdbc:sqlite:" + path;
        this.fileName = fileName;
        conn = Connect();
    }

    /* returns the number of elements in a specified table */
    public int TableSize(String TableName){
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
    public void CreateNewTable(String tableName, String... args) {
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
    public ResultSet SelectFirst(String tableName){
        return GenericStmt("SELECT * FROM "+tableName+" LIMIT 1");
    }

    public ResultSet Select (String tableName, String... whereArgs) {
        return GenericStmt(AppendWhereArgs("SELECT * FROM " + tableName, whereArgs));
    }

    /* Checks if a table with specified name exists in the database */
    public boolean CheckIfTableExists(String tableName) {
        try {
            return GenericStmt("SELECT name FROM sqlite_master WHERE name = '" + tableName +"'").next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    /* Resets the AutoIncrement to the highest number contained in the column */
    public void ResetAutoIncrement(String tableName) {
        ExecutePreparedStmt("UPDATE sqlite_sequence SET seq = 0 WHERE name = '" + tableName + "'");
    }

    /* Deletes the first element of a message Table */
    public void Delete(String tableName, String... whereArgs){
        ExecutePreparedStmt(AppendWhereArgs("DELETE FROM " + tableName, whereArgs));
    }

    private String AppendWhereArgs(String string, String[] whereArgs) {
        if (whereArgs.length > 0) {
            string += " WHERE ";
            for (String arg: whereArgs){
                string += arg + ", ";
            }
            return string.substring(0,string.length()-2);
        }
        return string;
    }

    public void DeleteAll(String tableName){
        ExecutePreparedStmt("DELETE FROM " + tableName);
    }

    /* Only statements allowed */
    public ResultSet GenericStmt(String query){
        try {
            return CreateNewStmt().executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* Only Prepared statements allowed */
    public void ExecutePreparedStmt(String query){
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());

        }
    }

    private Statement CreateNewStmt() throws SQLException {
        return conn.createStatement();
    }

    private Connection Connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url+fileName);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /* Closes the connection to the database */
    public void CloseConnection () {
        try { conn.close(); } catch (Exception e) { /* Ignored */ }
    }

    public String getPath () {
        return this.path;
    }

    public String getFileName () {
        return this.fileName;
    }

    public PreparedStatement getPreparedStmt (String sql) {
        try {
            return conn.prepareStatement(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }
}
