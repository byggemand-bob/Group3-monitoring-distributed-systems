package com.group3.monitorClient.messenger.messageQueue;

import com.group3.monitorClient.AbstractSQLMessageManagerTest;
import com.group3.monitorClient.messenger.messages.MessageInterface;
import com.group3.monitorClient.messenger.messages.TimingMonitorDataMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openapitools.client.model.TimingMonitorData;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TimingMonitorDataMessage_Test extends AbstractSQLMessageManagerTest {
    /* verifies the TimingMonitorDataMessage is able to save itself in an sql database */
    @Test
    public void testMakeSQLPass () throws SQLException {
        //setup
        TimingMonitorDataMessage timingMonitorDataMessage = getDefaultTimingMessage();
        TimingMonitorData timingMonitorData = timingMonitorDataMessage.getTimingMonitorData();

        long eventID_test = -9999L;
        String targetEndpoint_test = "";
        long senderID_test = -9999L;
        String timeStamp_test = "";
        int messageType_test = -9999;

        //Act
        timingMonitorDataMessage.MakeSQL(sqlMessageManager);
        ResultSet rs = sqlMessageManager.SelectFirstMessage();

        String blob = rs.getString("Message");
        targetEndpoint_test = blob.split(MessageInterface.separator)[0];
        eventID_test = Long.parseLong(blob.split(MessageInterface.separator)[1]);
        senderID_test = rs.getLong("SenderID");
        timeStamp_test = rs.getString("Timestamp");
        messageType_test = rs.getInt("MessageType");

        //Assert
        Assertions.assertEquals(timingMonitorData.getEventID(), eventID_test);
        Assertions.assertEquals(timingMonitorData.getSenderID(), senderID_test);
        Assertions.assertEquals(timingMonitorData.getTimestamp().toString(), timeStamp_test);
        Assertions.assertEquals(timingMonitorData.getTargetEndpoint(), targetEndpoint_test);
        Assertions.assertEquals(0, messageType_test);
    }
}
