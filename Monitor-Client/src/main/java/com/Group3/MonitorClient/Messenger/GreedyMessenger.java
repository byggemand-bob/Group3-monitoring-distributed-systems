package com.Group3.monitorClient.Messenger;

import com.Group3.monitorClient.Messenger.messageQueue.MessageCreator;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.ApiResponse;
import org.openapitools.client.api.MonitorApi;
import org.openapitools.client.model.TimingMonitorData;

import javax.annotation.Nullable;
import java.net.SocketTimeoutException;

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
                ThreadWait(0);
                if(!running){ break RunningLoop; }
            }

            SendMessage();
        }
    }

    /* Sends next message in the messageQueue */
    private void SendMessage(){
        MessageInterface message = messageQueue.Take();
        ApiResponse<Void> Response = null;

        if (message != null) {
            /*
             * Attempts to send a given message 10 times until successful,
             * Unless given a SocketTimeoutException, in which case it loops indefinitely
             */
            int Loop = 0;
            do{
                try {
                    Response = message.send(monitorClient);
                } catch (ApiException e){
                    String exceptionString = e.toString();

                    if(exceptionString.contains("java.net.SocketTimeoutException")){
                        System.out.println("Cannot connect to monitor server, waiting 5 seconds and retrying");
                        ThreadWait(5000);
                        Loop--; //if the messenger can't connect to MonitorServer it will loop indefinitely until connection is established.
                    } else {
                        e.printStackTrace();
                    }
                }
                Loop++;
                if(!running || paused){ break; }
            } while((Response == null || Response.getStatusCode() != 200) && Loop < 10);

            CheckResponse(Response); //after 10 loops of none 200 response codes, check Response;
        } else {
            ThreadWait(5000);
        }
    }

    /* Checks the Response of message.send() and takes the appropriate action */
    private void CheckResponse(@Nullable ApiResponse<Void> Response){
        if(Response != null && Response.getStatusCode() == 200){
            messageQueue.Delete();
        } else {
            //TODO: add handling if the response wasn't successfully send
            if(Response == null){
                if(running && !paused){
                    //A non SocketTimeoutException was thrown when sending the message
                }
            } else if(Response.getStatusCode() == 400){
                //if the last response code was 400
            }
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
