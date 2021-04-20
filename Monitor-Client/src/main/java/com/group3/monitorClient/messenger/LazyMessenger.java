package com.group3.monitorClient.messenger;

import com.group3.monitorClient.controller.requirements.Requirement;
import com.group3.monitorClient.messenger.messages.MessageInterface;

import java.util.LinkedList;
import java.util.List;

/*
 * The LazyMessenger is a class to handle the Messenger class.
 * It periodically checks Requirements added to the class,
 * then pauses or resumes the Messenger based on how the requirements tests
 */

// TODO: Refactor using Controller
public class LazyMessenger implements MessengerInterface {
    protected MessengerInterface subMessenger;
    private List<Requirement> requirementList = new LinkedList<Requirement>();
    private boolean running = true;
    private boolean paused = false;
    private Thread thread;

    /*
     * specifies which SynchronizedQueue to utilize,
     * useful if multiple messengers should share the same queue.
     */
    public LazyMessenger(String monitorIP, String sqlPath, String sqlFileName){
        subMessenger = new GreedyMessenger(monitorIP, sqlPath, sqlFileName);
    }

    /* starts a thread running current class.run() */
    @Override
    public void start(){
        running = true;
        subMessenger.pause();
        subMessenger.start();
        thread = new Thread(this);
        thread.start();
    }

    /*
     * Pauses the current thread as well as the messenger,
     * however both the thread and the messenger completes their current loops first.
     */
    @Override
    public void pause(){
        subMessenger.pause();
        paused = true;
    }

    /* resumes the thread as well as the messenger*/
    @Override
    public void resume(){
        paused = false;
        synchronized (this) {
            notify();
        }
    }

    /* This terminates the thread, whoever it completes the current loop*/
    @Override
    public void stop(){
        running = false;
        paused = false;
        subMessenger.stop();
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

    /* Adds a message to the message-queue, to be sent later when requirements allow. */
    @Override
    public void AddMessage(MessageInterface message){
        subMessenger.AddMessage(message);
    }

    @Override
    public long MessageQueueSize() {
        return subMessenger.MessageQueueSize();
    }

    /*
     * Tests every requirement in the requirementList.
     * Returns true if everything tests true, else false
     */
    private boolean TestRequirements(){
        if(requirementList.size() > 0) {
            return requirementList.stream().allMatch(x -> x.Test());
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
        RunningLoop: while(running){
            while(paused){
                ThreadWait(0);
                if(!running){
                    break RunningLoop;
                }
            }

            if(TestRequirements()){
                subMessenger.resume();
            } else {
                subMessenger.pause();
            }

            ThreadWait(5000);
        }
    }

    /* waits for specified amount of MilliSeconds if 0, waits until another calls thread.notify() */
    private void ThreadWait(int MilliSeconds){
        try {
            synchronized(this){
                wait(MilliSeconds);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
