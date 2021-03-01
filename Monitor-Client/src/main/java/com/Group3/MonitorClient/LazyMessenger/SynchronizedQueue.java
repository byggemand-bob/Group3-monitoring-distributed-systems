package com.Group3.MonitorClient.LazyMessenger;

import org.openapitools.client.model.TimingMonitorData;

import java.util.LinkedList;
import java.util.Queue;

/*
* SynchronizedQueue is utilized by the MessengerClass,
* it purpose is to ensure only one process has access to the monitorDataQueue at a time.
*/
public class SynchronizedQueue {
    private Queue<TimingMonitorData> monitorDataQueue = new LinkedList<TimingMonitorData>();

    public synchronized void Add(TimingMonitorData MonitorData){
        monitorDataQueue.add(MonitorData);
    }

    public synchronized TimingMonitorData Take(){
        return monitorDataQueue.poll();
    }
}
