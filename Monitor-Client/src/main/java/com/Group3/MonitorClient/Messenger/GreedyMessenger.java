package com.Group3.monitorClient.Messenger;

import com.Group3.monitorClient.Messenger.messageQueue.MessageCreator;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.ApiResponse;
import org.openapitools.client.api.MonitorApi;
import org.openapitools.client.model.TimingMonitorData;

/*
 * The GreedyMessenger class runs a continues thread sending TimingMonitorData from a SynchronizedQueue.
 * It Utilizes the MonitorApi and will indefinably probe the queue until stopped or paused.
 */
public class GreedyMessenger implements Messenger{
    protected MonitorApi monitorClient;
    private boolean running = true;
    private boolean paused = false;
    private QueueInterface<MessageInterface> messageQueue;
    private MessageCreator messageCreator = new MessageCreator();
    private Thread thread;

    /*
     * specifies which SynchronizedQueue to utilize,
     * useful if multiple messengers should share the same queue.
     */
    public GreedyMessenger(String monitorIP, QueueInterface<MessageInterface> messageQueue){
        ApiClient client = new ApiClient();
        client.setBasePath(monitorIP);
        monitorClient = new MonitorApi(client);
        this.messageQueue = messageQueue;
    }

//    public GreedyMessenger(String monitorIP){
//        ApiClient client = new ApiClient();
//        client.setBasePath(monitorIP);
//        monitorClient = new MonitorApi(client);
//        messageQueue = new QueueInterface<TimingMonitorData>();
//    }

    /* starts a thread running current class.run() */
    @Override
    public void Start(){
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    /* This terminates the thread, whoever it completes the current loop*/
    @Override
    public void Stop(){
        running = false;
        paused = false;
        synchronized(this) {
            notify();
        }
    }

    /* pauses the thread, however completing the current loop before doing so */
    @Override
    public void Pause(){
        paused = true;
    }

    /* resumes the thread */
    @Override
    public void Resume(){
        paused = false;
        synchronized(this) {
            notify();
        }
    }

    /* Adds monitorData to the monitor queue, to be sent later when requirements allow. */
    @Override
    public void AddMonitorData(TimingMonitorData monitorData){
        messageQueue.Put(messageCreator.MakeMessage(monitorData));
    }

    public boolean MessengerIsAlive(){
        return thread.isAlive();
    }

    /* while running continues to take and send MonitorData while the SynchronizedQueue is not empty */
    @Override
    public void run() {
        RunningLoop: while (running) {
            while (paused) {
                try {
                    synchronized(this) {
                        wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(!running){ break RunningLoop; }
            }

            MessageInterface message = messageQueue.Take();

            ApiResponse<Void> Response = null;

            if (message != null) {
                try {
                    int Loop = 0;
                    do{
                        Response = message.send(monitorClient);
                        Loop++;
                    } while((Response == null || Response.getStatusCode() != 200) && Loop < 10);

                    if(Response == null || Response.getStatusCode() != 200){
                        //TODO: add handling if the response wasn't successfully send
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    synchronized(this){
                        wait(5000);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
