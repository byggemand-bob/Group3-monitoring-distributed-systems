package com.group3.monitorServer.controller.messages;

import org.openapitools.model.ErrorData;

public class ErrorDataMessage implements MessageInterface {
    //TODO: Make tests
    private ErrorData errorData;
    private int messageTypeID;

    public ErrorDataMessage(ErrorData errorData) {
        this.errorData = errorData;
        this.messageTypeID = MessageTypeID.ErrorData.ordinal();
    }

    /* the message converts itself into an sql format, and saves itself using provided SQLManager */
    @Override
    public void MakeSQL(SQLManagerOLD sqlManagerOLD) {
        String blob = errorData.getHttpResponse() + separator + errorData.getErrorMessageType().ordinal() + separator + errorData.getComment();
        sqlManagerOLD.InsertMessage("queue", errorData.getSenderID(), messageTypeID, errorData.getTimestamp().toString(),blob);
    }

    public ErrorData getErrorData () {
        return errorData;
    }

    public int getMessageTypeID () {
        return messageTypeID;
    }

}
