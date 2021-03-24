package com.group3.monitorClient.messenger.messages;

import com.group3.monitorClient.controller.MonitorClientInterface;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.ErrorData;

public class ErrorDataMessage implements MessageInterface{

    private ErrorData errorData;
    private int messageTypeID;

    public ErrorDataMessage(ErrorData errorData) {
        this.errorData = errorData;
        this.messageTypeID = MessageTypeID.ErrorData.ordinal();
    }

    @Override
    public int send(MonitorClientInterface monitorClientInterface) throws ApiException {
        return monitorClientInterface.addErrorData(errorData);
    }

    @Override
    public void MakeSQL(SQLManager sqlManager) {
        String blob = errorData.getHttpResponse() + separator + errorData.getErrorMessageType() + separator + errorData.getComment();
        sqlManager.InsertMessage("queue", errorData.getSenderID(), messageTypeID, errorData.getTimestamp().toString(),blob);
    }

    public ErrorData getErrorData () {
        return errorData;
    }

    public int getMessageTypeID () {
        return messageTypeID;
    }

}
