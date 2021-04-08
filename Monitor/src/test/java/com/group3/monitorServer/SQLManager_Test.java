package com.group3.monitorServer;

import com.group3.monitorServer.controller.messages.MessageCreator;
import com.group3.monitorServer.controller.messages.MessageInterface;
import com.group3.monitorServer.controller.messages.TimingMonitorDataMessage;
import org.junit.jupiter.api.Test;
import org.openapitools.model.TimingMonitorData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

public class SQLManager_Test extends AbstractSQLTest {
    @Test
    public  void comparisonTest () {
        //Setup
        String tableName = "queue";
        TimingMonitorData timingMonitorData = new TimingMonitorData();
        MessageCreator messageCreator = new MessageCreator();
        timingMonitorData.setEventID(420L);
        timingMonitorData.setTimestamp(OffsetDateTime.now());
        timingMonitorData.setTargetEndpoint("test");
        MessageInterface message = messageCreator.MakeMessage(timingMonitorData);
        sqlManager.CreateNewTable(tableName,
                "ID integer PRIMARY KEY AUTOINCREMENT",
                "MessageType integer NOT NULL",
                "SenderID integer NOT NULL",
                "Timestamp text NOT NULL",
                "ToBeSent BOOLEAN DEFAULT 1",
                "Message BLOB");
        for (long i = 0; i < 10; i++) {
            timingMonitorData.setSenderID(i);
            message.MakeSQL(sqlManager);
        }
        //Act
        int tableSize = sqlManager.TableSize(tableName);
        try {

            for (int i = 1; i <= tableSize; i++){
                ResultSet rs = sqlManager.GenericStmt("SELECT * FROM " + tableName);
                for (int l = 0; l < i; l++) {
                    rs.next();
                }
                MessageInterface tempMessage = messageCreator.MakeMessageFromSQL(rs);
                rs.next();
                for (int j = i + 1; j <= tableSize; j++, rs.next()) {
                    MessageInterface currentMessage = messageCreator.MakeMessageFromSQL(rs);
                    if (((TimingMonitorDataMessage)tempMessage).getTimingMonitorData().getSenderID() == ((TimingMonitorDataMessage)currentMessage).getTimingMonitorData().getSenderID() &&
                            ((TimingMonitorDataMessage)tempMessage).getTimingMonitorData().getEventID() == ((TimingMonitorDataMessage)currentMessage).getTimingMonitorData().getEventID() &&
                            ((TimingMonitorDataMessage)tempMessage).getTimingMonitorData().getEventCode() == ((TimingMonitorDataMessage)currentMessage).getTimingMonitorData().getEventCode()){

                    }
//                    System.out.println(((TimingMonitorDataMessage)tempMessage).getTimingMonitorData().getSenderID() + " compared to " + rs.getLong("SenderID"));
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Assert
    }
}
