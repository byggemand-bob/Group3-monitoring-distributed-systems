package com.group3.monitorServer.MessageProcessor.workers;

import com.group3.monitorServer.messages.ErrorDataMessage;

public class ErrorMessageWorker implements Runnable{
    ErrorDataMessage msg;
    long msgID;

    public ErrorMessageWorker(ErrorDataMessage msg, long msgID) {
        this.msg = msg;
        this.msgID = msgID;
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


    }
}
