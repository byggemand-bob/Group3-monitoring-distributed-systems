package com.Group3.MonitorClient.LazyMessenger;

import com.Group3.MonitorClient.LazyMessenger.Requirements.Requirement;
import org.openapitools.client.model.TimingMonitorData;

import java.util.List;

public class LazyMessenger implements Runnable {
    private Messenger messenger;
    private SynchronizedQueue messageQueue;
    private List<Requirement> Requirements;
    private boolean running = true;
    private boolean paused = false;
    Thread thread;

    public LazyMessenger(String MonitorIP){
        messageQueue = new SynchronizedQueue();
        messenger = new Messenger(MonitorIP, messageQueue);
    }

    public void start(){
        thread = new Thread(this);
        thread.start();
        messenger.start();
    }

    public void pause(){
        messenger.pause();
        paused = true;
    }

    public void resume(){
        paused = false;
        notify();
        messenger.resume();
    }

    public void stop(){
        running = false;
        paused = false;
        messenger.stop();
        notify();
    }

    public void AddRequirement(Requirement requirement){
        Requirements.add(requirement);
    }

    private boolean TestRequirements(){
        return !Requirements.stream().anyMatch(x -> !x.test());
    }

    public void AddMonitorData(TimingMonitorData MonitorData){
        messageQueue.Add(MonitorData);
    }

    @Override
    public void run() {
        while(running){
            while(paused){
                try {
                    thread.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if(TestRequirements()){
                messenger.resume();
            } else {
                messenger.pause();
            }

            try {
                thread.wait(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
