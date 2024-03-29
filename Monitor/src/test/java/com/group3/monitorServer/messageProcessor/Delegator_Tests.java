package com.group3.monitorServer.messageProcessor;

import com.group3.monitorServer.constraint.store.ConstraintStore;
import com.group3.monitorServer.messageProcessor.notifier.htmlNotifier.HTMLNotifier;
import com.group3.monitorServer.messages.ErrorDataMessage;
import com.group3.monitorServer.messages.MessageCreator;
import com.group3.monitorServer.messages.TimingMonitorDataMessage;
import com.group3.monitorServer.testClasses.AbstractHTMLWriterTest;
import com.group3.monitorServer.testClasses.AbstractSQLMessageManagerTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openapitools.model.TimingMonitorData;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

public class Delegator_Tests extends AbstractSQLMessageManagerTest {
    /* Tests the control functions start(), stop(), pause() and resume() */
    @Test
    public void ThreadControl() throws InterruptedException {
        //setup
        AbstractHTMLWriterTest.deleteHtml();
        HTMLNotifier htmlNotifier = new HTMLNotifier(AbstractHTMLWriterTest.HTMLTestPath, new ConstraintStore());
        int loopCount;
        Delegator delegator = new Delegator(sqlMessageManager, htmlNotifier);

        AddMessages();

        //Act & Assert
        /* verifies there is five messages in the table, before the Delegator is started */
        Assertions.assertEquals(7L, sqlMessageManager.TableSize());

        delegator.start();

        loopCount = 0;
        while(sqlMessageManager.TableSize() != 0 && loopCount < 1000){
            Thread.sleep(50);
            loopCount++;
        }

        delegator.pause();

        /* Tests that the Delegator processed all messages before continuing */
        Assertions.assertEquals(0, sqlMessageManager.TableSize());

        AddMessages();
        Thread.sleep(100);

        /* verifies the Delegator wasn't running while paused */
        Assertions.assertEquals(7L, sqlMessageManager.TableSize());

        delegator.resume();
        loopCount = 0;
        while(sqlMessageManager.TableSize() != 0 && loopCount < 1000){
            Thread.sleep(50);
            loopCount++;
        }
        delegator.stop();

        /* tests the Delegator was running again after being unpaused */
        Assertions.assertEquals(0, sqlMessageManager.TableSize());

        Thread.sleep(100);

        /* verifies that the delegator thread was terminated after calling stop() */
        Assertions.assertFalse(delegator.isAlive());
        AbstractHTMLWriterTest.deleteHtml();
    }

    private void AddMessages(){
        TimingMonitorDataMessage timingMonitorDataMessage = getDefaultTimingMessage();
        ErrorDataMessage errorDataMessage = getDefaultErrorMessage();

        //msg1
        errorDataMessage.getErrorData().setSenderID(1L);
        errorDataMessage.makeSQL(sqlMessageManager);

        //msg2
        timingMonitorDataMessage.getTimingMonitorData().setEventCode(TimingMonitorData.EventCodeEnum.RECEIVEREQUEST);
        timingMonitorDataMessage.makeSQL(sqlMessageManager);

        //msg3
        errorDataMessage.getErrorData().setSenderID(2L);
        errorDataMessage.makeSQL(sqlMessageManager);

        //msg4
        timingMonitorDataMessage.getTimingMonitorData().setEventCode(TimingMonitorData.EventCodeEnum.RECEIVERESPONSE);
        timingMonitorDataMessage.makeSQL(sqlMessageManager);

        //msg5
        errorDataMessage.getErrorData().setSenderID(3L);
        errorDataMessage.makeSQL(sqlMessageManager);

        //msg6
        timingMonitorDataMessage.getTimingMonitorData().setEventCode(TimingMonitorData.EventCodeEnum.SENDREQUEST);
        timingMonitorDataMessage.makeSQL(sqlMessageManager);

        //msg7
        timingMonitorDataMessage.getTimingMonitorData().setEventCode(TimingMonitorData.EventCodeEnum.SENDRESPONSE);
        timingMonitorDataMessage.makeSQL(sqlMessageManager);
    }

    @Test
    public void blobTest () throws SQLException {
        sqlMessageManager.InsertMessage(21L, 1, "test", "messageTest1");
        sqlMessageManager.InsertMessage(21L, 1, "test", "messageTest2");
        sqlMessageManager.InsertMessage(21L, 1, "test", "messageTest3");
        sqlMessageManager.InsertMessage(21L, 1, "test", "messageTest4");

        //Act
        ResultSet rs = sqlMessageManager.SelectMessages("Message = 'messageTest3'");



        //Assert
        Assertions.assertEquals("messageTest3", rs.getString("Message"));
        sqlMessageManager.CloseConnection();
    }

    @Test
    public void testFindTimingDataMatchPass () { //TODO: refactor to match returns of the new analyzed messages
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
            method = Delegator.class.getDeclaredMethod("findTimingDataMatch", TimingMonitorDataMessage.class);
            method.setAccessible(true);
            Delegator delegator = new Delegator(sqlMessageManager, new HTMLNotifier(AbstractHTMLWriterTest.HTMLTestPath, new ConstraintStore()));
            message = (TimingMonitorDataMessage) messageCreator.MakeMessage(timingMonitorData);
            matchingMessage = ((TimingMonitorDataMessageID) method.invoke(delegator, message)).timingMonitorDataMessage;
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

        AbstractHTMLWriterTest.deleteHtml();
    }
}
