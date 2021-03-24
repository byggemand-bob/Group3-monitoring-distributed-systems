package com.Group3.monitorClient.testClasses;

import com.Group3.monitorClient.messenger.lazyMessenger.LazyMessenger;
import com.Group3.monitorClient.messenger.messages.MessageInterface;
import com.Group3.monitorClient.messenger.queue.SynchronizedQueue;

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
