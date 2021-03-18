package com.Group3.monitorClient.testClasses;

import com.Group3.monitorClient.Messenger.LazyMessenger.LazyMessenger;
import com.Group3.monitorClient.Messenger.messages.MessageInterface;
import com.Group3.monitorClient.Messenger.Queue.SynchronizedQueue;

/*
 * Replaces the subMessenger with GreedyMessenger_TestClass,
 * this removes the need for connecting to a server
 */
public class LazyMonitor_TestClass extends LazyMessenger {
    public LazyMonitor_TestClass(String monitorIP, SynchronizedQueue<MessageInterface> messageQueue) {
        super(monitorIP, messageQueue);
        subMessenger = new GreedyMessenger_TestClass(monitorIP, messageQueue);
    }
}
