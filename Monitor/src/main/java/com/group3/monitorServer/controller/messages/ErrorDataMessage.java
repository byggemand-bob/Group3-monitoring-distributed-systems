package com.group3.monitorServer.controller.messages;

import org.openapitools.model.ErrorData;

public class ErrorDataMessage implements MessageInterface{
    private ErrorData errorData;
    private int messageTypeID;

    public ErrorDataMessage(ErrorData errorData) {
        this.errorData = errorData;
        this.messageTypeID = MessageTypeID.ErrorData.ordinal();
    }

    /* Message sends itself using the given MonitorClientInterface */
    @Override
    public int send(MonitorClientInterface monitorClientInterface) throws Exception {
        throw new Exception("MessageInterface.send() was called. This function should never be called on the monitorServer");
    }

    /* the message converts itself into an sql format, and saves itself using provided SQLManager */
    @Override
    public void MakeSQL(SQLMessageManager sqlMessageManager) {
        String blob = errorData.getHttpResponse() + separator + errorData.getErrorMessageType().ordinal() + separator + errorData.getComment();
        sqlMessageManager.InsertMessage(errorData.getSenderID(), messageTypeID, errorData.getTimestamp().toString(),blob);
    }

    public ErrorData getErrorData () {
        return errorData;
    }

    public int getMessageTypeID () {
        return messageTypeID;
    }
}
