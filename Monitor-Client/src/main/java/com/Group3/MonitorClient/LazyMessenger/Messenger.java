package com.Group3.MonitorClient.LazyMessenger;

import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.MonitorApi;
import org.openapitools.client.model.TimingMonitorData;

public class Messenger implements Runnable{
    private MonitorApi MonitorClient;
    private boolean running = true;
    private boolean paused = true;
    private SynchronizedQueue DataQueue;
    Thread thread;

    public Messenger(String MonitorIP, SynchronizedQueue DataQueue){
        ApiClient client = new ApiClient();
        client.setBasePath(MonitorIP);
        MonitorClient = new MonitorApi(client);
        this.DataQueue = DataQueue;
    }

    public void start(){
        thread = new Thread(this);
        thread.start();
    }

    public void stop(){
        //runs final loop before terminating
        running = false;
        paused = false;
        notify();
    }

    public void pause(){
        paused = true;
    }

    public void resume(){
        paused = false;
        notify();
    }

    @Override
    public void run() {
        while (running) {
            while (paused) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            TimingMonitorData Data = DataQueue.Take();

            if (Data != null) {
                try {
                    MonitorClient.addMonitorData(Data);
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    wait(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
