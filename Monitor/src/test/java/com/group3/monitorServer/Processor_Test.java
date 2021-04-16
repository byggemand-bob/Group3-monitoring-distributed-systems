package com.group3.monitorServer;

import com.group3.monitorServer.MessageProcessor.Processor;
import com.group3.monitorServer.messages.MessageCreator;
import com.group3.monitorServer.messages.SQLMessageManager;
import com.group3.monitorServer.messages.TimingMonitorDataMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openapitools.model.TimingMonitorData;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

public class Processor_Test extends AbstractMessageManager{

    @Test
    public void blobTest () throws SQLException {
        SQLMessageManager sqlMessageManager = new SQLMessageManager("src/main/resources/sqlite/db", "queue.db", "test");
        sqlMessageManager.InsertMessage(21L, 1, "test", "messageTest1");
        sqlMessageManager.InsertMessage(21L, 1, "test", "messageTest2");
        sqlMessageManager.InsertMessage(21L, 1, "test", "messageTest3");
        sqlMessageManager.InsertMessage(21L, 1, "test", "messageTest4");

        //Act
        ResultSet rs = sqlMessageManager.SelectMessage("Message = 'messageTest3'");



        //Assert
        Assertions.assertEquals("messageTest3", rs.getString("Message"));
        sqlMessageManager.CloseConnection();
    }

    @Test
    public void testFindMatchPass () throws SQLException { //TODO: refactor to match returns of the new analyzed messages
        //Setup
        MessageCreator messageCreator = new MessageCreator();
        OffsetDateTime offsetDateTime = OffsetDateTime.now();

        //Message 1
        TimingMonitorData timingMonitorData = new TimingMonitorData();
        timingMonitorData.setTimestamp(offsetDateTime);
        timingMonitorData.setTargetEndpoint("/monitor");
        timingMonitorData.setEventID(22L);
        timingMonitorData.setSenderID(2L);
        timingMonitorData.setEventCode(TimingMonitorData.EventCodeEnum.SENDREQUEST);
        String blob = timingMonitorData.getTargetEndpoint() + TimingMonitorDataMessage.separator +
                timingMonitorData.getEventID() + TimingMonitorDataMessage.separator +
                timingMonitorData.getEventCode().ordinal();
        sqlMessageManager.InsertMessage(timingMonitorData.getSenderID(), 0, offsetDateTime.toString(), blob);


        //Message 2
        timingMonitorData.setTimestamp(offsetDateTime);
        timingMonitorData.setTargetEndpoint("/monitor");
        timingMonitorData.setEventID(24L);
        timingMonitorData.setSenderID(2L);
        timingMonitorData.setEventCode(TimingMonitorData.EventCodeEnum.SENDREQUEST);
        blob = timingMonitorData.getTargetEndpoint() + TimingMonitorDataMessage.separator +
                timingMonitorData.getEventID() + TimingMonitorDataMessage.separator +
                timingMonitorData.getEventCode().ordinal();
        sqlMessageManager.InsertMessage(timingMonitorData.getSenderID(), 0, offsetDateTime.toString(), blob);

        //Message 3
        timingMonitorData.setTimestamp(offsetDateTime);
        timingMonitorData.setTargetEndpoint("/monitor");
        timingMonitorData.setEventID(23L);
        timingMonitorData.setSenderID(2L);
        timingMonitorData.setEventCode(TimingMonitorData.EventCodeEnum.SENDREQUEST);
        blob = timingMonitorData.getTargetEndpoint() + TimingMonitorDataMessage.separator +
                timingMonitorData.getEventID() + TimingMonitorDataMessage.separator +
                timingMonitorData.getEventCode().ordinal();
        sqlMessageManager.InsertMessage(timingMonitorData.getSenderID(), 0, offsetDateTime.toString(), blob);

        //Message 4
        timingMonitorData.setTimestamp(offsetDateTime);
        timingMonitorData.setTargetEndpoint("/monitor");
        timingMonitorData.setEventID(22L);
        timingMonitorData.setSenderID(2L);
        timingMonitorData.setEventCode(TimingMonitorData.EventCodeEnum.RECEIVERESPONSE);
        blob = timingMonitorData.getTargetEndpoint() + TimingMonitorDataMessage.separator +
                timingMonitorData.getEventID() + TimingMonitorDataMessage.separator +
                timingMonitorData.getEventCode().ordinal();
        sqlMessageManager.InsertMessage(timingMonitorData.getSenderID(), 0, offsetDateTime.toString(), blob);

        //Act
        TimingMonitorDataMessage message = null;
        TimingMonitorDataMessage matchingMessage = null;
        try {
            Method method = null;
            method = Processor.class.getDeclaredMethod("FindMatch", TimingMonitorDataMessage.class);
            method.setAccessible(true);
            Processor processor = new Processor(sqlMessageManager);
            message = (TimingMonitorDataMessage) messageCreator.MakeMessage(timingMonitorData);
            matchingMessage = (TimingMonitorDataMessage) method.invoke(processor, message);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //Assert
        Assertions.assertEquals(message.getTimingMonitorData().getEventID(),matchingMessage.getTimingMonitorData().getEventID());
        Assertions.assertEquals(message.getTimingMonitorData().getSenderID(),matchingMessage.getTimingMonitorData().getSenderID());
        Assertions.assertNotEquals(message.getTimingMonitorData().getEventCode(),matchingMessage.getTimingMonitorData().getEventCode());
        Assertions.assertEquals(TimingMonitorData.EventCodeEnum.SENDREQUEST,matchingMessage.getTimingMonitorData().getEventCode());
    }
}
