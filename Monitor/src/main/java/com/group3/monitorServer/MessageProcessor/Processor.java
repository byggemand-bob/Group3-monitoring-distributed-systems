package com.group3.monitorServer.MessageProcessor;

import com.group3.monitorServer.messages.*;

import java.sql.ResultSet;
import java.sql.SQLException;

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
        RunningLoop: while(running){
            while(paused){
                ThreadWait(0);
                if(!running){
                    break RunningLoop;
                }
            }
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
