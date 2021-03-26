package com.group3.monitorClient.messenger.messageQueue;

import com.group3.monitorClient.AbstractSQLTest;
import com.group3.monitorClient.messenger.messages.MessageInterface;
import com.group3.monitorClient.messenger.messages.MessageCreator;
import com.group3.monitorClient.messenger.queue.PersistentSQLQueue;
import com.group3.monitorClient.messenger.messages.TimingMonitorDataMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openapitools.client.model.TimingMonitorData;
import org.threeten.bp.OffsetDateTime;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersistentSQLQueue_Test extends AbstractSQLTest {
    /* Verifies the queue constructs a message table if one doesn't already exist in the specified database */
    @Test
    public void testMessageQueueConstructorPass () {
        //Setup
        String[] col1 = new String[0];
        String[] col2 = new String[0];
        String[] col3 = new String[0];
        String[] col4 = new String[0];
        String[] col5 = new String[0];
        String[] col6 = new String[0];
        String tableName = "queue";
        PersistentSQLQueue messageQueue = new PersistentSQLQueue(sqlManager.getPath(), sqlManager.getFileName());

        //Act
        ResultSet rs = sqlManager.GenericStmt("PRAGMA table_info(" + tableName + ")");

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

            col6 = new String[]{
                    rs.getString(2),//saves name
                    rs.getString(3),//saves type
                    rs.getString(4),//saves notnull
                    rs.getString(5),//saves default value
                    rs.getString(6)};//saves primary key
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
        Assertions.assertEquals("ToBeSent", col5[0]);
        Assertions.assertEquals("BOOLEAN", col5[1]);
        Assertions.assertEquals("0", col5[2]);
        Assertions.assertEquals("1",col5[3]);
        Assertions.assertEquals("0", col5[4]);
        //checks col6
        Assertions.assertEquals("Message", col6[0]);
        Assertions.assertEquals("BLOB", col6[1]);
        Assertions.assertEquals("0", col6[2]);
        Assertions.assertNull(col6[3]);
        Assertions.assertEquals("0", col6[4]);

        messageQueue.CloseConnection();
    }

    /* test if the message order of the queue is preserved */
    @Test
    public void testOrderPreservationPass(){
        //setup
        PersistentSQLQueue messageQueue = new PersistentSQLQueue(sqlManager.getPath(), sqlManager.getFileName());
        TimingMonitorData timingMonitorData = new TimingMonitorData();
        MessageCreator messageCreator = new MessageCreator();
        OffsetDateTime offsetDateTime = OffsetDateTime.now();
        timingMonitorData.setTimestamp(offsetDateTime);
        timingMonitorData.setTargetEndpoint("/monitor");
        timingMonitorData.setEventID(22L);

        //Act
        timingMonitorData.setSenderID(1L);
        messageQueue.Put(messageCreator.MakeMessage(timingMonitorData));

        timingMonitorData.setSenderID(2L);
        messageQueue.Put(messageCreator.MakeMessage(timingMonitorData));

        timingMonitorData.setSenderID(3L);
        messageQueue.Put(messageCreator.MakeMessage(timingMonitorData));

        MessageInterface firstMessage = messageQueue.Take();

        messageQueue.Delete();

        MessageInterface secondMessage = messageQueue.Take();

        //Assert
        Assertions.assertEquals(1L, ((TimingMonitorDataMessage)firstMessage).getTimingMonitorData().getSenderID());
        Assertions.assertEquals(2L, ((TimingMonitorDataMessage)secondMessage).getTimingMonitorData().getSenderID());

        messageQueue.CloseConnection();
    }

    @Test
    public void testFailQueuePass(){
        PersistentSQLQueue messageQueue = new PersistentSQLQueue(sqlManager.getPath(), sqlManager.getFileName());
        TimingMonitorData timingMonitorData = new TimingMonitorData();
        MessageCreator messageCreator = new MessageCreator();
        OffsetDateTime offsetDateTime = OffsetDateTime.now();
        timingMonitorData.setTimestamp(offsetDateTime);
        timingMonitorData.setTargetEndpoint("/monitor");
        timingMonitorData.setEventID(22L);

        timingMonitorData.setSenderID(1L);
        messageQueue.Put(messageCreator.MakeMessage(timingMonitorData));

        timingMonitorData.setSenderID(2L);
        messageQueue.Put(messageCreator.MakeMessage(timingMonitorData));

        timingMonitorData.setSenderID(3L);
        messageQueue.Put(messageCreator.MakeMessage(timingMonitorData));

        //Act
        MessageInterface firstMessageBefore = messageQueue.Take();
        int SizeOfQueueBefore = messageQueue.Size();
        int SizeOfFailedQueueBefore = messageQueue.SizeFailed();

        messageQueue.Failed();

        MessageInterface firstMessageAfter = messageQueue.Take();
        int SizeOfQueueAfter = messageQueue.Size();
        int SizeOfFailedQueueAfter = messageQueue.SizeFailed();
        MessageInterface firstFailedMessage = messageQueue.TakeFailed();

        //Assert
        Assertions.assertEquals(1L, ((TimingMonitorDataMessage)firstMessageBefore).getTimingMonitorData().getSenderID());
        Assertions.assertEquals(2L, ((TimingMonitorDataMessage)firstMessageAfter).getTimingMonitorData().getSenderID());
        Assertions.assertEquals(1L, ((TimingMonitorDataMessage)firstFailedMessage).getTimingMonitorData().getSenderID());
        Assertions.assertEquals(3, SizeOfQueueBefore);
        Assertions.assertEquals(2, SizeOfQueueAfter);
        Assertions.assertEquals(0, SizeOfFailedQueueBefore);
        Assertions.assertEquals(1, SizeOfFailedQueueAfter);

        messageQueue.CloseConnection();
    }


    @Test
    public void testDeleteFailedPass () {
        PersistentSQLQueue messageQueue = new PersistentSQLQueue(sqlManager.getPath(), sqlManager.getFileName());
        TimingMonitorData timingMonitorData = new TimingMonitorData();
        MessageCreator messageCreator = new MessageCreator();
        OffsetDateTime offsetDateTime = OffsetDateTime.now();
        timingMonitorData.setTimestamp(offsetDateTime);
        timingMonitorData.setTargetEndpoint("/monitor");
        timingMonitorData.setEventID(22L);

        timingMonitorData.setSenderID(1L);
        messageQueue.Put(messageCreator.MakeMessage(timingMonitorData));

        timingMonitorData.setSenderID(2L);
        messageQueue.Put(messageCreator.MakeMessage(timingMonitorData));

        timingMonitorData.setSenderID(3L);
        messageQueue.Put(messageCreator.MakeMessage(timingMonitorData));

        messageQueue.Failed();
        messageQueue.Failed();
        messageQueue.Failed();

        //Act
        int sizeBefore = messageQueue.SizeFailed();

        messageQueue.DeleteFailed();

        int sizeAfter = messageQueue.SizeFailed();

        //Assert
        Assertions.assertEquals(3, sizeBefore);
        Assertions.assertEquals(2, sizeAfter);

        messageQueue.CloseConnection();
    }

    @Test
    public void testDeleteAllFailedPass () {
        PersistentSQLQueue messageQueue = new PersistentSQLQueue(sqlManager.getPath(), sqlManager.getFileName());
        TimingMonitorData timingMonitorData = new TimingMonitorData();
        MessageCreator messageCreator = new MessageCreator();
        OffsetDateTime offsetDateTime = OffsetDateTime.now();
        timingMonitorData.setTimestamp(offsetDateTime);
        timingMonitorData.setTargetEndpoint("/monitor");
        timingMonitorData.setEventID(22L);

        timingMonitorData.setSenderID(1L);
        messageQueue.Put(messageCreator.MakeMessage(timingMonitorData));

        timingMonitorData.setSenderID(2L);
        messageQueue.Put(messageCreator.MakeMessage(timingMonitorData));

        timingMonitorData.setSenderID(3L);
        messageQueue.Put(messageCreator.MakeMessage(timingMonitorData));

        messageQueue.Failed();
        messageQueue.Failed();
        messageQueue.Failed();

        //Act
        int sizeBefore = messageQueue.SizeFailed();

        messageQueue.DeleteAllFailed();

        int sizeAfter = messageQueue.SizeFailed();

        //Assert
        Assertions.assertEquals(3, sizeBefore);
        Assertions.assertEquals(0, sizeAfter);

        messageQueue.CloseConnection();
    }
}
