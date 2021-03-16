package com.Group3.monitorClient.Messenger;

import com.Group3.monitorClient.Messenger.messageQueue.SQLManager;
import org.openapitools.client.api.MonitorApi;

public interface MessageInterface {
    /* Send message to MonitorServer */
    public void send(MonitorApi MonitorClient);

    /*
     * Create SQL-query to save message data in SQL database
     * needed for messageQueue to function
     */
    public void MakeSQL(SQLManager sqlManager);
}
