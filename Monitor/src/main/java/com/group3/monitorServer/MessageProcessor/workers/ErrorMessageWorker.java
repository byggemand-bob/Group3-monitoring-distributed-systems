package com.group3.monitorServer.MessageProcessor.workers;

import com.group3.monitorServer.messages.ErrorDataMessage;
import com.group3.monitorServer.messages.MessageInterface;
import com.group3.monitorServer.messages.TimingMonitorDataMessage;

public class ErrorMessageWorker implements Runnable{
    ErrorDataMessage msg;

    public ErrorMessageWorker(ErrorDataMessage msg) {
        this.msg = msg;
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
