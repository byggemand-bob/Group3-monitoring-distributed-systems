package com.group3.monitorClient.testClasses;

import com.group3.monitorClient.messenger.queue.SynchronizedQueue;
import org.openapitools.client.model.TimingMonitorData;

import java.util.Random;

/* class used to simulate adding and taking elements from the SynchronizedQueue */
public class AddRemoveThread_TestClass implements Runnable{
    public SynchronizedQueue<TimingMonitorData> syncQueue;
    public int addNum;
    public int takeNum;
    public Random random = new Random();

    public AddRemoveThread_TestClass(SynchronizedQueue<TimingMonitorData> syncQueue, int addNum, int takeNum){
        this.syncQueue = syncQueue;
        this.addNum = addNum;
        this.takeNum = takeNum;
    }

    @Override
    public void run() {
        TimingMonitorData timingMonitorData;

        for(int x = 0; x < addNum; x++){
            //adds a small random time variable to thread execution
            try {
                Thread.sleep(random.nextInt(10));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            timingMonitorData = new TimingMonitorData();
            syncQueue.Put(timingMonitorData);
        }

        for(int x = 0; x < takeNum; x++){
            syncQueue.Delete();
        }
    }
}
