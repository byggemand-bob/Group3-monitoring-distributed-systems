package com.Group3.monitorClient.messenger.messages;

import java.sql.*;

public class SQLManager {
    private final String path;
    private final String url;
    private final String fileName;
    private Connection conn;
    private Statement stmt;

    public SQLManager(String path, String fileName) {
        this.path = path;
        this.url = "jdbc:sqlite:" + path;
        this.fileName = fileName;
        conn = Connect();
        try {
            stmt  = conn.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
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

//    public ResultSet SelectAll(String table, String... args){
//        StringBuilder cols = new StringBuilder();
//        for (String arg: args){
//            cols.append(arg).append(",");
//        }
//        cols = new StringBuilder(cols.substring(0, cols.length() - 1));
//        String sql = "SELECT "+cols+" FROM "+table;
//
//        try (ResultSet rs    = stmt.executeQuery(sql)){
//            return rs;
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//            return null;
//        }
//    }

    /* returns the number of elements in a specified table */
    public int TableSize(String TableName){
        int size = -1;
        String Quary = "SELECT COUNT(*) FROM " + TableName;
        ResultSet rs = GenericSQLQuery(Quary);
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

        try {
            // create a new table
            stmt.execute(sql.toString());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /* Inserts and Message element into the given table */
    public void InsertMessage(String tableName, long senderID, int messageType, String timeStamp, String message) {
        String sql = "INSERT INTO "+tableName+"(SenderID, MessageType, Timestamp, Message) VALUES(?,?,?,?)";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, senderID);
            pstmt.setInt(2, messageType);
            pstmt.setString(3, timeStamp);
            pstmt.setString(4, message);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /* returns the first element of tableName, as ordered by the first column */
    public ResultSet SelectFirst(String tableName){
        String sql = "SELECT * FROM "+tableName+" ORDER BY 1 LIMIT 1";

        try {
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
        }
        return null;
    }

    /* Checks if a table with specified name exists in the database */
    public boolean CheckIfExists(String tableName) {
        String sql = "SELECT name FROM sqlite_master WHERE name = '" + tableName +"'";

        try (ResultSet rs    = stmt.executeQuery(sql)){
            return rs.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /* Resets the AutoIncrement to the highest number contained in the column */
    public void ResetAutoIncrement(String tableName) {
        String sql = "UPDATE sqlite_sequence SET seq = 0 WHERE name = '" + tableName + "'";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /* Deletes the first element of a message Table */
    public void DeleteFirstMessage(String tableName){
        try {
            long ID = SelectFirst(tableName).getLong("ID");

            String Quary = "DELETE FROM " + tableName + " WHERE ID = " + ID;

            PreparedStatement pstmt = conn.prepareStatement(Quary);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
    }

    public String getPath () {
        return this.path;
    }

    public String getFileName () {
        return this.fileName;
    }

    /*
    * Made for testing
    * Only Queries allowed
     */
    public ResultSet GenericSQLQuery(String query){
        try {
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /* Closes the connection to the database */
    public void CloseConnection () {
        try { conn.close(); } catch (Exception e) { /* Ignored */ }
    }

//    public void SendMessage (MessageInterface message){
//        String sqlMessage = message.CreateMessage();
//
//        try (Connection conn = this.Connect();
//             Statement stmt  = conn.createStatement();
//             ResultSet rs    = stmt.executeQuery(sqlMessage)){
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//    }
}
