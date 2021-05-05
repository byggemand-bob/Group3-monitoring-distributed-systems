package com.group3.monitorClient.testClasses;

import com.group3.monitorClient.messenger.Messenger;
/*
 * Test class set monitorClient in GreedyMessenger to MonitorClient_TestClass,
 * this removes the need for connecting to a server
 */
public class Messenger_TestClass extends Messenger {
    public Messenger_TestClass(String monitorIP, String sqlPath, String sqlFileName) {
        super();
        initialize(sqlPath, sqlFileName);
        monitorClientInterface = new MonitorClientInterface_TestClass(monitorIP);
    }

    /* Specifies the http status code returned when messages are being sent */
    public Messenger_TestClass(String monitorIP, String sqlPath, String sqlFileName, int ReturnStatusCode) {
        super();
        initialize(sqlPath, sqlFileName);
        monitorClientInterface = new MonitorClientInterface_TestClass(monitorIP, ReturnStatusCode);
    }

    public void ChangeReturnedStatusCode(int StatusCode){
        ((MonitorClientInterface_TestClass)monitorClientInterface).ChangeReturnedStatusCode(StatusCode);
    }

    public void CloseConnection(){
        sqlMessageManager.CloseConnection();
    }
}
