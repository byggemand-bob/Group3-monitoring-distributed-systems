package com.Group3.monitorClient.testClasses;

import com.Group3.monitorClient.Messenger.LazyMessenger.LazyMessenger;
import com.Group3.monitorClient.Messenger.MessageInterface;
import com.Group3.monitorClient.Messenger.SynchronizedQueue;
import org.openapitools.client.model.TimingMonitorData;

/*
 * Test class set subMessenger in LazyMessenger to GreedyMessenger_TestClass,
 * this removes the need for connecting to a server
 */
public class LazyMessenger_TestClass extends LazyMessenger {
    public LazyMessenger_TestClass(String monitorIP, SynchronizedQueue<MessageInterface> messageQueue) {
        super(monitorIP, messageQueue);
        subMessenger = new GreedyMessenger_TestClass(monitorIP, messageQueue);
    }
}
