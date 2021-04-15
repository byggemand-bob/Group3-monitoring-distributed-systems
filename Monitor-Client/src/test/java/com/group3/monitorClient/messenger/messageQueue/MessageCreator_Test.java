package com.group3.monitorClient.messenger.messageQueue;

import com.group3.monitorClient.AbstractSQLMessageManagerTest;
import com.group3.monitorClient.messenger.messages.MessageCreator;
import com.group3.monitorClient.messenger.messages.MessageInterface;
import com.group3.monitorClient.messenger.messages.TimingMonitorDataMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openapitools.client.model.TimingMonitorData;
import org.threeten.bp.OffsetDateTime;

import java.sql.ResultSet;

public class MessageCreator_Test extends AbstractSQLMessageManagerTest {
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
        MessageCreator messageCreator = new MessageCreator();

        //Act
        TimingMonitorDataMessage timingMessage = getDefaultTimingMessage();
        timingMessage.MakeSQL(sqlMessageManager);

        ResultSet rs = sqlMessageManager.SelectFirstMessage();

        MessageInterface messageReturn = messageCreator.MakeMessageFromSQL(rs);
        TimingMonitorData tmdReturn = ((TimingMonitorDataMessage)messageReturn).getTimingMonitorData();
        TimingMonitorData tmdBefore = timingMessage.getTimingMonitorData();

        //Assert
        Assertions.assertEquals(tmdBefore.getSenderID(), tmdReturn.getSenderID());
        Assertions.assertEquals(tmdBefore.getEventID(), tmdReturn.getEventID());
        Assertions.assertEquals(tmdBefore.getTimestamp(), tmdReturn.getTimestamp());
        Assertions.assertEquals(tmdBefore.getTargetEndpoint(), tmdReturn.getTargetEndpoint());
    }
}
