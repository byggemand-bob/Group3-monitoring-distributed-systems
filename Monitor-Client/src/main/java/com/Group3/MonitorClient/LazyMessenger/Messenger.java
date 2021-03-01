package com.Group3.MonitorClient.LazyMessenger;

import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.MonitorApi;
import org.openapitools.client.model.TimingMonitorData;

/*
* The messenger class runs a continues thread sending TimingMonitorData from a SynchronizedQueue.
* It Utilizes the MonitorApi and will indefinably probe the queue until stopped or paused.
*/
public class Messenger implements Runnable{
    private MonitorApi monitorClient;
    private boolean running = true;
    private boolean paused = true;
    private SynchronizedQueue dataQueue;
    Thread thread;

    public Messenger(String MonitorIP, SynchronizedQueue DataQueue){
        ApiClient client = new ApiClient();
        client.setBasePath(MonitorIP);
        monitorClient = new MonitorApi(client);
        this.dataQueue = DataQueue;
    }

    /* starts a thread running current class.run() */
    public void Start(){
        thread = new Thread(this);
        thread.start();
    }

    /* This terminates the thread, whoever it completes the current loop*/
    public void Stop(){
        running = false;
        paused = false;
        notify();
    }

    /* pauses the thread, however completing the current loop before doing so */
    public void Pause(){
        paused = true;
    }

    /* resumes the thread */
    public void Resume(){
        paused = false;
        notify();
    }

    /* while running continues to take and send MonitorData while the SynchronizedQueue is not empty */
    @Override
    public void run() {
        while (running) {
            TimingMonitorData Data = dataQueue.Take();

            if (Data != null) {
                try {
                    monitorClient.addMonitorData(Data);
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

            while (paused) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
