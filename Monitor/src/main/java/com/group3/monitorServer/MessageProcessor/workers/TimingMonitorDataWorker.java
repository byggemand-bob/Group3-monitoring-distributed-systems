package com.group3.monitorServer.MessageProcessor.workers;

import com.group3.monitorServer.messages.MessageInterface;
import com.group3.monitorServer.messages.TimingMonitorDataMessage;

public class TimingMonitorDataWorker implements Runnable{
    TimingMonitorDataMessage msg1;
    TimingMonitorDataMessage msg2;
    long msg1ID;
    long msg2ID;



    public TimingMonitorDataWorker(TimingMonitorDataMessage msg1, long msg1ID, TimingMonitorDataMessage msg2, long msg2ID) {
        this.msg1 = msg1;
        this.msg2 = msg2;
        this.msg1ID = msg1ID;
        this.msg2ID = msg2ID;
    }

    @Override
    public void run() {
        AnalyzeTimingMessage(msg1, msg2);
    }

    private void AnalyzeTimingMessage(TimingMonitorDataMessage firstMessage, TimingMonitorDataMessage secondMessage) {
        //TODO: analyze TimingMessage and delete elements which have been analyzed
        System.out.println(firstMessage.getTimingMonitorData().getEventCode() + " belong to " + secondMessage.getTimingMonitorData().getEventCode());
    }
}
