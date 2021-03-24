package com.Group3.monitorClient.messenger.messageQueue;

import com.Group3.monitorClient.AbstractSQLTest;
import com.Group3.monitorClient.Messenger.messages.MessageInterface;
import com.Group3.monitorClient.Messenger.messages.TimingMonitorDataMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openapitools.client.model.TimingMonitorData;
import org.threeten.bp.OffsetDateTime;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TimingMonitorDataMessage_Test extends AbstractSQLTest {
    /* verifies the TimingMonitorDataMessage is able to save itself in an sql database */
    @Test
    public void testMakeSQLPass () {
        //setup
        String tableName = "queue";
        long eventID_test = 0L;
        String targetEndpoint_test = "0";
        long senderID_test = 0L;
        String timeStamp_test = "0";
        int messageType_test = 0;
        TimingMonitorData timingMonitorData = new TimingMonitorData();
        OffsetDateTime offsetDateTime = OffsetDateTime.now();
        timingMonitorData.setSenderID(21L);
        timingMonitorData.setTimestamp(offsetDateTime);
        timingMonitorData.setTargetEndpoint("/monitor");
        timingMonitorData.setEventID(22L);
        MessageInterface messageInterface = new TimingMonitorDataMessage(timingMonitorData);

        //Act
        sqlManager.CreateNewTable(tableName,
                "ID integer PRIMARY KEY AUTOINCREMENT",
                "MessageType integer NOT NULL",
                "SenderID integer NOT NULL",
                "Timestamp text NOT NULL",
                "Message BLOB");
        messageInterface.MakeSQL(sqlManager);
        ResultSet rs = sqlManager.SelectFirst("queue");

        try {
            String blob = rs.getString("Message");
            targetEndpoint_test = blob.split(",")[0];
            eventID_test = Long.parseLong(blob.split(",")[1]);
            senderID_test = rs.getLong("SenderID");
            timeStamp_test = rs.getString("Timestamp");
            messageType_test = rs.getInt("MessageType");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Assert
        Assertions.assertEquals(timingMonitorData.getEventID(), eventID_test);
        Assertions.assertEquals(timingMonitorData.getSenderID(), senderID_test);
        Assertions.assertEquals(timingMonitorData.getTimestamp().toString(), timeStamp_test);
        Assertions.assertEquals(timingMonitorData.getTargetEndpoint(), targetEndpoint_test);
        Assertions.assertEquals(0, messageType_test);
    }
}
