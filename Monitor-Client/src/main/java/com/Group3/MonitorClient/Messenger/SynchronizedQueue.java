package com.Group3.MonitorClient.Messenger;

import org.openapitools.client.model.TimingMonitorData;

import java.util.LinkedList;
import java.util.Queue;

/*
 * SynchronizedQueue is utilized by the MessengerClass,
 * it purpose is to ensure only one process has access to the monitorDataQueue at a time.
 */
public class SynchronizedQueue {
    private Queue<TimingMonitorData> monitorDataQueue = new LinkedList<TimingMonitorData>();

    public synchronized void Add(TimingMonitorData monitorData){
        monitorDataQueue.add(monitorData);
    }

    /* Takes first element of queue if there is any, otherwise returns null */
    public synchronized TimingMonitorData Take(){
        return monitorDataQueue.poll();
    }

    /* returns the number of messages in the queue */
    public int Size(){
        return monitorDataQueue.size();
    }
}
