package com.group3.monitorClient.testClasses;

import com.group3.monitorClient.controller.MonitorClientInterface;
import com.group3.monitorClient.messenger.GreedyMessenger;
import com.group3.monitorClient.messenger.messages.MessageInterface;
import com.group3.monitorClient.messenger.queue.SynchronizedQueue;

/*
 * Test class set monitorClient in GreedyMessenger to MonitorClient_TestClass,
 * this removes the need for connecting to a server
 */
public class GreedyMessenger_TestClass extends GreedyMessenger {
    public GreedyMessenger_TestClass(String monitorIP, SynchronizedQueue<MessageInterface> messageQueue) {
        super(monitorIP, messageQueue);
        monitorClientInterface = new MonitorClientInterface_TestClass(monitorIP);
    }

    /* Specifies the http status code returned when messages are being sent */
    public GreedyMessenger_TestClass(String monitorIP, SynchronizedQueue<MessageInterface> messageQueue, int ReturnStatusCode) {
        super(monitorIP, messageQueue);
        monitorClientInterface = new MonitorClientInterface_TestClass(monitorIP, ReturnStatusCode);
    }

    public void ChangeReturnedStatusCode(int StatusCode){
        ((MonitorClientInterface_TestClass)monitorClientInterface).ChangeReturnedStatusCode(StatusCode);
    }
}
