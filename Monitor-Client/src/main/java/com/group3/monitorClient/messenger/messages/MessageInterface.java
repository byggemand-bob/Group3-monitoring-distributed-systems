package com.group3.monitorClient.messenger.messages;

import com.group3.monitorClient.MonitorClientInterface;
import org.openapitools.client.ApiException;

public interface MessageInterface {
    String separator = ";";

    /* Send message to MonitorServer, and returns http status code */
    int send(MonitorClientInterface monitorClientInterface) throws ApiException;

    /*
     * Create SQL-query to save message data in SQL database
     * needed for messageQueue to function
     */
    void makeSQL(SQLMessageManager sqlMessageManager);
}
