package com.group3.monitorClient.messenger;

import com.group3.monitorClient.AbstractSQLMessageManagerTest;
import com.group3.monitorClient.messenger.messages.MessageTypeID;
import com.group3.monitorClient.messenger.messages.TimingMonitorDataMessage;
import com.group3.monitorClient.testClasses.AddRemoveThread_TestClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class SQLMessageManager_Test extends AbstractSQLMessageManagerTest {
    /* Test the constructor correctly creates the messageTable */
    @Test
    public void testMessageQueueConstructorPass () {
        //Setup
        String[] col1 = new String[0];
        String[] col2 = new String[0];
        String[] col3 = new String[0];
        String[] col4 = new String[0];
        String[] col5 = new String[0];

        //Act
        ResultSet rs = sqlMessageManager.GenericStmt("PRAGMA table_info(" + getSQLTableName() + ")");

        try {
            rs.next();

            col1 = new String[]{
                    rs.getString(2),//saves name
                    rs.getString(3),//saves type
                    rs.getString(4),//saves notnull
                    rs.getString(5),//saves default value
                    rs.getString(6)};//saves primary key

            rs.next();

            col2 = new String[]{
                    rs.getString(2),//saves name
                    rs.getString(3),//saves type
                    rs.getString(4),//saves notnull
                    rs.getString(5),//saves default value
                    rs.getString(6)};//saves primary key
            rs.next();

            col3 = new String[]{
                    rs.getString(2),//saves name
                    rs.getString(3),//saves type
                    rs.getString(4),//saves notnull
                    rs.getString(5),//saves default value
                    rs.getString(6)};//saves primary key
            rs.next();

            col4 = new String[]{
                    rs.getString(2),//saves name
                    rs.getString(3),//saves type
                    rs.getString(4),//saves notnull
                    rs.getString(5),//saves default value
                    rs.getString(6)};//saves primary key
            rs.next();

            col5 = new String[]{
                    rs.getString(2),//saves name
                    rs.getString(3),//saves type
                    rs.getString(4),//saves notnull
                    rs.getString(5),//saves default value
                    rs.getString(6)};//saves primary key
            rs.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Assert

        //checks col1
        Assertions.assertEquals("ID", col1[0]);
        Assertions.assertEquals("integer", col1[1]);
        Assertions.assertEquals("0", col1[2]);
        Assertions.assertNull(col1[3]);
        Assertions.assertEquals("1", col1[4]);
        //checks col2
        Assertions.assertEquals("MessageType", col2[0]);
        Assertions.assertEquals("integer", col2[1]);
        Assertions.assertEquals("1", col2[2]);
        Assertions.assertNull(col2[3]);
        Assertions.assertEquals("0", col2[4]);
        //checks col3
        Assertions.assertEquals("SenderID", col3[0]);
        Assertions.assertEquals("integer", col3[1]);
        Assertions.assertEquals("1", col3[2]);
        Assertions.assertNull(col3[3]);
        Assertions.assertEquals("0", col3[4]);
        //checks col4
        Assertions.assertEquals("Timestamp", col4[0]);
        Assertions.assertEquals("text", col4[1]);
        Assertions.assertEquals("1", col4[2]);
        Assertions.assertNull(col4[3]);
        Assertions.assertEquals("0", col4[4]);
        //checks col5
        Assertions.assertEquals("Message", col5[0]);
        Assertions.assertEquals("BLOB", col5[1]);
        Assertions.assertEquals("0", col5[2]);
        Assertions.assertNull(col5[3]);
        Assertions.assertEquals("0", col5[4]);
    }

    /* test if the message order of the SQLMessageManager is preserved */
    @Test
    public void testOrderPreservationPass() throws SQLException {
        //setup
        TimingMonitorDataMessage timingMessage = getDefaultTimingMessage();

        //Act
        timingMessage.getTimingMonitorData().setSenderID(1L);
        timingMessage.MakeSQL(sqlMessageManager);

        timingMessage.getTimingMonitorData().setSenderID(2L);
        timingMessage.MakeSQL(sqlMessageManager);

        timingMessage.getTimingMonitorData().setSenderID(3L);
        timingMessage.MakeSQL(sqlMessageManager);

        ResultSet firstMessage = sqlMessageManager.SelectFirstMessage();

        sqlMessageManager.Delete("ID = '" + firstMessage.getLong("ID") + "'");

        ResultSet secondMessage = sqlMessageManager.SelectFirstMessage();

        //Assert
        Assertions.assertEquals(1L, firstMessage.getLong("SenderID"));
        Assertions.assertEquals(2L, secondMessage.getLong("SenderID"));
    }

    /* Ensures that the tableSize() functions works */
    @Test
    public void testSizePass() throws SQLException {
        //Setup
        for(int x = 0; x < 3; x++){
            getDefaultTimingMessage().MakeSQL(sqlMessageManager);
        }

        //Act
        long SizeBefore = sqlMessageManager.TableSize();

        ResultSet rs = sqlMessageManager.SelectFirstMessage();
        sqlMessageManager.Delete("ID = '" + rs.getLong("ID") + "'");

        long SizeAfter = sqlMessageManager.TableSize();

        //Assert
        Assertions.assertEquals(3L, SizeBefore);
        Assertions.assertEquals(2L, SizeAfter);
    }

    /* Test inserting and extracting messages remains the same */
    @Test
    public void testInsertMessageAndTakeMessagePass () throws SQLException {
        //Setup
        TimingMonitorDataMessage timingMessage = getDefaultTimingMessage();
        long senderID = timingMessage.getTimingMonitorData().getSenderID();
        int messageType = MessageTypeID.TimingMonitorData.ordinal();
        String timeStamp = timingMessage.getTimingMonitorData().getTimestamp().toString();
        String message = timingMessage.getTimingMonitorData().getTargetEndpoint()
                       + TimingMonitorDataMessage.separator + timingMessage.getTimingMonitorData().getEventID()
                       + TimingMonitorDataMessage.separator + timingMessage.getTimingMonitorData().getEventCode().ordinal();

        //Act
        timingMessage.MakeSQL(sqlMessageManager);
        ResultSet rs = sqlMessageManager.SelectFirstMessage();

        long senderID_test = rs.getLong("senderID");
        int messageType_test = rs.getInt("messageType");
        String timeStamp_test = rs.getString("timeStamp");
        String message_test = rs.getString("message");

        //Assert
        Assertions.assertEquals(senderID, senderID_test);
        Assertions.assertEquals(messageType, messageType_test);
        Assertions.assertEquals(timeStamp, timeStamp_test);
        Assertions.assertEquals(message, message_test);
    }

    /* test DeleteAll() function */
    @Test
    public void TestResetTablePass() throws SQLException {
        //Setup
        TimingMonitorDataMessage timingMessage = getDefaultTimingMessage();

        //Act
        timingMessage.MakeSQL(sqlMessageManager);
        timingMessage.MakeSQL(sqlMessageManager);
        timingMessage.MakeSQL(sqlMessageManager);

        sqlMessageManager.ResetTable();

        timingMessage.MakeSQL(sqlMessageManager);

        ResultSet rs = sqlMessageManager.SelectAllMessages();

        rs.next();

        //Assert
        Assertions.assertEquals(1, rs.getLong("ID"));
        Assertions.assertFalse(rs.next());
    }

    /* verifies the ResetAutoIncrement() reset the of the specified table */
    @Test
    public void testResetAutoincrementWithoutDataPass() throws SQLException {
        //Setup
        TimingMonitorDataMessage timingMessage = getDefaultTimingMessage();

        //Act
        timingMessage.MakeSQL(sqlMessageManager);
        timingMessage.MakeSQL(sqlMessageManager);

        ResultSet rs = sqlMessageManager.SelectFirstMessage();

        int firstID = rs.getInt("ID");

        sqlMessageManager.Delete("ID = '" + firstID + "'");
        rs = sqlMessageManager.SelectFirstMessage();

        int secondID = rs.getInt("ID");

        sqlMessageManager.Delete("ID = '" + secondID + "'");
        timingMessage.MakeSQL(sqlMessageManager);
        rs = sqlMessageManager.SelectFirstMessage();

        int thirdID = rs.getInt("ID");

        //Assert
        Assertions.assertEquals(1, firstID);
        Assertions.assertEquals(2, secondID);
        Assertions.assertEquals(1, thirdID);
    }
}

