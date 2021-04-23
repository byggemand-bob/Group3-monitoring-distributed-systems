package com.group3.monitorServer.messageProcessor;

import com.group3.monitorServer.controller.Controllable;
import com.group3.monitorServer.messageProcessor.notifier.Notifier;
import com.group3.monitorServer.messageProcessor.workers.ErrorMessageWorker;
import com.group3.monitorServer.messageProcessor.workers.TimingMonitorDataWorker;
import com.group3.monitorServer.messages.*;
import org.openapitools.model.TimingMonitorData;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Delegator implements Controllable {

    private final SQLMessageManager sqlMessageManager;
    private final MessageCreator messageCreator = new MessageCreator();
    private final Notifier notifier;
    private boolean running = false;
    private boolean paused = false;
    private Thread thread = null;

    public Delegator(SQLMessageManager sqlMessageManager, Notifier notifier) {
        this.sqlMessageManager = sqlMessageManager;
        this.notifier = notifier;
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
                        TimingMonitorDataMessageID matchingMessage = findTimingDataMatch(firstMessage);
                        TimingMonitorDataMessage secondMessage;
                        if (matchingMessage != null) {
                            secondMessage = matchingMessage.timingMonitorDataMessage;
                        } else {
                            secondMessage = null;
                        }

                        if (secondMessage != null) {
                            sqlMessageManager.UpdateInUse(allMessages.getInt("ID"), true);
                            sqlMessageManager.UpdateInUse(matchingMessage.id, true);
                            new Thread(new TimingMonitorDataWorker(sqlMessageManager,
                                                                   notifier,
                                                                   firstMessage,
                                                                   allMessages.getInt("ID"),
                                                                   secondMessage,
                                                                   matchingMessage.id)).start();
                        }
                    } else if(allMessages.getInt("MessageType") == MessageTypeID.ErrorData.ordinal()){
                        new Thread(new ErrorMessageWorker(sqlMessageManager,
                                                          notifier,
                                                          (ErrorDataMessage) messageCreator.MakeMessageFromSQL(allMessages),
                                                          allMessages.getInt("ID"))).start();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    private TimingMonitorDataMessageID findTimingDataMatch (TimingMonitorDataMessage message) {
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
                TimingMonitorDataMessageID result = getTimingMonitorDataMessageAndID(resultSetQuery);
                if (!resultSetQuery.next()) {
                    return result;
                } else {
                    System.out.println("resultsetquery returns multiple matching messages");
                }
            } else {
                System.out.println("resultsetquery is null");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    private static TimingMonitorDataMessageID getTimingMonitorDataMessageAndID (ResultSet rs) {
        MessageCreator messageCreator = new MessageCreator();
        TimingMonitorDataMessageID result  = new TimingMonitorDataMessageID();
        try {
            result.id = rs.getInt("ID");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        result.timingMonitorDataMessage = (TimingMonitorDataMessage) messageCreator.MakeMessageFromSQL(rs);
        return result;
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
