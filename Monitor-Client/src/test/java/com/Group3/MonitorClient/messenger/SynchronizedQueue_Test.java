package com.Group3.monitorClient.messenger;

import com.Group3.monitorClient.Messenger.SynchronizedQueue;
import com.Group3.monitorClient.testClasses.AddRemoveThread_TestClass;

import java.util.Random;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openapitools.client.model.TimingMonitorData;

import java.util.LinkedList;
import java.util.List;

public class SynchronizedQueue_Test {
    /* tests the SynchronizedQueue can handle multiple Threads manipulating it at the same time */
    @Test
    public void MultiThreadHandling(){
        SynchronizedQueue<TimingMonitorData> syncQueue = new SynchronizedQueue<TimingMonitorData>();
        Random random = new Random();
        int numOfThreads = random.nextInt(7) + 3;

        Assertions.assertEquals(syncQueue.Size(), 0);

        List<Thread> ThreadList = new LinkedList<>();
        for(int x = 0; x < numOfThreads; x++){
            ThreadList.add(new Thread(new AddRemoveThread_TestClass(syncQueue, 2, 1)));
        }

        for(Thread thread: ThreadList){
            thread.start();
        }

        for(Thread thread: ThreadList){
            try {
                thread.join();
            } catch (InterruptedException e) { e.printStackTrace(); }
        }

        Assertions.assertEquals(numOfThreads, syncQueue.Size());
    }

    /* tests the order of the queue is maintained after multiple threads take elements */
    @Test
    public void QueueOrderTest(){
        SynchronizedQueue<TimingMonitorData> syncQueue = new SynchronizedQueue<TimingMonitorData>();
        List<Thread> ThreadList = new LinkedList<>();

        for(long x = 0; x < 6; x++){
            TimingMonitorData timingMonitorData = new TimingMonitorData();
            timingMonitorData.setEventID(x);
            syncQueue.Add(timingMonitorData);
        }

        for(int x = 0; x < 5; x++){
            ThreadList.add(new Thread(new AddRemoveThread_TestClass(syncQueue, 1, 1)));
        }

        for(Thread thread: ThreadList){
            thread.start();
        }

        for(Thread thread: ThreadList){
            try {
                thread.join();
            } catch (InterruptedException e) {e.printStackTrace(); }
        }

        TimingMonitorData TestCase = syncQueue.Take();
        long TestEventID = TestCase.getEventID();

        Assertions.assertEquals(5L, TestEventID);
    }
}

