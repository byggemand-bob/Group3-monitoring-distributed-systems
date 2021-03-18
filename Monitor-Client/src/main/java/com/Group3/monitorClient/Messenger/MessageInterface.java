package com.Group3.monitorClient.Messenger;

import com.Group3.monitorClient.Messenger.messageQueue.SQLManager;
import org.openapitools.client.ApiException;
import org.openapitools.client.ApiResponse;
import org.openapitools.client.api.MonitorApi;

import java.net.SocketTimeoutException;

public interface MessageInterface {
    /* Send message to MonitorServer */
    public ApiResponse<Void> send(MonitorApi MonitorClient) throws ApiException;

    /*
     * Create SQL-query to save message data in SQL database
     * needed for messageQueue to function
     */
    public void MakeSQL(SQLManager sqlManager);
}
