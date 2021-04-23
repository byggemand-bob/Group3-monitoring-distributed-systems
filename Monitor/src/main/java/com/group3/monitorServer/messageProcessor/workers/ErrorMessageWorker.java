package com.group3.monitorServer.messageProcessor.workers;

import com.group3.monitorServer.messageProcessor.notifier.Notifier;
import com.group3.monitorServer.messages.ErrorDataMessage;
import com.group3.monitorServer.messages.SQLMessageManager;

public class ErrorMessageWorker implements Runnable{
    private SQLMessageManager sqlMessageManager;
    private Notifier notifier;
    private ErrorDataMessage msg;
    private long msgID;

    public ErrorMessageWorker(SQLMessageManager sqlMessageManager, Notifier notifier, ErrorDataMessage msg, long msgID) {
        this.msg = msg;
        this.msgID = msgID;
        this.sqlMessageManager = sqlMessageManager;
        this.notifier = notifier;
    }

    @Override
    public void run() {
        AnalyzeErrorMessage(msg);
    }

    private void AnalyzeErrorMessage(ErrorDataMessage message) {
        if(notifier.errorViolation(message.getErrorData())){
            sqlMessageManager.Delete("ID = " + msgID);
        } else {
            sqlMessageManager.UpdateInUse(msgID, false);
        }
    }
}
