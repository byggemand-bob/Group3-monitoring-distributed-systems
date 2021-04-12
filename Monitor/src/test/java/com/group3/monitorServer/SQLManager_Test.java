package com.group3.monitorServer;

import com.group3.monitorServer.controller.messages.MessageCreator;
import com.group3.monitorServer.controller.messages.MessageInterface;
import com.group3.monitorServer.controller.messages.SQLManager;
import com.group3.monitorServer.controller.messages.TimingMonitorDataMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openapitools.model.TimingMonitorData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

public class SQLManager_Test extends AbstractSQLTest {
    /* Tests that there is no issues crating multiple pointers to the sql database */
    @Test
    public void DoublePointer() throws SQLException {
        //Setup
        String tableName = "queue";
        TimingMonitorData timingMonitorData = new TimingMonitorData();
        sqlManager.CreateNewTable(tableName,
                "ID integer PRIMARY KEY AUTOINCREMENT",
                "MessageType integer NOT NULL",
                "SenderID integer NOT NULL",
                "Timestamp text NOT NULL",
                "ToBeSent BOOLEAN DEFAULT 1",
                "Message BLOB");

        MessageCreator messageCreator = new MessageCreator();
        timingMonitorData.setEventID(420L);
        timingMonitorData.setEventCode(TimingMonitorData.EventCodeEnum.RECEIVEREQUEST);
        timingMonitorData.setTimestamp(OffsetDateTime.now());
        timingMonitorData.setTargetEndpoint("test");
        MessageInterface message = messageCreator.MakeMessage(timingMonitorData);

        for (long i = 0; i < 3; i++) {
            timingMonitorData.setSenderID(i);
            message.MakeSQL(sqlManager);
        }

        //act
        ResultSet rs1 = sqlManager.GenericStmt("SELECT * FROM " + tableName);
        ResultSet rs2 = sqlManager.GenericStmt("SELECT * FROM " + tableName);

        rs2.next();
        rs2.next();

        //Assert
        Assertions.assertNotEquals(rs1.getLong("SenderID"), rs2.getLong("SenderID"));
    }
}
