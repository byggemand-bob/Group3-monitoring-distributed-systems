package com.group3.monitorServer.messages;

public interface MessageInterface {
    String separator = ",";

    /* Send message to MonitorServer, and returns http status code */
    int send(MonitorClientInterface monitorClientInterface) throws Exception;

    /*
     * Create SQL-query to save message data in SQL database
     * needed for messageQueue to function
     */
    void MakeSQL(SQLMessageManager sqlMessageManager);
}
