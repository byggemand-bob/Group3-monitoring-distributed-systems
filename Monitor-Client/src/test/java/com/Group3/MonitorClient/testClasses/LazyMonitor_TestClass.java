package com.Group3.monitorClient.testClasses;

import com.Group3.MonitorClient.Messenger.LazyMessenger.LazyMessenger;
import com.Group3.MonitorClient.Messenger.SynchronizedQueue;
import org.openapitools.client.model.TimingMonitorData;

/*
 * Replaces the subMessenger with GreedyMessenger_TestClass,
 * this removes the need for connecting to a server
 */
public class LazyMonitor_TestClass extends LazyMessenger {
    public LazyMonitor_TestClass(String monitorIP, SynchronizedQueue<TimingMonitorData> messageQueue) {
        super(monitorIP, messageQueue);
        subMessenger = new GreedyMessenger_TestClass(monitorIP, messageQueue);
    }
}
