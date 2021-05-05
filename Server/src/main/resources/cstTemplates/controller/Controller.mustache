package com.group3.monitorClient.controller;

import com.group3.monitorClient.controller.requirements.Requirement;

import java.util.LinkedList;
import java.util.List;

public class Controller implements Runnable{
    private List<Requirement> requirementList = new LinkedList<>();
    private boolean running = true;
    private List<Controllable> ControlledThreads = new LinkedList<>();

    /*
     * Tests every requirement in the requirementList.
     * Returns true if everything tests true, else false
     */
    private boolean TestRequirements(){
        if(requirementList.size() > 0) {
            return requirementList.stream().allMatch(Requirement::Test);
        } else {
            return true;
        }
    }

    public void addRequirement(Requirement requirement){
        requirementList.add(requirement);
    }

    public void addThread (Controllable thread) {
        ControlledThreads.add(thread);
    }

    /* while running continues to test the requirementList, and pauses or resumes the Messenger */
    @Override
    public void run() {
        ControlledThreads.forEach(Controllable::start);

        RunningLoop: while(running){
        //TODO: Decide if Controllers should be Controllable
//            while(paused){
//                ThreadWait(0);
//                if(!running){
//                    break RunningLoop;
//                }
//            }

            if(TestRequirements()){
                ControlledThreads.forEach(Controllable::resume);
            } else {
                ControlledThreads.forEach(Controllable::pause);
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

    /* starts a thread running current class.run() */
    public void start(){
        running = true;
        new Thread(this).start();
    }

    public void stop(){
        ControlledThreads.forEach(Controllable::stop);
        running = false;
        synchronized(this) {
            notify();
        }
    }
}
