package com.group3.monitorServer.controller.MessageProcessor;

import com.group3.monitorServer.controller.messages.MessageCreator;
import com.group3.monitorServer.controller.messages.MessageInterface;
import com.group3.monitorServer.controller.messages.MessageTypeID;
import com.group3.monitorServer.controller.messages.TimingMonitorDataMessage;
import com.group3.monitorServer.controller.queue.PersistentSQLQueue;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class Processor implements Runnable {
    public PersistentSQLQueue queue;
    private MessageCreator messageCreator = new MessageCreator();
    private boolean running = false;
    private boolean paused = false;
    private Thread thread;

    public Processor(PersistentSQLQueue Queue){
        this.queue = Queue;
    }

    public void Start(){
        running = true;
        thread = new Thread(this);
        thread.start();
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

            ResultSet rs = queue.TakeAll();

            try {
                while(rs.next()){
                    if(rs.getInt("MessageType") == MessageTypeID.TimingMonitorData.ordinal()){
                        TimingMonitorDataMessage TimingMessage = (TimingMonitorDataMessage) messageCreator.MakeMessageFromSQL(rs);
                        LinkedList<TimingMonitorDataMessage> TimingMessagesList = new LinkedList<>();
                        TimingMessagesList = FindMatchingTimingMonitorData(TimingMessage, rs);
                        AnalyseTimingMessages(TimingMessagesList);
                        //TODO: Add elements analyzed to deletelist
                    } else if(rs.getInt("MessageType") == MessageTypeID.ErrorData.ordinal()){
                        //TODO: Move Analyze ErrorData and add to delete list
                    }
                }

                //TODO: Add foreach element in deletelist, DeleteByID()
            } catch (SQLException e) {
                e.printStackTrace();
            }

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
}
