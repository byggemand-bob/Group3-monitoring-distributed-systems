package com.group3.monitorServer.controller.messages;

public interface MessageInterface {
    String separator = ",";

    /*
     * Create SQL-query to save message data in SQL database
     * needed for messageQueue to function
     */
    void MakeSQL(SQLManagerOLD sqlManagerOLD);
}
