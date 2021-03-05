package com.Group3.MonitorClient.Messenger;

/*
* SecureSynchronizedQueue purpose is like its parent class to ensure only one thread has access to the queue.
* Additionally it has code stumps for saving and removing monitorData, meant to save and remove from secondary memory.
*/
public abstract class SecureSynchronizedQueue<T> extends SynchronizedQueue<T> {
    @Override
    public synchronized void Add(T data){
        save(data);
        super.Add(data);
    }

    /* Takes first element of queue if there is any, otherwise returns null */
    @Override
    public synchronized T Take(){
        T data = super.Take();
        remove(data);
        return data;
    }

    /* Code stump for saving TimingMonitorData to secondary memory */
    abstract protected void save(T monitorData);

    /* Code stump for removing TimingMonitorData to secondary memory, after message been sent */
    abstract protected void remove(T monitorData);
}
