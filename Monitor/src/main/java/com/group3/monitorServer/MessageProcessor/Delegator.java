package com.group3.monitorServer.messageProcessor;

import com.group3.monitorServer.messageProcessor.workers.ErrorMessageWorker;
import com.group3.monitorServer.messageProcessor.workers.TimingMonitorDataWorker;
import com.group3.monitorServer.controller.Controllable;
import com.group3.monitorServer.messages.*;
import org.openapitools.model.TimingMonitorData;
import com.group3.monitorServer.messages.ErrorDataMessage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Delegator implements Controllable {

    private final SQLMessageManager sqlMessageManager;
    private final MessageCreator messageCreator = new MessageCreator();
    private boolean running = false;
    private boolean paused = false;
    private Thread thread = null;

    public Delegator(SQLMessageManager sqlMessageManager) {
        this.sqlMessageManager = sqlMessageManager;
    }

    @Override
    public void start(){
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void stop(){
        running = false;
        paused = false;
        synchronized (this){
            notify();
        }
    }
    @Override
    public void resume(){
        paused = false;
        synchronized (this){
            notify();
        }
    }
    @Override
    public void pause(){
        paused = true;
    }

    @Override
    public void run() {
        RunningLoop: while(running){
            while(paused){
                ThreadWait(0);
                if(!running){
                    break RunningLoop;
                }
            }
            ResultSet allMessages = sqlMessageManager.SelectMessages("InUse = 0");
            try {
                while(allMessages.next()){
                    if(allMessages.getInt("MessageType") == MessageTypeID.TimingMonitorData.ordinal()){
                        TimingMonitorDataMessage firstMessage = (TimingMonitorDataMessage) messageCreator.MakeMessageFromSQL(allMessages);
                        TimingMonitorDataMessage secondMessage = findTimingDataMatch(firstMessage);
                        if (secondMessage != null) {
                            new Thread(new TimingMonitorDataWorker(firstMessage, secondMessage)).start();
                        }
                    } else if(allMessages.getInt("MessageType") == MessageTypeID.ErrorData.ordinal()){
                        new Thread(new ErrorMessageWorker((ErrorDataMessage) messageCreator.MakeMessageFromSQL(allMessages))).start();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    private TimingMonitorDataMessage findTimingDataMatch (TimingMonitorDataMessage message) {
        String[] whereArgs = new String[2];
        TimingMonitorData.EventCodeEnum eventCodeEnum = matchingEventCode(message.getTimingMonitorData().getEventCode());
        String blob = message.getTimingMonitorData().getTargetEndpoint() + TimingMonitorDataMessage.separator +
                message.getTimingMonitorData().getEventID() + TimingMonitorDataMessage.separator +
                eventCodeEnum.ordinal();
        whereArgs[0] = "Message = '" + blob + "'";
        whereArgs[1] = "SenderID = '" + message.getTimingMonitorData().getSenderID() + "'";
        ResultSet resultSetQuery = sqlMessageManager.SelectMessages(whereArgs);
        try {
            if (resultSetQuery != null && resultSetQuery.next()) {
                TimingMonitorDataMessage result = (TimingMonitorDataMessage) messageCreator.MakeMessageFromSQL(resultSetQuery);
                if (!resultSetQuery.next()) {
                    return result;
                }
            } else {
                System.out.println("resultsetquery is null");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    private TimingMonitorData.EventCodeEnum matchingEventCode (TimingMonitorData.EventCodeEnum eventCodeEnum) {
        switch (eventCodeEnum.toString()) {
            case "SendRequest":
                return TimingMonitorData.EventCodeEnum.RECEIVERESPONSE;
            case "ReceiveRequest":
                return TimingMonitorData.EventCodeEnum.SENDRESPONSE;
            case "SendResponse":
                return TimingMonitorData.EventCodeEnum.RECEIVEREQUEST;
            case "ReceiveResponse":
                return TimingMonitorData.EventCodeEnum.SENDREQUEST;
            default:
                throw new IllegalStateException("Unexpected value of event code enum: " + eventCodeEnum.toString());
        }
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

    public boolean isAlive(){
        if(thread == null){
            return false;
        } else {
            return thread.isAlive();
        }
    }
}
