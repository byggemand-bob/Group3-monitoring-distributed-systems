package com.group3.monitorClient.messenger;

import com.group3.monitorClient.controller.MonitorClientInterface;
import com.group3.monitorClient.messenger.messages.ErrorDataMessage;
import com.group3.monitorClient.messenger.queue.QueueInterface;
import com.group3.monitorClient.messenger.messages.MessageCreator;
import com.group3.monitorClient.messenger.messages.MessageInterface;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.MonitorApi;
import org.openapitools.client.model.ErrorData;
import org.threeten.bp.OffsetDateTime;

/*
 * The GreedyMessenger class runs a continues thread sending TimingMonitorData from a SynchronizedQueue.
 * It Utilizes the MonitorApi and will indefinably probe the queue until stopped or paused.
 */
public class GreedyMessenger implements MessengerInterface {
    protected MonitorClientInterface monitorClientInterface;
    private boolean running = true;
    private boolean paused = false;
    private final QueueInterface<MessageInterface> messageQueue;
    private final MessageCreator messageCreator = new MessageCreator();
    private Thread thread;
    private long senderID = 1L;//TODO: add real sender id instead of dummy data

    /*
     * specifies which SynchronizedQueue to utilize,
     * useful if multiple messengers should share the same queue.
     */
    public GreedyMessenger(String monitorIP, QueueInterface<MessageInterface> messageQueue){
        monitorClientInterface = new MonitorClientInterface(monitorIP);
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

    /* Adds a message to the message-queue */
    @Override
    public void AddMessage(MessageInterface message){
        messageQueue.Put(message);
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
        int statusCode = -1;

        if (message != null) {
            /*
             * Attempts to send a given message 10 times until successful,
             * Unless unable to connect to the server, in which case it loops indefinitely
             */
            int loop = 0;
            boolean socketTimeout = false;
            ErrorData errorData = null;

            do{
                boolean errorOccurrence = false;
                try {
                    statusCode = message.send(monitorClientInterface);
                } catch (ApiException e){
                    String exceptionString = e.toString();

                    if(exceptionString.contains("java.net.SocketTimeoutException")){
                        errorOccurrence = true;
                        System.out.println("Cannot connect to monitor server, waiting 5 seconds and retrying");
                        ThreadWait(5000);
                        loop--; //if the messenger can't connect to MonitorServer it will loop indefinitely until connection is established.

                        if (!socketTimeout) {
                            socketTimeout = true;
                            errorData = new ErrorData();
                            errorData.setTimestamp(OffsetDateTime.now());
                            errorData.setSenderID(senderID);
                            errorData.setErrorMessageType(ErrorData.ErrorMessageTypeEnum.NOCONNECTION);
                            errorData.setComment("No connection to monitor server - start: " + errorData.getTimestamp().toString() + "\n");
                        }
                    } else {
                        e.printStackTrace();
                    }
                }

                if (socketTimeout && !errorOccurrence) {
                    errorData.setComment(errorData.getComment() + "Connection re-established: " + OffsetDateTime.now().toString());
                    MessageInterface errorDataMessage = messageCreator.MakeMessage(errorData);
                    messageQueue.Put(errorDataMessage);
                    socketTimeout = false;
                }

                loop++;
                if(!running || paused){ break; }
            } while(statusCode >= 200 && statusCode < 300 && loop < 10);

            CheckResponse(statusCode); //after 10 loops of none 200 response codes, check Response;
        } else {
            ThreadWait(5000);
        }
    }



    /* Checks the Response of message.send() and takes the appropriate action */
    private void CheckResponse(int response){
        if(response >= 200 && response < 300){
            messageQueue.Delete();
        } else {
            ErrorData errorData = new ErrorData();
            if(response == -1){
                if(running && !paused){ //TODO: change - preferably figure out what unknown error is supposed to be
                    //A non-SocketTimeoutException was thrown when sending the message
                    errorData.setTimestamp(OffsetDateTime.now());
                    errorData.setSenderID(senderID);
                    errorData.setErrorMessageType(ErrorData.ErrorMessageTypeEnum.UNKNOWNERROR);
                    MessageInterface errorDataMessage = messageCreator.MakeMessage(errorData);
                    messageQueue.Put(errorDataMessage);
                }
            } else {
                //if a non-200 http response is gotten - add it to the message
                errorData.setTimestamp(OffsetDateTime.now());
                errorData.setSenderID(senderID);
                errorData.setErrorMessageType(ErrorData.ErrorMessageTypeEnum.HTTPERROR);
                errorData.setHttpResponse(response);
                MessageInterface errorDataMessage = messageCreator.MakeMessage(errorData);
                messageQueue.Put(errorDataMessage);
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
