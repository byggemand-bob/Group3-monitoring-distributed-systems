package com.group3.monitorClient.messenger.messageQueue;

import com.group3.monitorClient.AbstractSQLTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.threeten.bp.OffsetDateTime;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLManager_Test extends AbstractSQLTest {
    /* verifies the CheckIfExists() is able to confirm the existence of a given table */
    @Test
    public void testCheckIfExistsTestPass () {
        //Setup
        String tableName = "test";
        sqlManager.CreateNewTable(tableName, "id integer PRIMARY KEY", "name text NOT NULL", "capacity real");
        //Act

        //Assert
        Assertions.assertTrue(sqlManager.CheckIfExists(tableName));
    }

    /* verifies the function CheckIfExists() isn't able to find a none existent table */
    @Test
    public void testCheckIfExistsTestFail () {
        //Setup
        String tableName = "test";
        //Act

        //Assert
        Assertions.assertFalse(sqlManager.CheckIfExists(tableName));
        Assertions.assertFalse(sqlManager.CheckIfExists("queue"));
    }

    /* verifies the function CreateNewTable() constructs a table as specified */
    @Test
    public void testCreateNewTableTestPass () throws SQLException {
        //Setup
        String tableName = "test";
        String[] args = {"id integer PRIMARY KEY", "name text NOT NULL", "capacity real DEFAULT 1"};
        sqlManager.CreateNewTable(tableName, args);
        ResultSet rs = sqlManager.GenericStmt("PRAGMA table_info(" + tableName + ")");
        //Act
        rs.next();

        String[] column1 ={
                rs.getString(2),//saves name
                rs.getString(3),//saves type
                rs.getString(4),//saves notnull
                rs.getString(5),//saves default value
                rs.getString(6)};//saves primary key
        rs.next();

        String[] column2 ={
                rs.getString(2),//saves name
                rs.getString(3),//saves type
                rs.getString(4),//saves notnull
                rs.getString(5),//saves default value
                rs.getString(6)};//saves primary key

        rs.next();

        String[] column3 ={
                rs.getString(2),//saves name
                rs.getString(3),//saves type
                rs.getString(4),//saves notnull
                rs.getString(5),//saves default value
                rs.getString(6)};//saves primary key


        //Assert
        //verifies there wasn't more columns than 3 in the table
        Assertions.assertFalse(rs.next());
        //checks column1
        Assertions.assertEquals("id", column1[0]);
        Assertions.assertEquals("integer", column1[1]);
        Assertions.assertEquals("0", column1[2]);
        Assertions.assertEquals(null, column1[3]);
        Assertions.assertEquals("1", column1[4]);
        //checks column2
        Assertions.assertEquals("name", column2[0]);
        Assertions.assertEquals("text", column2[1]);
        Assertions.assertEquals("1", column2[2]);
        Assertions.assertEquals(null, column2[3]);
        Assertions.assertEquals("0", column2[4]);
        //checks column3
        Assertions.assertEquals("capacity", column3[0]);
        Assertions.assertEquals("real", column3[1]);
        Assertions.assertEquals("0", column3[2]);
        Assertions.assertEquals("1", column3[3]);
        Assertions.assertEquals("0", column3[4]);
    }

    /* verifies a messages content is preserved after being inserted and retrieved from the sql database */
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
                "ToBeSent BOOLEAN DEFAULT 1",
                "Message BLOB");

        //Act
        sqlManager.InsertMessage(tableName, senderID, messageType, timeStamp, message);
        ResultSet rs = sqlManager.SelectFirstMessage(tableName);
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

    /* verifies the method DeleteFirstMessage() deletes the first and only the first message of the given table */
    @Test
    public void testDeleteFirstMessagePass() throws SQLException {
        //Setup
        String tableName = "test";
        int messageType = 2;
        String timeStamp = "test";
        String message = "test";
        sqlManager.CreateNewTable(tableName,
                "ID integer PRIMARY KEY AUTOINCREMENT",
                "MessageType integer NOT NULL",
                "SenderID integer NOT NULL",
                "Timestamp text NOT NULL",
                "ToBeSent BOOLEAN DEFAULT 1",
                "Message BLOB");

        //Act
        sqlManager.InsertMessage(tableName, 1L, messageType, timeStamp, message);
        sqlManager.InsertMessage(tableName, 2L, messageType, timeStamp, message);
        sqlManager.InsertMessage(tableName, 3L, messageType, timeStamp, message);

        int SizeBefore = sqlManager.TableSize(tableName);

        sqlManager.ChangeStatusOfFirstToBeSentElement(tableName);

        sqlManager.DeleteFirstMessage(tableName);

        int SizeAfter = sqlManager.TableSize(tableName);

        ResultSet rs = sqlManager.SelectFirstFailedMessage(tableName);

        //Assert
        Assertions.assertEquals(3, SizeBefore);
        Assertions.assertEquals(2, SizeAfter);

        /* Ensures it didn't delete the first messages that was set as ToBeSent = 0 */
        Assertions.assertEquals(1L,rs.getLong("SenderID"));
    }

    /* verifies the ResetAutoIncrement() reset the of the specified table */
    @Test
    public void testResetAutoincrementWithoutDataPass() throws SQLException {
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
                "ToBeSent BOOLEAN DEFAULT 1",
                "Message BLOB");

        //Act
        sqlManager.InsertMessage(tableName, senderID, messageType, timeStamp, message);
        sqlManager.InsertMessage(tableName, senderID, messageType, timeStamp, message);

        ResultSet rs = sqlManager.SelectFirstMessage(tableName);

        int firstID = rs.getInt("ID");

        sqlManager.DeleteFirstMessage(tableName);
        rs = sqlManager.SelectFirstMessage(tableName);

        int secondID = rs.getInt("ID");

        sqlManager.DeleteFirstMessage(tableName);
        sqlManager.ResetAutoIncrement(tableName);
        sqlManager.InsertMessage(tableName, senderID, messageType, timeStamp, message);
        rs = sqlManager.SelectFirstMessage(tableName);

        int thirdID = rs.getInt("ID");

        //Assert
        Assertions.assertEquals(1, firstID);
        Assertions.assertEquals(2, secondID);
        Assertions.assertEquals(1, thirdID);
    }

    /* verifies the ResetAutoIncrement() reset the autoincrement to the highest increment number in the table */
    @Test
    public void testResetAutoincrementWithDataPass() throws SQLException {
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
                "ToBeSent BOOLEAN DEFAULT 1",
                "Message BLOB");

        //Act
        sqlManager.InsertMessage(tableName, senderID, messageType, timeStamp, message);
        sqlManager.InsertMessage(tableName, senderID, messageType, timeStamp, message);

        sqlManager.DeleteFirstMessage(tableName);
        ResultSet rs = sqlManager.SelectFirstMessage(tableName);

        int firstID = rs.getInt("ID");

        sqlManager.ResetAutoIncrement(tableName);
        sqlManager.InsertMessage(tableName, senderID, messageType, timeStamp, message);
        sqlManager.InsertMessage(tableName, senderID, messageType, timeStamp, message);

        rs = sqlManager.SelectFirstMessage(tableName);

        int secondID = rs.getInt("ID");
        sqlManager.DeleteFirstMessage(tableName);

        rs = sqlManager.SelectFirstMessage(tableName);
        int thirdID = rs.getInt("ID");
        sqlManager.DeleteFirstMessage(tableName);

        rs = sqlManager.SelectFirstMessage(tableName);
        int fourthID = rs.getInt("ID");

        //Assert
        Assertions.assertEquals(2, firstID);
        Assertions.assertEquals(2, secondID);
        Assertions.assertEquals(3, thirdID);
        Assertions.assertEquals(4, fourthID);
    }

    @Test
    public void testChangeStatusOfFirstToBeSentElementPass () throws SQLException {
        //Setup
        String tableName = "queue";
        int messageType = 1;
        String timeStamp = OffsetDateTime.now().toString();
        String message = "Goddag :^)";
        sqlManager.CreateNewTable(tableName,
                "ID integer PRIMARY KEY AUTOINCREMENT",
                "MessageType integer NOT NULL",
                "SenderID integer NOT NULL",
                "Timestamp text NOT NULL",
                "ToBeSent BOOLEAN DEFAULT 1",
                "Message BLOB");

        //Act
        sqlManager.InsertMessage(tableName, 1L, messageType, timeStamp, message);
        sqlManager.InsertMessage(tableName, 2L, messageType, timeStamp, message);
        sqlManager.InsertMessage(tableName, 3L, messageType, timeStamp, message);

        boolean rs1before = sqlManager.GenericStmt("SELECT * FROM 'queue' WHERE SenderID = 1").getBoolean("ToBeSent");

        sqlManager.ChangeStatusOfFirstToBeSentElement(tableName);

        boolean rs1after = sqlManager.GenericStmt("SELECT * FROM 'queue' WHERE SenderID = 1").getBoolean("ToBeSent");

        boolean rs2before = sqlManager.GenericStmt("SELECT * FROM 'queue' WHERE SenderID = 2").getBoolean("ToBeSent");

        sqlManager.ChangeStatusOfFirstToBeSentElement(tableName);

        boolean rs2after = sqlManager.GenericStmt("SELECT * FROM 'queue' WHERE SenderID = 2").getBoolean("ToBeSent");

        boolean rs3 = sqlManager.GenericStmt("SELECT * FROM 'queue' WHERE SenderID = 3").getBoolean("ToBeSent");

        //Assert
        Assertions.assertTrue(rs1before);
        Assertions.assertFalse(rs1after);
        Assertions.assertTrue(rs2before);
        Assertions.assertFalse(rs2after);
        Assertions.assertTrue(rs3);
    }

    @Test
    public void testDeleteAllFailedMessagesPass(){
        //Setup
        String tableName = "queue";
        long senderID = 420;
        int messageType = 1;
        String timeStamp = OffsetDateTime.now().toString();
        String message = "Goddag :^)";
        sqlManager.CreateNewTable(tableName,
                "ID integer PRIMARY KEY AUTOINCREMENT",
                "MessageType integer NOT NULL",
                "SenderID integer NOT NULL",
                "Timestamp text NOT NULL",
                "ToBeSent BOOLEAN DEFAULT 1",
                "Message BLOB");

        //Act
        sqlManager.InsertMessage(tableName, senderID, messageType, timeStamp, message);
        sqlManager.InsertMessage(tableName, senderID, messageType, timeStamp, message);
        sqlManager.InsertMessage(tableName, senderID, messageType, timeStamp, message);

        sqlManager.ChangeStatusOfFirstToBeSentElement(tableName);
        sqlManager.ChangeStatusOfFirstToBeSentElement(tableName);

        sqlManager.DeleteAllFailedMessages(tableName);

        //Assert
        Assertions.assertEquals(1, sqlManager.TableSize(tableName));
    }

    /* verifies the method DeleteFirstFailedMessage() deletes the first and only the first failed message of the given table */
    @Test
    public void testDeleteFirstFailedMessagePass() throws SQLException {
        //Setup
        String tableName = "test";
        int messageType = 2;
        String timeStamp = "test";
        String message = "test";
        sqlManager.CreateNewTable(tableName,
                "ID integer PRIMARY KEY AUTOINCREMENT",
                "MessageType integer NOT NULL",
                "SenderID integer NOT NULL",
                "Timestamp text NOT NULL",
                "ToBeSent BOOLEAN DEFAULT 1",
                "Message BLOB");

        //Act
        sqlManager.InsertMessage(tableName, 1L, messageType, timeStamp, message);
        sqlManager.InsertMessage(tableName, 2L, messageType, timeStamp, message);
        sqlManager.InsertMessage(tableName, 3L, messageType, timeStamp, message);

        int SizeBefore = sqlManager.TableSize(tableName);

        sqlManager.ChangeStatusOfFirstToBeSentElement(tableName);

        sqlManager.DeleteFirstFailedMessage(tableName);

        int SizeAfter = sqlManager.TableSize(tableName);

        ResultSet rs = sqlManager.SelectFirstMessage(tableName);

        //Assert
        Assertions.assertEquals(3, SizeBefore);
        Assertions.assertEquals(2, SizeAfter);

        /* Ensures it didn't delete the first messages that was set as ToBeSent = 0 */
        Assertions.assertEquals(2L,rs.getLong("SenderID"));
    }

    /* Verifies the SelectFirstFailedMessage() selects the first failed message in the failed message queue */
    @Test
    public void testSelectFirstFailedMessagePass() throws SQLException {
        //Setup
        String tableName = "test";
        int messageType = 2;
        String timeStamp = "test";
        String message = "test";
        sqlManager.CreateNewTable(tableName,
                "ID integer PRIMARY KEY AUTOINCREMENT",
                "MessageType integer NOT NULL",
                "SenderID integer NOT NULL",
                "Timestamp text NOT NULL",
                "ToBeSent BOOLEAN DEFAULT 1",
                "Message BLOB");

        //Act
        sqlManager.InsertMessage(tableName, 1L, messageType, timeStamp, message);
        sqlManager.InsertMessage(tableName, 2L, messageType, timeStamp, message);
        sqlManager.InsertMessage(tableName, 3L, messageType, timeStamp, message);

        sqlManager.ChangeStatusOfFirstToBeSentElement(tableName);
        sqlManager.ChangeStatusOfFirstToBeSentElement(tableName);
        sqlManager.ChangeStatusOfFirstToBeSentElement(tableName);

        long rs1 = sqlManager.SelectFirstFailedMessage(tableName).getLong("SenderID");
        long rs2 = sqlManager.SelectFirstFailedMessage(tableName).getLong("SenderID");

        sqlManager.DeleteFirstFailedMessage(tableName);

        long rs3 = sqlManager.SelectFirstFailedMessage(tableName).getLong("SenderID");

        //Assert
        Assertions.assertEquals(1L, rs1);
        Assertions.assertEquals(1L, rs2);
        Assertions.assertEquals(2L, rs3);
    }
}
