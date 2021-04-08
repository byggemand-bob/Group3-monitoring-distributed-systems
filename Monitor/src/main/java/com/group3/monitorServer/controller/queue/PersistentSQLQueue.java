package com.group3.monitorServer.controller.queue;

import com.group3.monitorServer.controller.messages.MessageCreator;
import com.group3.monitorServer.controller.messages.MessageInterface;
import com.group3.monitorServer.controller.messages.SQLManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

/*
 * This class serves as a persistent queue, utilizing sqlite to ensure
 * messages are preserved even in the event of system failure
 */
public class PersistentSQLQueue implements QueueInterface<MessageInterface> {
    private final MessageCreator messageCreator = new MessageCreator();
    private final SQLManager sqlManager;

    /* Checks if the queue table exists in the specified database, otherwise creates it */
    public PersistentSQLQueue(String url, String fileName) {
        sqlManager = new SQLManager(url, fileName);
        if (!sqlManager.CheckIfExists("queue")) {
            sqlManager.CreateNewTable("queue",
                    "ID integer PRIMARY KEY AUTOINCREMENT",
                    "MessageType integer NOT NULL",
                    "SenderID integer NOT NULL",
                    "Timestamp text NOT NULL",
                    "ToBeSent BOOLEAN DEFAULT 1",
                    "Message BLOB");
        }
    }

    /* puts the given message into the sql database */
    @Override
    public synchronized void Put(MessageInterface data) {
        data.MakeSQL(sqlManager);
    }

    /* Takes the first element in the queue from the sql */
    @Override
    public MessageInterface Take() {
        if (Size() != 0) {
            ResultSet rs = sqlManager.SelectFirstMessage("queue");
            return messageCreator.MakeMessageFromSQL(rs);
        } else {
            return null;
        }
    }

    @Override
    public ResultSet Take(int Number) {
        return sqlManager.SelectMessages("queue", Number);
    }

    @Override
    public ResultSet TakeAll() {
        return sqlManager.SelectAllMessages("queue");
    }

    @Override
    public void DeleteID(Long ID) {
        sqlManager.DeleteByID("queue", ID);
    }

    /* Deletes the first message in the Queue */
    @Override
    public synchronized void Delete() {
        sqlManager.DeleteFirstMessage("queue");
        if(sqlManager.TableSize("queue") == 0){
            sqlManager.ResetAutoIncrement("queue");
        }
    }

    /* returns the number of messages in the queue */
    @Override
    public synchronized int Size() {
        return sqlManager.TableSize("queue", "ToBeSent = 1");
    }

    @Override
    public synchronized void Failed() {
        sqlManager.ChangeStatusOfFirstToBeSentElement("queue");
    }

    @Override
    public synchronized MessageInterface TakeFailed() {
        return messageCreator.MakeMessageFromSQL(sqlManager.SelectFirstFailedMessage("queue"));
    }

    @Override
    public synchronized int SizeFailed() {
        return sqlManager.TableSize("queue", "ToBeSent = 0");
    }

    @Override
    public synchronized void DeleteFailed() {
        sqlManager.DeleteFirstFailedMessage("queue");
    }

    @Override
    public synchronized void DeleteAllFailed() {
        sqlManager.DeleteAllFailedMessages("queue");
    }

    /* Closes the queues connection to the sql database */
    public synchronized void CloseConnection () {
        sqlManager.CloseConnection();
    }

    public synchronized String getPath () {
        return sqlManager.getPath();
    }

    public synchronized String getFileName () {
        return sqlManager.getFileName();
    }
}
