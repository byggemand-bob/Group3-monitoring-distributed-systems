package com.group3.monitorClient.testClasses;

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
        monitorClient = new MonitorClient_TestClass();
    }

    /* Specifies the http status code returned when messages are being sent */
    public GreedyMessenger_TestClass(String monitorIP, SynchronizedQueue<MessageInterface> messageQueue, int ReturnStatusCode) {
        super(monitorIP, messageQueue);
        monitorClient = new MonitorClient_TestClass(ReturnStatusCode);
    }

    public void ChangeReturnedStatusCode(int StatusCode){
        ((MonitorClient_TestClass)monitorClient).ChangeReturnedStatusCode(StatusCode);
    }
}
