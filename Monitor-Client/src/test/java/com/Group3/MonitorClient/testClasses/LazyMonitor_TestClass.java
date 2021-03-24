package com.Group3.monitorClient.testClasses;

import com.Group3.monitorClient.messenger.lazyMessenger.LazyMessenger;
import com.Group3.monitorClient.messenger.messages.MessageInterface;
import com.Group3.monitorClient.messenger.queue.SynchronizedQueue;

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
