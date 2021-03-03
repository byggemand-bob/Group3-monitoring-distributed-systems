package com.Group3.MonitorClient.junitTests.TestClasses;

import com.Group3.MonitorClient.Messenger.SynchronizedQueue;
import org.openapitools.client.model.TimingMonitorData;

/* Test class that continues to add elements to given Queue while running and un-paused */
public class DataAdder_TestClass implements Runnable {
    public SynchronizedQueue queue;
    public boolean running = true;
    public boolean paused = false;

    public DataAdder_TestClass(SynchronizedQueue Queue){
        queue = Queue;
    }

    public void pause(){
        paused = true;
    }

    public void resume(){
        paused = false;
        notify();
    }

    public void stop(){
        running = false;
        paused = false;
        notify();
    }

    @Override
    public void run() {
        while(running){
            while(paused){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            TimingMonitorData data = new TimingMonitorData();
            queue.Add(data);

            try {
                wait(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
