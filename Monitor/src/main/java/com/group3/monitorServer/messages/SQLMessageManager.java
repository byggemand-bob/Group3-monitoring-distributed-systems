package com.group3.monitorServer.messages;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * SQLManager handles queries to a given database
 */
public class SQLMessageManager {
    private SQLManager sqlManager;
    private String tableName;

    public SQLMessageManager(SQLManager sqlManager, String tableName) {
        this.tableName = tableName;
        this.sqlManager = sqlManager;

        if (!sqlManager.CheckIfTableExists(tableName)) {
            sqlManager.CreateNewTable(tableName,
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT",
                    "MessageType INTEGER NOT NULL",
                    "SenderID INTEGER NOT NULL",
                    "Timestamp TEXT NOT NULL",
                    "InUse BOOLEAN DEFAULT 0",
                    "Message BLOB");
        }
    }

    public ResultSet SelectAllMessages(){
        return sqlManager.Select(tableName);
    }

    /* returns the number of elements in a specified table */
    public long TableSize(){
        return sqlManager.TableSize(tableName);
    }

    /* Inserts and Message element into the given table */
    public void InsertMessage(long senderID, int messageType, String timeStamp, String message) {
        try {
            PreparedStatement pstmt = sqlManager.getPreparedStmt("INSERT INTO "+tableName+"(SenderID, MessageType, Timestamp, Message) VALUES(?,?,?,?)");
            pstmt.setLong(1, senderID);
            pstmt.setInt(2, messageType);
            pstmt.setString(3, timeStamp);
            pstmt.setString(4, message);
            sqlManager.ExecutePreparedStmt(pstmt);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /* returns the first element of tableName, as ordered by the first column and has ToBeSent = 1 */
    public ResultSet SelectFirstMessage(String... whereArgs){
        return sqlManager.SelectFirst(tableName, whereArgs);
    }

    public ResultSet SelectMessages(String... whereArgs) {
        return sqlManager.Select(tableName, whereArgs);
    }

    /* Deletes elements with specified where args from queue */
    public void Delete(String... whereArgs){
        sqlManager.Delete(tableName, whereArgs);
        if(sqlManager.TableSize(tableName) == 0){
            sqlManager.ResetAutoIncrement(tableName);
        }
    }

    /*Update the InUse state*/
    public void UpdateInUse (long msgId, boolean set) {
        GenericPreparedStmt("UPDATE " + tableName + " SET InUse = " + set + " WHERE ID  = " + msgId);
    }

    /* Deletes everything in the table and resets AutoIncrement */
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
        sqlManager.ExecutePreparedStmt(query);
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

    public SQLManager getSqlManager(){
        return sqlManager;
    }
}
