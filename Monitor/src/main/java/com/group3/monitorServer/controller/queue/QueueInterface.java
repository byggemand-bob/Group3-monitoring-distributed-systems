package com.group3.monitorServer.controller.queue;

import com.group3.monitorServer.controller.messages.MessageInterface;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public interface QueueInterface<T> {
    /* puts an element in the queue */
    void Put(T data);

    /* takes the first element in the queue, but do not delete, and return null if empty */
    T Take();

    ResultSet Take(int number);

    /* takes all elements in the queue, without deleting them */
    ResultSet TakeAll();

    /* Deletes the element with given ID */
    void DeleteID(Long ID);

    /* deletes the first element in the queue */
    void Delete();

    /* returns the size of the queue */
    int Size();

    /* First element of queue is moved to failed queue */
    void Failed();

    /* Take first element in failed queue */
    T TakeFailed();

    /* Returns the size of failed queue */
    int SizeFailed();

    /* Delete first element of failed queue */
    void DeleteFailed();

    /* Delete all elements of failed queue */
    void DeleteAllFailed();
}
