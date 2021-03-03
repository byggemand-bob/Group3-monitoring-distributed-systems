package com.Group3.MonitorClient.junitTests.TestClasses;

import com.Group3.MonitorClient.Messenger.GreedyMessenger;
import com.Group3.MonitorClient.Messenger.SynchronizedQueue;
import com.Group3.MonitorClient.junitTests.TestClasses.MonitorClient_TestClass;

/*
 * Test class set monitorClient in GreedyMessenger to MonitorClient_TestClass,
 * this removes the need for connecting to a server
 */
public class GreedyMessenger_TestClass extends GreedyMessenger {
    public GreedyMessenger_TestClass(String monitorIP, SynchronizedQueue messageQueue) {
        super(monitorIP, messageQueue);
        monitorClient = new MonitorClient_TestClass();
    }
}
