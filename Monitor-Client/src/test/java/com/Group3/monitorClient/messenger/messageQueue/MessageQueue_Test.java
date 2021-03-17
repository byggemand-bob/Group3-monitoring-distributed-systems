package com.Group3.monitorClient.messenger.messageQueue;

import com.Group3.monitorClient.AbstractSQLTest;
import com.Group3.monitorClient.Messenger.MessageInterface;
import com.Group3.monitorClient.Messenger.messageQueue.MessageCreator;
import com.Group3.monitorClient.Messenger.messageQueue.MessageQueue;
import com.Group3.monitorClient.Messenger.messageQueue.TimingMonitorDataMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openapitools.client.model.TimingMonitorData;
import org.threeten.bp.OffsetDateTime;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageQueue_Test extends AbstractSQLTest {
    @Test
    public void testMakeMessageTimingMonitorDataPass () {
        //Setup
        TimingMonitorData timingMonitorData = new TimingMonitorData();
        MessageCreator messageCreator = new MessageCreator();
        OffsetDateTime offsetDateTime = OffsetDateTime.now();
        timingMonitorData.setSenderID(21L);
        timingMonitorData.setTimestamp(offsetDateTime);
        timingMonitorData.setTargetEndpoint("/monitor");
        timingMonitorData.setEventID(22L);

        //Act
        MessageInterface message = messageCreator.MakeMessage(timingMonitorData);

        //Assert
        Assertions.assertEquals(0, ((TimingMonitorDataMessage)message).getMessageTypeID());
    }

    @Test
    public void testMessageQueueConstructorPass () {
        //Setup
        String[] col1 = new String[0];
        String[] col2 = new String[0];
        String[] col3 = new String[0];
        String[] col4 = new String[0];
        String[] col5 = new String[0];
        String tableName = "queue";
        MessageQueue messageQueue = new MessageQueue(sqlManager.getPath(), sqlManager.getFileName());

        //Act
        ResultSet rs = sqlManager.GenericSQLQuery("PRAGMA table_info(" + tableName + ")");

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
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Assert

        //checks col1
        Assertions.assertEquals("ID", col1[0]);
        Assertions.assertEquals("integer", col1[1]);
        Assertions.assertEquals("0", col1[2]);
        Assertions.assertEquals(null, col1[3]);
        Assertions.assertEquals("1", col1[4]);
        //checks col2
        Assertions.assertEquals("MessageType", col2[0]);
        Assertions.assertEquals("integer", col2[1]);
        Assertions.assertEquals("1", col2[2]);
        Assertions.assertEquals(null, col2[3]);
        Assertions.assertEquals("0", col2[4]);
        //checks col3
        Assertions.assertEquals("SenderID", col3[0]);
        Assertions.assertEquals("integer", col3[1]);
        Assertions.assertEquals("1", col3[2]);
        Assertions.assertEquals(null, col3[3]);
        Assertions.assertEquals("0", col3[4]);
        //checks col4
        Assertions.assertEquals("Timestamp", col4[0]);
        Assertions.assertEquals("text", col4[1]);
        Assertions.assertEquals("1", col4[2]);
        Assertions.assertEquals(null, col4[3]);
        Assertions.assertEquals("0", col4[4]);
        //checks col5
        Assertions.assertEquals("Message", col5[0]);
        Assertions.assertEquals("BLOB", col5[1]);
        Assertions.assertEquals("0", col5[2]);
        Assertions.assertEquals(null, col5[3]);
        Assertions.assertEquals("0", col5[4]);

        messageQueue.CloseConnection();
    }
}
