package com.group3.monitorClient.messenger;

import com.group3.monitorClient.MonitorClientInterface;
import com.group3.monitorClient.configuration.ConfigurationManager;
import com.group3.monitorClient.exception.MonitorConfigException;
import com.group3.monitorClient.messenger.messages.*;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.ErrorData;
import org.threeten.bp.OffsetDateTime;


import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.threeten.bp.format.DateTimeFormatter;

/*
 * The GreedyMessenger class runs a continues thread sending TimingMonitorData from a SynchronizedQueue.
 * It Utilizes the MonitorApi and will indefinably probe the queue until stopped or paused.
 */
public class Messenger implements MessengerInterface {
    private static Messenger messenger_instance = null;

    protected static MonitorClientInterface monitorClientInterface;
    private boolean running = true;
    private boolean paused = false;
    public static SQLMessageManager sqlMessageManager;
    private static SQLMessageManager sqlFailedMessageManager;
    private MessageCreator messageCreator = new MessageCreator();
    private Thread thread;
    private final long senderID = ConfigurationManager.getInstance().getPropertyAsLong(ConfigurationManager.IDProp);


    /*
     * specifies which SynchronizedQueue to utilize,
     * useful if multiple messengers should share the same queue.
     */
    public static void initialize(String sqlPath, String sqlFileName){
        SQLManager sqlManager = SQLManager.getInstance();
        sqlManager.Connect(sqlPath, sqlFileName);
        sqlMessageManager = new SQLMessageManager(sqlManager, SQLMessageManager.message_table_name);
        sqlFailedMessageManager = new SQLMessageManager(sqlManager, SQLMessageManager.failed_message_table_name);
        messenger_instance = new Messenger();
        monitorClientInterface = new MonitorClientInterface();
    }

    public static Messenger getInstance() {
        if (messenger_instance == null) {
            initialize(ConfigurationManager.getInstance().getProperty(ConfigurationManager.sqlPathProp, "src" + File.separator + "main" + File.separator + "resources" + File.separator + "sqlite" + File.separator + "db" + File.separator),
                       ConfigurationManager.getInstance().getProperty(ConfigurationManager.dbFileNameProp, "queue.db"));
        }
        return messenger_instance;
    }

    protected Messenger() { }

    /* starts a thread running current class.run() */
    @Override
    public void start(){
        running = true;
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    /* This terminates the thread, whoever it completes the current loop*/
    @Override
    public void stop(){
        running = false;
        paused = false;
        synchronized(this) {
            notify();
        }
    }

    /* pauses the thread, however completing the current loop before doing so */
    @Override
    public void pause(){
        paused = true;
    }

    /* resumes the thread */
    @Override
    public void resume(){
        paused = false;
        synchronized(this) {
            notify();
        }
    }

    /* Adds a message to the message-queue */
    @Override
    public void AddMessage(MessageInterface message){
        message.makeSQL(sqlMessageManager);
    }

    @Override
    public long MessageQueueSize() {
        return sqlMessageManager.TableSize();
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
    //TODO: Multi thread this?
    private void SendMessage(){
    	// Start by checking if any messages are available for sending
    	boolean availableMessages = sqlMessageManager.TableSize("InUse = 0") > 0;
    	if (!availableMessages) {
    		ThreadWait(5000);
    		return;
    	}
    	
        ResultSet firstMessageRS = sqlMessageManager.SelectFirstMessage("InUse = 0");
        MessageInterface message = messageCreator.MakeMessageFromSQL(firstMessageRS);

        try {
            sqlMessageManager.UpdateInUse(firstMessageRS.getInt("ID"), true);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if (message != null) {
            int loop = 0, statusCode = -1;
            boolean socketTimeout = false;
            ErrorData errorData = null;

            /*
             * Attempts to send a given message 10 times until successful,
             * Unless unable to connect to the server, in which case it loops indefinitely
             */
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

                        /* Create a noConnection error, but only one at a time */
                        if (!socketTimeout) {
                            socketTimeout = true;
                            errorData = new ErrorData();
                            errorData.setTimestamp(OffsetDateTime.now());
                            errorData.setSenderID(senderID);
                            errorData.setErrorMessageType(ErrorData.ErrorMessageTypeEnum.NOCONNECTION);
                            String timeNow = OffsetDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME);
                            errorData.setComment("No connection to monitor server - start: " + timeNow.substring(0, timeNow.length()-6));
                        }
                    } else {
                        try {
                            File dumpfile = new File("src/main/resources/dumpfile.txt");
                            PrintWriter pw = new PrintWriter(new FileOutputStream(dumpfile, true));

                            String timeNow = OffsetDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME);
                            pw.println(timeNow.substring(0, timeNow.length()-6));
                            if(e.getCode() != 0){ pw.println("http error code: " + e.getCode()); }
                            e.printStackTrace(pw);
                            pw.println();
                            pw.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                /* If a SocketTimeout occurred and connection is re-established, complete the noConnection message and queue it */
                if (socketTimeout && !errorOccurrence) {
                    String timeNow = OffsetDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME);
                    errorData.setComment(errorData.getComment() + ", Connection re-established: " + timeNow.substring(0, timeNow.length()-6));
                    MessageInterface errorDataMessage = messageCreator.MakeMessage(errorData);
                    errorDataMessage.makeSQL(sqlMessageManager);
                    socketTimeout = false;
                }

                loop++;
                if(!running || paused){ break; }
            } while(!(statusCode >= 200 && statusCode < 300) && loop < 10);

            try {
                CheckResponse(statusCode, message, firstMessageRS.getLong("ID")); //after 10 loops of none 200 response codes, check Response;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /* Checks the Response of message.send() and takes the appropriate action */
    private void CheckResponse(int response, MessageInterface message, long messageID){
        if(response >= 200 && response < 300){
            sqlMessageManager.Delete("ID = '" + messageID + "'");
        } else {
            message.makeSQL(sqlFailedMessageManager);
            sqlMessageManager.Delete("ID = '" + messageID + "'");
            if(!(message.getClass() == ErrorDataMessage.class)){
                ErrorData errorData = new ErrorData();
                if(response == -1){
                    if(running && !paused){ //TODO: change - preferably change from UnknownError to a defined error
                        //A non-SocketTimeoutException was thrown when sending the message
                        errorData.setTimestamp(OffsetDateTime.now());
                        errorData.setSenderID(senderID);
                        errorData.setErrorMessageType(ErrorData.ErrorMessageTypeEnum.UNKNOWNERROR);
                        MessageInterface errorDataMessage = messageCreator.MakeMessage(errorData);
                        errorDataMessage.makeSQL(sqlMessageManager);
                    }
                } else {
                    //if a non-200 http response is gotten - add it to the message
                    errorData.setTimestamp(OffsetDateTime.now());
                    errorData.setSenderID(senderID);
                    errorData.setErrorMessageType(ErrorData.ErrorMessageTypeEnum.HTTPERROR);
                    errorData.setHttpResponse(response);
                    MessageInterface errorDataMessage = messageCreator.MakeMessage(errorData);
                    errorDataMessage.makeSQL(sqlMessageManager);
                }
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

    public static MonitorClientInterface getMonitorClientInterface() {
        return monitorClientInterface;
    }

    public static SQLMessageManager getSqlMessageManager() {
        return sqlMessageManager;
    }

    public static SQLMessageManager getSqlFailedMessageManager() {
        return sqlFailedMessageManager;
    }

    public void CloseConnectionToSQL(){
        sqlMessageManager.CloseConnection();
    }
}
