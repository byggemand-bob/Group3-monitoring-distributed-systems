package com.Group3.monitorClient.Messenger;

import java.util.LinkedList;
import java.util.Queue;

/*
 * SynchronizedQueue is utilized by the MessengerClass,
 * it purpose is to ensure only one process has access to the monitorDataQueue at a time.
 */
public class SynchronizedQueue<T> {
    private Queue<T> queue = new LinkedList<T>();

    public synchronized void Add(T data){
        queue.add(data);
    }

    /* Takes first element of queue if there is any, otherwise returns null */
    public synchronized T Take(){
        return queue.poll();
    }

    /* returns the number of messages in the queue */
    public int Size(){
        return queue.size();
    }
}
