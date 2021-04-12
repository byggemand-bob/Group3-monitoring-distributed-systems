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
    @Test
    public  void comparisonTest () {
        //Setup
        String tableName = "queue";
        TimingMonitorData timingMonitorData = new TimingMonitorData();
        MessageCreator messageCreator = new MessageCreator();
        timingMonitorData.setEventID(420L);
        timingMonitorData.setEventCode(TimingMonitorData.EventCodeEnum.RECEIVEREQUEST);
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

        SQLManager sqlManager2 = new SQLManager("src/main/resources/sqlite/db/", "test.db");

        //act
        ResultSet rs1 = sqlManager.GenericStmt("SELECT * FROM " + tableName);
        ResultSet rs2 = sqlManager2.GenericStmt("SELECT * FROM " + tableName);

        rs2.next();
        rs2.next();

        //Assert
        Assertions.assertNotEquals(rs1.getLong("SenderID"), rs2.getLong("SenderID"));

        sqlManager2.CloseConnection();
    }
}
