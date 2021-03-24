package com.Group3.monitorClient.messenger.messages;

import org.openapitools.client.ApiException;
import org.openapitools.client.api.MonitorApi;

public interface MessageInterface {
    String Separator = ",";

    /* Send message to MonitorServer, and returns http status code */
    int send(MonitorApi MonitorClient) throws ApiException;

    /*
     * Create SQL-query to save message data in SQL database
     * needed for messageQueue to function
     */
    void MakeSQL(SQLManager sqlManager);
}
