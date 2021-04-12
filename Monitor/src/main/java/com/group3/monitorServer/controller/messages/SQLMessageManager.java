package com.group3.monitorServer.controller.messages;

import java.sql.*;

/*
 * SQLManager handles queries to a given database
 */
public class SQLMessageManager {
    private SQLManager sqlManager;
    private String tableName;

    public SQLMessageManager(String path, String fileName, String tableName) {
        this.tableName = tableName;
        sqlManager = new SQLManager(path, fileName);

        if (!sqlManager.CheckIfTableExists(tableName)) {
            sqlManager.CreateNewTable(tableName,
                    "ID integer PRIMARY KEY AUTOINCREMENT",
                    "MessageType integer NOT NULL",
                    "SenderID integer NOT NULL",
                    "Timestamp text NOT NULL",
                    "Message BLOB");
        }
    }

    public ResultSet SelectAllMessages(){
        return sqlManager.SelectAll(tableName);
    }

    /* returns the number of elements in a specified table */
    public int TableSize(){
        return sqlManager.TableSize(tableName);
    }

    /* Inserts and Message element into the given table */
    public void InsertMessage(long senderID, int messageType, String timeStamp, String message) {
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
    public ResultSet SelectFirstMessage(){
        return sqlManager.SelectFirst(tableName);
    }

    /* Deletes the first element of a message Table, where ToBeSent = 1 */
    public void DeleteFirstMessage(){
        sqlManager.DeleteFirst(tableName);
        if(sqlManager.TableSize(tableName) == 0){
            sqlManager.ResetAutoIncrement(tableName);
        }
    }

    public void ResetTable(){
        sqlManager.DeleteAll(tableName);
        sqlManager.ResetAutoIncrement(tableName);
    }

    /* Only statements allowed */
    public ResultSet GenericStmt(String query){
        return sqlManager.GenericStmt(query);
    }

    /* Only Prepared statements allowed */
    public void GenericPreparedStmt(String query){
        sqlManager.GenericPreparedStmt(query);
    }

    /* Closes the connection to the database */
    public void CloseConnection () {
        sqlManager.CloseConnection();
    }

    public String getTableName() {
        return this.tableName;
    }

    public String getPath () {
        return sqlManager.getPath();
    }

    public String getFileName () {
        return sqlManager.getFileName();
    }
}
