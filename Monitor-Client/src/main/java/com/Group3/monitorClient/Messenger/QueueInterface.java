package com.Group3.monitorClient.Messenger;

public interface QueueInterface<T> {
    public void put (T data);

    public T take ();
}
