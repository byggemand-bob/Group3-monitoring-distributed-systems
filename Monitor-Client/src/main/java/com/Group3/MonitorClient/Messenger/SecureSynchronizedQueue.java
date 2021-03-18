package com.Group3.monitorClient.Messenger;

/*
* SecureSynchronizedQueue purpose is like its parent class to ensure only one thread has access to the queue.
* Additionally it has code stumps for saving and removing monitorData, meant to save and remove from secondary memory.
*/
public abstract class SecureSynchronizedQueue<T> extends SynchronizedQueue<T> {
    @Override
    public synchronized void Put(T data){
        save(data);
        super.Put(data);
    }

    /* Takes first element of queue if there is any, otherwise returns null */
    @Override
    public synchronized T Take(){
        T data = super.Take();
        remove(data);
        return data;
    }

    /* Code stump for saving data to secondary memory */
    abstract protected void save(T data);

    /* Code stump for removing data to secondary memory, after message been sent */
    abstract protected void remove(T data);
}
