package com.Group3.MonitorServer.Controller.messages;

import com.group3.monitorClient.controller.MonitorClientInterface;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.ErrorData;

public class ErrorDataMessage implements com.group3.monitorClient.messenger.messages.MessageInterface {
    //TODO: Make tests
    private ErrorData errorData;
    private int messageTypeID;

    public ErrorDataMessage(ErrorData errorData) {
        this.errorData = errorData;
        this.messageTypeID = com.group3.monitorClient.messenger.messages.MessageTypeID.ErrorData.ordinal();
    }

    /* Message sends itself using the given MonitorClientInterface */
    @Override
    public int send(MonitorClientInterface monitorClientInterface) throws ApiException {
        return monitorClientInterface.addErrorData(errorData);
    }

    /* the message converts itself into an sql format, and saves itself using provided SQLManager */
    @Override
    public void MakeSQL(SQLManager sqlManager) {
        String blob = errorData.getHttpResponse() + separator + errorData.getErrorMessageType().ordinal() + separator + errorData.getComment();
        sqlManager.InsertMessage("queue", errorData.getSenderID(), messageTypeID, errorData.getTimestamp().toString(),blob);
    }

    public ErrorData getErrorData () {
        return errorData;
    }

    public int getMessageTypeID () {
        return messageTypeID;
    }

}
