package com.Group3.monitorClient.Messenger.messages;

import com.Group3.monitorClient.Messenger.messages.SQLManager;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.MonitorApi;

public interface MessageInterface {
    /* Send message to MonitorServer, and returns http status code */
    public int send(MonitorApi MonitorClient) throws ApiException;

    /*
     * Create SQL-query to save message data in SQL database
     * needed for messageQueue to function
     */
    public void MakeSQL(SQLManager sqlManager);
}
