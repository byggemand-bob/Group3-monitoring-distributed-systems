package com.Group3.MonitorClient.Messenger.LazyMessenger;

import com.Group3.MonitorClient.Messenger.LazyMessenger.Requirements.Requirement;
import com.Group3.MonitorClient.Messenger.Messenger;
import com.Group3.MonitorClient.Messenger.GreedyMessenger;
import com.Group3.MonitorClient.Messenger.SynchronizedQueue;
import org.openapitools.client.model.TimingMonitorData;

import java.util.LinkedList;
import java.util.List;

/*
 * The LazyMessenger is a class to handle the Messenger class.
 * It periodically checks Requirements added to the class,
 * then pauses or resumes the Messenger based on how the requirements tests
 */
public class LazyMessenger implements Messenger {
    protected Messenger subMessenger;
    private SynchronizedQueue<TimingMonitorData> messageQueue;
    private List<Requirement> requirementList = new LinkedList<Requirement>();
    private boolean running = true;
    private boolean paused = false;
    private Thread thread;

    public LazyMessenger(String monitorIP){
        messageQueue = new SynchronizedQueue<TimingMonitorData>();
        subMessenger = new GreedyMessenger(monitorIP, messageQueue);
    }

    /*
     * specifies which SynchronizedQueue to utilize,
     * useful if multiple messengers should share the same queue.
     */
    public LazyMessenger(String monitorIP, SynchronizedQueue<TimingMonitorData> messageQueue){
        subMessenger = new GreedyMessenger(monitorIP, messageQueue);
        this.messageQueue = messageQueue;
    }

    /* Returns number of messages in messageQueue */
    public int QueueSize(){
        return messageQueue.Size();
    }

    /* starts a thread running current class.run() */
    @Override
    public void Start(){
        running = true;
        subMessenger.Pause();
        subMessenger.Start();
        thread = new Thread(this);
        thread.start();
    }

    /*
     * Pauses the current thread as well as the messenger,
     * however both the thread and the messenger completes their current loops first.
     */
    @Override
    public void Pause(){
        subMessenger.Pause();
        paused = true;
    }

    /* resumes the thread as well as the messenger*/
    @Override
    public void Resume(){
        paused = false;
        synchronized (this) {
            notify();
        }
    }

    /* This terminates the thread, whoever it completes the current loop*/
    @Override
    public void Stop(){
        running = false;
        paused = false;
        subMessenger.Stop();
        synchronized (this) {
            notify();
        }
    }

    /*
     * Adds class to the list of requirements needed to pass for the Messenger to run.
     * The class must implement to Requirement interface to work
     */
    public void AddRequirement(Requirement requirement){
        requirementList.add(requirement);
    }

    /* Adds monitorData to the monitor queue, to be sent later when requirements allow. */
    @Override
    public void AddMonitorData(TimingMonitorData monitorData){
        messageQueue.Add(monitorData);
    }

    /*
     * Tests every requirement in the requirementList.
     * Returns true if everything tests true, else false
     */
    private boolean TestRequirements(){
        if(requirementList.size() > 0) {
            return requirementList.stream().allMatch(x -> x.test());
        } else {
            return true;
        }
    }

    public boolean MessengerIsAlive(){
        return thread.isAlive();
    }

    /* while running continues to test the requirementList, and pauses or resumes the Messenger */
    @Override
    public void run() {
        while(running){
            while(paused){
                try {
                    synchronized (this) {
                        wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(!running){
                    break;
                }
            }

            if(TestRequirements()){
                subMessenger.Resume();
            } else {
                subMessenger.Pause();
            }

            try {
                synchronized (this) {
                    wait(5000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
