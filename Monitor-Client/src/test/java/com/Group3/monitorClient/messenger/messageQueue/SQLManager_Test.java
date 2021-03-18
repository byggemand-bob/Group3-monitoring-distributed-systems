package com.Group3.monitorClient.messenger.messageQueue;

import com.Group3.monitorClient.AbstractSQLTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openapitools.client.model.TimingMonitorData;
import org.threeten.bp.OffsetDateTime;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLManager_Test extends AbstractSQLTest {

    @Test
    public void testCheckIfExistsTestPass () {
        //Setup
        String tableName = "test";
        sqlManager.CreateNewTable(tableName, "id integer PRIMARY KEY", "name text NOT NULL", "capacity real");
        //Act

        //Assert
        Assertions.assertTrue(sqlManager.CheckIfExists(tableName));
    }

    @Test
    public void testCreateNewTableTestPass () throws SQLException {
        //Setup
        String tableName = "test";
        String[] args = {"id integer PRIMARY KEY", "name text NOT NULL", "capacity real DEFAULT 1"};
        sqlManager.CreateNewTable(tableName, args);
        ResultSet rs = sqlManager.GenericSQLQuery("PRAGMA table_info(" + tableName + ")");
        //Act
        rs.next();

        String[] row1 ={
                rs.getString(2),//saves name
                rs.getString(3),//saves type
                rs.getString(4),//saves notnull
                rs.getString(5),//saves default value
                rs.getString(6)};//saves primary key
        rs.next();

        String[] row2 ={
                rs.getString(2),//saves name
                rs.getString(3),//saves type
                rs.getString(4),//saves notnull
                rs.getString(5),//saves default value
                rs.getString(6)};//saves primary key

        rs.next();

        String[] row3 ={
                rs.getString(2),//saves name
                rs.getString(3),//saves type
                rs.getString(4),//saves notnull
                rs.getString(5),//saves default value
                rs.getString(6)};//saves primary key


        //Assert
        //checks row1
        Assertions.assertEquals("id", row1[0]);
        Assertions.assertEquals("integer", row1[1]);
        Assertions.assertEquals("0", row1[2]);
        Assertions.assertEquals(null, row1[3]);
        Assertions.assertEquals("1", row1[4]);
        //checks row2
        Assertions.assertEquals("name", row2[0]);
        Assertions.assertEquals("text", row2[1]);
        Assertions.assertEquals("1", row2[2]);
        Assertions.assertEquals(null, row2[3]);
        Assertions.assertEquals("0", row2[4]);
        //checks row3
        Assertions.assertEquals("capacity", row3[0]);
        Assertions.assertEquals("real", row3[1]);
        Assertions.assertEquals("0", row3[2]);
        Assertions.assertEquals("1", row3[3]);
        Assertions.assertEquals("0", row3[4]);
    }

    @Test
    public void testInsertMessageAndTakeMessagePass () {
        //Setup
        long senderID_test = 0;
        int messageType_test = 0;
        String timeStamp_test = "0";
        String message_test = "0";
        String tableName = "test";
        long senderID = 420;
        int messageType = 1;
        String timeStamp = OffsetDateTime.now().toString();
        String message = "Goddag :^)";
        sqlManager.CreateNewTable(tableName,
                            "ID integer PRIMARY KEY AUTOINCREMENT",
                            "MessageType integer NOT NULL",
                            "SenderID integer NOT NULL",
                            "Timestamp text NOT NULL",
                            "Message BLOB");

        //Act

        sqlManager.InsertMessage(tableName, senderID, messageType, timeStamp, message);
        ResultSet rs = sqlManager.SelectMessage(tableName);
        try {
            senderID_test = rs.getLong("senderID");
            messageType_test = rs.getInt("messageType");
            timeStamp_test = rs.getString("timeStamp");
            message_test = rs.getString("message");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Assert
        Assertions.assertEquals(senderID, senderID_test);
        Assertions.assertEquals(messageType, messageType_test);
        Assertions.assertEquals(timeStamp, timeStamp_test);
        Assertions.assertEquals(message, message_test);

    }

    @Test
    public void testDeleteFirstMessagePass(){
        //Setup
        String tableName = "test";
        long senderID = 1L;
        int messageType = 2;
        String timeStamp = "test";
        String message = "test";
        sqlManager.CreateNewTable(tableName,
                "ID integer PRIMARY KEY AUTOINCREMENT",
                "MessageType integer NOT NULL",
                "SenderID integer NOT NULL",
                "Timestamp text NOT NULL",
                "Message BLOB");

        //Act
        sqlManager.InsertMessage(tableName, senderID, messageType, timeStamp, message);
        sqlManager.InsertMessage(tableName, senderID, messageType, timeStamp, message);
        sqlManager.InsertMessage(tableName, senderID, messageType, timeStamp, message);

        int SizeBefore = sqlManager.TableSize("test");

        sqlManager.DeleteFirstMessage("test");

        int SizeAfter = sqlManager.TableSize("test");

        //Assert
        Assertions.assertEquals(3, SizeBefore);
        Assertions.assertEquals(2, SizeAfter);
    }
}
