package com.Group3.monitorClient.Messenger;

public interface QueueInterface<T> {
    /* puts an element in the queue */
    void Put(T data);

    /* takes the first element in the queue, but do not delete, and return null if empty */
    T Take();

    /* deletes the first element in the queue */
    void Delete();

    /* returns the size of the queue */
    int Size();
}
