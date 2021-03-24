package com.Group3.monitorClient.messenger.messages;

import org.openapitools.client.ApiException;
import org.openapitools.client.api.MonitorApi;

public class ErrorDataMessage implements MessageInterface{

    @Override
    public int send(MonitorApi MonitorClient) throws ApiException {
        return 0;
    }

    @Override
    public void MakeSQL(SQLManager sqlManager) {

    }
}
