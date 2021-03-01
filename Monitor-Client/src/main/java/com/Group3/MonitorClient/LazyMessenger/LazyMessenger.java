package com.Group3.MonitorClient.LazyMessenger;

import com.Group3.MonitorClient.LazyMessenger.Requirements.Requirement;
import org.openapitools.client.model.TimingMonitorData;

import java.util.List;

/*
* The LazyMessenger is a class to handle the Messenger class.
* It periodically checks Requirements added to the class,
* then pauses or resumes the Messenger based on how the requirements tests
*/
public class LazyMessenger implements Runnable {
    private Messenger messenger;
    private SynchronizedQueue messageQueue;
    private List<Requirement> requirementList;
    private boolean running = true;
    private boolean paused = false;
    Thread thread;

    public LazyMessenger(String MonitorIP){
        messageQueue = new SynchronizedQueue();
        messenger = new Messenger(MonitorIP, messageQueue);
    }

    /* starts a thread running current class.run() */
    public void Start(){
        thread = new Thread(this);
        thread.start();
        messenger.Start();
    }

    /*
    * Pauses the current thread as well as the messenger,
    * however both the thread and the messenger completes their current loops first.
    */
    public void Pause(){
        messenger.Pause();
        paused = true;
    }

    /* resumes the thread as well as the messenger*/
    public void Resume(){
        paused = false;
        notify();
        messenger.Resume();
    }

    /* This terminates the thread, whoever it completes the current loop*/
    public void Stop(){
        running = false;
        paused = false;
        messenger.Stop();
        notify();
    }

    /*
    * Adds class to the list of requirements needed to pass for the Messenger to run.
    * The class must implement to Requirement interface to work
    */
    public void AddRequirement(Requirement requirement){
        requirementList.add(requirement);
    }

    /*
    * Tests every requirement in the requirementList.
    * Returns true if everything tests true, else false
    */
    private boolean TestRequirements(){
        return requirementList.stream().allMatch(x -> x.test());
    }

    /* Adds MonitorData to the monitor queue, to be sent later when requirements allow. */
    public void AddMonitorData(TimingMonitorData MonitorData){
        messageQueue.Add(MonitorData);
    }

    /* while running continues to test the requirementList, and pauses or resumes the Messenger */
    @Override
    public void run() {
        while(running){

            if(TestRequirements()){
                messenger.Resume();
            } else {
                messenger.Pause();
            }

            try {
                thread.wait(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            while(paused){
                try {
                    thread.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
