package com.Group3.monitorClient.messenger.messageQueue;

import com.Group3.monitorClient.AbstractSQLTest;
import com.Group3.monitorClient.Messenger.messages.MessageInterface;
import com.Group3.monitorClient.Messenger.messages.MessageCreator;
import com.Group3.monitorClient.Messenger.messages.TimingMonitorDataMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openapitools.client.model.TimingMonitorData;
import org.threeten.bp.OffsetDateTime;

import java.sql.ResultSet;

public class MessageCreator_Test extends AbstractSQLTest {
    /* Verifies the messageCreator is able to construct the message properly when given a TimingMonitorData instance */
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
        Assertions.assertEquals(timingMonitorData, ((TimingMonitorDataMessage)message).getTimingMonitorData());
    }

    /*
     * verifies the MessageCreator is able to recreate an instance of
     * TimingMonitorData after its been passed and returned as an sql ResultSet
     */
    @Test
    public void testMakeMessageFromSQLPass () {
        //Setup
        String tableName = "queue";
        TimingMonitorData timingMonitorData = new TimingMonitorData();
        MessageCreator messageCreator = new MessageCreator();
        OffsetDateTime offsetDateTime = OffsetDateTime.now();
        timingMonitorData.setSenderID(21L);
        timingMonitorData.setTimestamp(offsetDateTime);
        timingMonitorData.setTargetEndpoint("/monitor");
        timingMonitorData.setEventID(22L);

        //Act
        sqlManager.CreateNewTable(tableName,
                "ID integer PRIMARY KEY AUTOINCREMENT",
                "MessageType integer NOT NULL",
                "SenderID integer NOT NULL",
                "Timestamp text NOT NULL",
                "Message BLOB");
        MessageInterface message = messageCreator.MakeMessage(timingMonitorData);

        message.MakeSQL(sqlManager);

        ResultSet rs = sqlManager.SelectFirst(tableName);

        MessageInterface messageReturn = messageCreator.CreateMessageFromSQL(rs);
        TimingMonitorData tmdReturn = ((TimingMonitorDataMessage)messageReturn).getTimingMonitorData();
        TimingMonitorData tmdBefore = ((TimingMonitorDataMessage)message).getTimingMonitorData();

        //Assert
        Assertions.assertEquals(tmdBefore.getSenderID(), tmdReturn.getSenderID());
        Assertions.assertEquals(tmdBefore.getEventID(), tmdReturn.getEventID());
        Assertions.assertEquals(tmdBefore.getTimestamp(), tmdReturn.getTimestamp());
        Assertions.assertEquals(tmdBefore.getTargetEndpoint(), tmdReturn.getTargetEndpoint());
    }
}
