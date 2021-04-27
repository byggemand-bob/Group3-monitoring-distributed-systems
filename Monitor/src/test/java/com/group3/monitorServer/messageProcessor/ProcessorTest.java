package com.group3.monitorServer.messageProcessor;

import com.group3.monitorServer.constraint.Constraint;
import com.group3.monitorServer.constraint.store.ConstraintStore;
import com.group3.monitorServer.messageProcessor.notifier.htmlNotifier.HTMLNotifier;
import com.group3.monitorServer.messages.ErrorDataMessage;
import com.group3.monitorServer.messages.TimingMonitorDataMessage;
import com.group3.monitorServer.testClasses.AbstractHTMLWriterTest;
import com.group3.monitorServer.testClasses.AbstractSQLMessageManagerTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openapitools.model.ErrorData;
import org.openapitools.model.TimingMonitorData;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.LinkedList;

/*
 * Processor tests, if for testing the entire processing system in unison
 * Delegator, Notifier and constraint analyser
 */
public class ProcessorTest extends AbstractSQLMessageManagerTest {
    @Test
    public void MessageProcessing() throws InterruptedException, IOException {
        //Setup
        AbstractHTMLWriterTest.deleteHtml();

        ConstraintStore constStore = new ConstraintStore();
        HTMLNotifier htmlNotifier = new HTMLNotifier(AbstractHTMLWriterTest.HTMLTestPath, constStore);
        Delegator delegator = new Delegator(sqlMessageManager, htmlNotifier);
        LinkedList<String> Notifications = new LinkedList<>();

        Constraint constraint1 = new Constraint("1", 5000);
        constraint1.withMin(1500);
        constStore.addConstraint(constraint1);

        MessageProcessing_CreateMessages();

        //Act
        delegator.start();

        int loop = 0;
        while(sqlMessageManager.TableSize() > 1 && loop <100){
            Thread.sleep(50);
            loop++;
        }
        delegator.stop();

        Notifications = MessageProcessing_ExtractNotifications();

        //Assert
        String expected = "<b>Error violation:</b> Node <2>: after 10 successive unsuccessful attempts" +
                          " to send data to monitor server, it received the following httpcode: 400";
        Assertions.assertTrue(Notifications.contains(expected));

        expected = "<b>Timing constraint violation:</b> for target-endpoint <1>, "+
                   "Details: time difference = <6000>, bounds min: <1500>, max: <5000>";
        Assertions.assertTrue(Notifications.contains(expected));

        expected = "<b>Error violation:</b> An unknown ErrorType was encountered by node:" +
                   " <4> at Sat, 6 Nov 2021 12:20:30";
        Assertions.assertTrue(Notifications.contains(expected));

        expected = "<b>Timing constraint violation:</b> for target-endpoint <1>, " +
                   "Details: time difference = <1000>, bounds min: <1500>, max: <5000>";
        Assertions.assertTrue(Notifications.contains(expected));

        expected = "<b>Error violation:</b> Node <6>: No connection to monitor server - start:"+
                   "Sat, 6 Nov 2021 12:20:30, Connection re-established: Sat, 6 Nov 2021 12:25:30";

        Assertions.assertEquals(1, sqlMessageManager.TableSize());

        AbstractHTMLWriterTest.deleteHtml();
    }

    /* Extracts the messages from the html for comparison */
    private LinkedList<String> MessageProcessing_ExtractNotifications() throws IOException {
        LinkedList<String> Results = new LinkedList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(AbstractHTMLWriterTest.HTMLTestPath));

