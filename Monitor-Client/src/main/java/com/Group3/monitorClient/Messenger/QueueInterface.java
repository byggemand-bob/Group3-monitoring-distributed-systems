package com.Group3.monitorClient.Messenger;

public interface QueueInterface<T> {
    public void Put(T data);

    public T Take();

    public int Size();
}
