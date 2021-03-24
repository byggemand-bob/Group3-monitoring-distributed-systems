package com.Group3.monitorClient.messenger.queue;

import java.util.LinkedList;
import java.util.Queue;

/*
 * SynchronizedQueue is utilized by the MessengerClass,
 * it purpose is to ensure only one process has access to the monitorDataQueue at a time.
 */
public class SynchronizedQueue<T> implements QueueInterface<T> {
    private Queue<T> queue = new LinkedList<T>();

    @Override
    public synchronized void Put(T data){
        queue.add(data);
    }

    /* Takes first element of queue if there is any, otherwise returns null */
    @Override
    public synchronized T Take(){
        return queue.peek();
    }

    @Override
    public void Delete() {
        queue.remove();
    }

    /* returns the number of messages in the queue */
    @Override
    public int Size(){
        return queue.size();
    }
}