        String currentString;
        while((currentString = bufferedReader.readLine()) != null){
            if(currentString.startsWith("\t\t\t<li>")){
                Results.add(currentString.substring(7, currentString.length()-5));
            }
        }
        return Results;
    }

    /* Adds the messages to the sql needed for testing, inserting them in random order */
    private void MessageProcessing_CreateMessages(){
        OffsetDateTime time = OffsetDateTime.of(2021, 11, 6,
                                                12, 20, 30,
                                                1000, ZoneOffset.ofHours(2));

        TimingMonitorDataMessage timingMonitorDataMessage = getDefaultTimingMessage();
        timingMonitorDataMessage.getTimingMonitorData().setTargetEndpoint("1");
        ErrorDataMessage errorDataMessage = getDefaultErrorMessage();

        LinkedList<Integer> msgNums = new LinkedList<>();
        for(int i = 0; i <= 9; i++){
            msgNums.add(i);
        }
        Collections.shuffle(msgNums);

        while(!msgNums.isEmpty()){
            int msgNum = msgNums.removeFirst();

            if(msgNum == 0){
                //[0 & 1] Create first pair of timingMessages - wont violate
                timingMonitorDataMessage.getTimingMonitorData().setEventCode(TimingMonitorData.EventCodeEnum.RECEIVEREQUEST);
                timingMonitorDataMessage.getTimingMonitorData().setSenderID(1L);
                timingMonitorDataMessage.getTimingMonitorData().setTimestamp(time);
                timingMonitorDataMessage.getTimingMonitorData().setEventID(1L);
                timingMonitorDataMessage.makeSQL(sqlMessageManager);
            } else if(msgNum == 1) {
                timingMonitorDataMessage.getTimingMonitorData().setEventCode(TimingMonitorData.EventCodeEnum.SENDRESPONSE);
                timingMonitorDataMessage.getTimingMonitorData().setSenderID(1L);
                timingMonitorDataMessage.getTimingMonitorData().setTimestamp(time.plusSeconds(4));
                timingMonitorDataMessage.getTimingMonitorData().setEventID(1L);
                timingMonitorDataMessage.makeSQL(sqlMessageManager);
            } else if(msgNum == 2) {
                //[2] Create first ErrorMessage
                errorDataMessage.getErrorData().setSenderID(2L);
                errorDataMessage.getErrorData().setErrorMessageType(ErrorData.ErrorMessageTypeEnum.HTTPERROR);
                errorDataMessage.getErrorData().setHttpResponse(400);
                errorDataMessage.makeSQL(sqlMessageManager);
            } else if(msgNum == 3) {
                //[3 & 4] Create secondPair of TimingMessages - will violate by being longer then the max
                timingMonitorDataMessage.getTimingMonitorData().setEventCode(TimingMonitorData.EventCodeEnum.RECEIVEREQUEST);
                timingMonitorDataMessage.getTimingMonitorData().setSenderID(3L);
                timingMonitorDataMessage.getTimingMonitorData().setTimestamp(time);
                timingMonitorDataMessage.getTimingMonitorData().setEventID(2L);
                timingMonitorDataMessage.makeSQL(sqlMessageManager);
            } else if(msgNum == 4) {
                timingMonitorDataMessage.getTimingMonitorData().setEventCode(TimingMonitorData.EventCodeEnum.SENDRESPONSE);
                timingMonitorDataMessage.getTimingMonitorData().setSenderID(3L);
                timingMonitorDataMessage.getTimingMonitorData().setTimestamp(time.plusSeconds(6));
                timingMonitorDataMessage.getTimingMonitorData().setEventID(2L);
                timingMonitorDataMessage.makeSQL(sqlMessageManager);
            } else if(msgNum == 5) {
                //[5] Create second ErrorMessage
                errorDataMessage.getErrorData().setSenderID(4L);
                errorDataMessage.getErrorData().setTimestamp(time);
                errorDataMessage.getErrorData().setErrorMessageType(ErrorData.ErrorMessageTypeEnum.UNKNOWNERROR);
                errorDataMessage.makeSQL(sqlMessageManager);
            } else if(msgNum == 6) {
                //[6 & 7] Create third pair of timingMessages - will violate by being to quick
                timingMonitorDataMessage.getTimingMonitorData().setEventCode(TimingMonitorData.EventCodeEnum.RECEIVEREQUEST);
                timingMonitorDataMessage.getTimingMonitorData().setSenderID(5L);
                timingMonitorDataMessage.getTimingMonitorData().setTimestamp(time);
                timingMonitorDataMessage.getTimingMonitorData().setEventID(5L);
                timingMonitorDataMessage.makeSQL(sqlMessageManager);
            } else if(msgNum == 7) {
                timingMonitorDataMessage.getTimingMonitorData().setEventCode(TimingMonitorData.EventCodeEnum.SENDRESPONSE);
                timingMonitorDataMessage.getTimingMonitorData().setSenderID(5L);
                timingMonitorDataMessage.getTimingMonitorData().setTimestamp(time.plusSeconds(1));
                timingMonitorDataMessage.getTimingMonitorData().setEventID(5L);
                timingMonitorDataMessage.makeSQL(sqlMessageManager);
            } else if(msgNum == 8) {
                //[8] Create third ErrorMessage
                errorDataMessage.getErrorData().setSenderID(6L);
                errorDataMessage.getErrorData().setErrorMessageType(ErrorData.ErrorMessageTypeEnum.NOCONNECTION);
                String comment;
                String timeString = time.format(DateTimeFormatter.RFC_1123_DATE_TIME);
                comment = "No connection to monitor server - start: " + timeString.substring(0, timeString.length()-6);
                timeString = time.plusMinutes(5).format(DateTimeFormatter.RFC_1123_DATE_TIME);
                comment += " Connection re-established: " + timeString.substring(0, timeString.length()-6);
                errorDataMessage.getErrorData().setComment(comment);
                errorDataMessage.makeSQL(sqlMessageManager);
            } else if(msgNum == 9) {
                //[9] Create partial pair timingMessages - wont be processed
                timingMonitorDataMessage.getTimingMonitorData().setEventCode(TimingMonitorData.EventCodeEnum.RECEIVEREQUEST);
                timingMonitorDataMessage.getTimingMonitorData().setSenderID(7L);
                timingMonitorDataMessage.getTimingMonitorData().setTimestamp(time);
                timingMonitorDataMessage.getTimingMonitorData().setEventID(7L);
                timingMonitorDataMessage.makeSQL(sqlMessageManager);
            }
        }
    }
}
