package com.group3.monitorServer.controller.MessageProcessor;

import com.group3.monitorServer.controller.messages.*;
import com.group3.monitorServer.controller.queue.PersistentSQLQueue;
import org.openapitools.model.TimingMonitorData;

import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;

public class Processor implements Runnable {
    private final SQLMessageManager sqlMessageManager;
    private final MessageCreator messageCreator = new MessageCreator();
    private boolean running = false;
    private boolean paused = false;

    public Processor(SQLMessageManager sqlMessageManager){
        this.sqlMessageManager = sqlMessageManager;
    }

    public void Start(){
        running = true;
        new Thread(this).start();
    }

    public void Stop(){
        running = false;
        paused = false;
        synchronized (this){
            notify();
        }
    }

    public void Resume(){
        paused = false;
        synchronized (this){
            notify();
        }
    }

    public void Pause(){
        paused = true;
    }

    @Override
    public void run() {
        System.out.println("running");
        RunningLoop: while(running){
            while(paused){
                ThreadWait(0);
                if(!running){
                    break RunningLoop;
                }
            }
            System.out.println("Continuing");
            ResultSet allMessages = sqlMessageManager.SelectAllMessages();

            try {
                while(allMessages.next()){
                    if(allMessages.getInt("MessageType") == MessageTypeID.TimingMonitorData.ordinal()){
                        TimingMonitorDataMessage firstMessage = (TimingMonitorDataMessage) messageCreator.MakeMessageFromSQL(allMessages);
                        TimingMonitorDataMessage secondMessage = FindMatch(firstMessage);
                        if (secondMessage != null) {
                            AnalyzeTimingMessage(firstMessage, secondMessage);
                        }
                    } else if(allMessages.getInt("MessageType") == MessageTypeID.ErrorData.ordinal()){
                        AnalyzeErrorMessage(messageCreator.MakeMessageFromSQL(allMessages));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    private void AnalyzeErrorMessage(MessageInterface message) {
        //TODO: Move Analyze ErrorData and delete

    }

    private void AnalyzeTimingMessage(TimingMonitorDataMessage firstMessage, TimingMonitorDataMessage secondMessage) {
        //TODO: analyze TimingMessage and delete elements which have been analyzed
        System.out.println(firstMessage.getTimingMonitorData().getEventCode() + " belong to " + secondMessage.getTimingMonitorData().getEventCode());
    }

    private TimingMonitorDataMessage FindMatch (TimingMonitorDataMessage message) {
        String[] whereArgs = new String[2];
        TimingMonitorData.EventCodeEnum eventCodeEnum;
        switch (message.getTimingMonitorData().getEventCode().toString()) {
            case "SendRequest":
                eventCodeEnum = TimingMonitorData.EventCodeEnum.RECEIVERESPONSE;
                break;
            case "ReceiveRequest":
                eventCodeEnum = TimingMonitorData.EventCodeEnum.SENDRESPONSE;
                break;
            case "SendResponse":
                eventCodeEnum = TimingMonitorData.EventCodeEnum.RECEIVEREQUEST;
                break;
            case "ReceiveResponse":
                eventCodeEnum = TimingMonitorData.EventCodeEnum.SENDREQUEST;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + message.getTimingMonitorData().getEventCode().toString());
        }
        String blob = message.getTimingMonitorData().getTargetEndpoint() + TimingMonitorDataMessage.separator +
                message.getTimingMonitorData().getEventID() + TimingMonitorDataMessage.separator +
                eventCodeEnum.ordinal();
        whereArgs[0] = "Message = '" + blob + "'";
        whereArgs[1] = "SenderID = '" + message.getTimingMonitorData().getSenderID() + "'";
        ResultSet resultSetQuery = sqlMessageManager.SelectMessage(whereArgs);
        try {
            if (resultSetQuery != null && resultSetQuery.next()) {
                //TODO: analyze TimingMessage
                TimingMonitorDataMessage result = (TimingMonitorDataMessage) messageCreator.MakeMessageFromSQL(resultSetQuery);
                if (!resultSetQuery.next()) {
                    return result;
                } else {
                    System.out.println("");
                }
            } else {
                System.out.println("resultsetquery is null");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /* waits for specified amount of MilliSeconds if 0, waits until another calls thread.notify() */
    private void ThreadWait(int MilliSeconds){
        try {
            synchronized(this){
                wait(MilliSeconds);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
