package com.group3.monitorServer.messageProcessor.workers;

import com.group3.monitorServer.messages.ErrorDataMessage;
import com.group3.monitorServer.messages.SQLMessageManager;

public class ErrorMessageWorker implements Runnable{
    SQLMessageManager sqlMessageManager;
    ErrorDataMessage msg;
    long msgID;

    public ErrorMessageWorker(SQLMessageManager sqlMessageManager, ErrorDataMessage msg, long msgID) {
        this.msg = msg;
        this.msgID = msgID;
        this.sqlMessageManager = sqlMessageManager;
    }

    @Override
    public void run() {
        AnalyzeErrorMessage(msg);
    }

    private void AnalyzeErrorMessage(ErrorDataMessage message) {
        //TODO: Move Analyze ErrorData and delete
        System.out.println(message.getErrorData().getSenderID());
        System.out.println(message.getErrorData().getErrorMessageType());
        System.out.println(message.getErrorData().getComment());
        System.out.println(message.getErrorData().getTimestamp());
        System.out.println(message.getErrorData().getHttpResponse());

        sqlMessageManager.Delete("ID = " + msgID);
    }
}
