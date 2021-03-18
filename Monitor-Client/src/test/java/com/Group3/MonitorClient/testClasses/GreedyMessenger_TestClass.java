package com.Group3.monitorClient.testClasses;

import com.Group3.monitorClient.Messenger.GreedyMessenger;
import com.Group3.monitorClient.Messenger.MessageInterface;
import com.Group3.monitorClient.Messenger.SynchronizedQueue;
import org.openapitools.client.model.TimingMonitorData;

/*
 * Test class set monitorClient in GreedyMessenger to MonitorClient_TestClass,
 * this removes the need for connecting to a server
 */
public class GreedyMessenger_TestClass extends GreedyMessenger {
    public GreedyMessenger_TestClass(String monitorIP, SynchronizedQueue<MessageInterface> messageQueue) {
        super(monitorIP, messageQueue);
        monitorClient = new MonitorClient_TestClass();
    }
}
