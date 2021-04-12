package com.group3.monitorServer.controller.messages;

import java.sql.*;

/*
 * SQLManager handles queries to a given database
 */
public class SQLManagerOLD {
    private final String path;
    private final String url;
    private final String fileName;
    private Connection conn;

    public SQLManagerOLD(String path, String fileName) {
        this.path = path;
        this.url = "jdbc:sqlite:" + path;
        this.fileName = fileName;
        conn = Connect();
    }

    public ResultSet SelectAllMessages(String TableName){
        return GenericStmt("SELECT * FROM " + TableName);
    }

    public ResultSet SelectMessages(String TableName, int Number){
        return GenericStmt("SELECT * FROM " + TableName + " LIMIT " + Number);
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

    public int TableSize(String TableName, String WhereArgs){
        int size = -1;
        ResultSet rs = GenericStmt("SELECT COUNT(*) FROM " + TableName + " WHERE " + WhereArgs);

        try {
            if(rs.next()){
                size = rs.getInt(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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

        GenericPreparedStmt(sql.toString());
    }

    /* Inserts and Message element into the given table */
    public void InsertMessage(String tableName, long senderID, int messageType, String timeStamp, String message) {
        //TODO: choose the prettiest in all the lands
        GenericPreparedStmt("INSERT INTO "+tableName+"(SenderID, MessageType, Timestamp, Message) " +
                "VALUES(" +
                        senderID + ',' +
                        messageType + ",'" +
                        timeStamp +"','" +
                        message + "')");
//        try {
//            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO "+tableName+"(SenderID, MessageType, Timestamp, Message) VALUES(?,?,?,?)");
//            pstmt.setLong(1, senderID);
//            pstmt.setInt(2, messageType);
//            pstmt.setString(3, timeStamp);
//            pstmt.setString(4, message);
//            pstmt.executeUpdate();
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
    }

    /* returns the first element of tableName, as ordered by the first column and has ToBeSent = 1 */
    public ResultSet SelectFirstMessage(String tableName){
        return GenericStmt("SELECT * FROM "+tableName+" WHERE ToBeSent = 1 ORDER BY 1 LIMIT 1");
    }

    /* returns the first element of tableName, as ordered by the first column and has ToBeSent = 0 */
    public ResultSet SelectFirstFailedMessage(String tableName){
        return GenericStmt("SELECT * FROM "+tableName+" WHERE ToBeSent = 0 ORDER BY 1 LIMIT 1");
    }

    public ResultSet SelectBySenderID(String tableName){
        return GenericStmt("SELECT * FROM "+tableName+" WHERE ToBeSent = 0 ORDER BY SenderID");
    }

    /* Checks if a table with specified name exists in the database */
    public boolean CheckIfExists(String tableName) {
        try {
            return GenericStmt("SELECT name FROM sqlite_master WHERE name = '" + tableName +"'").next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    /* Resets the AutoIncrement to the highest number contained in the column */
    public void ResetAutoIncrement(String tableName) {
        GenericPreparedStmt("UPDATE sqlite_sequence SET seq = 0 WHERE name = '" + tableName + "'");
    }

    /* Deletes the first element of a message Table, where ToBeSent = 1 */
    public void DeleteFirstMessage(String tableName){
        try {
            long ID = SelectFirstMessage(tableName).getLong("ID");
            GenericPreparedStmt("DELETE FROM " + tableName + " WHERE ID = " + ID);
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
    }

    /* Deletes the first element of a message Table, where ToBeSent = 0 */
    public void DeleteFirstFailedMessage(String tableName){
        try {
            long ID = SelectFirstFailedMessage(tableName).getLong("ID");
            GenericPreparedStmt("DELETE FROM " + tableName + " WHERE ID = " + ID);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void DeleteAllFailedMessages(String tableName){
        String sql = "DELETE FROM " + tableName + " WHERE ToBeSent = 0";
        GenericPreparedStmt(sql);
    }

    public void DeleteByID (String tableName, long id) {
        String sql = "DELETE FROM " + tableName + " WHERE ID = " + id;
        GenericPreparedStmt(sql);
    }

    public void ChangeStatusOfFirstToBeSentElement (String tableName) {
        String sql = "SELECT * FROM "+tableName+" WHERE ToBeSent = 1 ORDER BY 1 LIMIT 1";
        try {
            long ID = GenericStmt(sql).getLong(1);
            GenericPreparedStmt("UPDATE " + tableName + " SET ToBeSent = 0 WHERE ID = '" + ID + "'");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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
     * Only statements allowed
     */
    public ResultSet GenericStmt(String query){
        try {
            return CreateNewStmt().executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * Made for testing
     * Only Prepared statements allowed
     */
    public void GenericPreparedStmt(String query){
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());

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

    private Statement CreateNewStmt() throws SQLException {
        return conn.createStatement();
    }

    /* Closes the connection to the database */
    public void CloseConnection () {
        try { conn.close(); } catch (Exception e) { /* Ignored */ }
    }
}
