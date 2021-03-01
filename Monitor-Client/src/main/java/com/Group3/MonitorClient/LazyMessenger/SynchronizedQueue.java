package com.Group3.MonitorClient.LazyMessenger;

import org.openapitools.client.model.TimingMonitorData;

import java.util.LinkedList;
import java.util.Queue;

public class SynchronizedQueue {
    private Queue<TimingMonitorData> MonitorDataQueue = new LinkedList<TimingMonitorData>();

    public synchronized void Add(TimingMonitorData MonitorData){
        MonitorDataQueue.add(MonitorData);
    }

    public synchronized TimingMonitorData Take(){
        return MonitorDataQueue.poll();
    }
}
