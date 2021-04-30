package com.group3.monitorServer.messageProcessor.workers;

import com.group3.monitorServer.messageProcessor.notifier.Notifier;
import com.group3.monitorServer.messages.SQLMessageManager;
import com.group3.monitorServer.messages.TimingMonitorDataMessage;

public class TimingMonitorDataWorker implements Runnable{
    private SQLMessageManager sqlMessageManager;
    private Notifier notifier;
    private TimingMonitorDataMessage msg1;
    private TimingMonitorDataMessage msg2;
    private long msg1ID;
    private long msg2ID;

    public TimingMonitorDataWorker(SQLMessageManager sqlMessageManager, Notifier notifier, TimingMonitorDataMessage msg1, long msg1ID, TimingMonitorDataMessage msg2, long msg2ID) {
        this.msg1 = msg1;
        this.msg2 = msg2;
        this.msg1ID = msg1ID;
        this.msg2ID = msg2ID;
        this.sqlMessageManager = sqlMessageManager;
        this.notifier = notifier;
    }

    @Override
    public void run() {
        AnalyzeTimingMessage(msg1, msg2);
    }

    private void AnalyzeTimingMessage(TimingMonitorDataMessage firstMessage, TimingMonitorDataMessage secondMessage) {
        if(notifier.timingViolation(firstMessage.getTimingMonitorData(), secondMessage.getTimingMonitorData())){
            sqlMessageManager.Delete("ID = " + msg1ID);
            sqlMessageManager.Delete("ID = " + msg2ID);
        } else {
            sqlMessageManager.UpdateInUse(msg1ID, false);
            sqlMessageManager.UpdateInUse(msg2ID, false);
        }
    }
}
