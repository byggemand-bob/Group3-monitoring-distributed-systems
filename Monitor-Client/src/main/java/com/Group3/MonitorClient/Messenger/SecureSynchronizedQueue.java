package com.Group3.MonitorClient.Messenger;

import org.openapitools.client.model.TimingMonitorData;

/*
* SecureSynchronizedQueue purpose is like its parent class to ensure only one thread has access to the queue.
* Additionally it has code stumps for saving and removing monitorData, meant to save and remove from secondary memory.
*/
public abstract class SecureSynchronizedQueue extends SynchronizedQueue {
    @Override
    public synchronized void Add(TimingMonitorData monitorData){
        save(monitorData);
        super.Add(monitorData);
    }

    /* Takes first element of queue if there is any, otherwise returns null */
    @Override
    public synchronized TimingMonitorData Take(){
        TimingMonitorData monitorData = super.Take();
        remove(monitorData);
        return monitorData;
    }

    /* Code stump for saving TimingMonitorData to secondary memory */
    abstract protected void save(TimingMonitorData monitorData);

    /* Code stump for removing TimingMonitorData to secondary memory, after message been sent */
    abstract protected void remove(TimingMonitorData monitorData);
}
