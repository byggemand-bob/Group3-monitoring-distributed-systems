package com.group3.monitorServer.controller.queue;

import com.group3.monitorServer.controller.messages.MessageCreator;
import com.group3.monitorServer.controller.messages.MessageInterface;
import com.group3.monitorServer.controller.messages.SQLManagerOLD;

import java.sql.ResultSet;

/*
 * This class serves as a persistent queue, utilizing sqlite to ensure
 * messages are preserved even in the event of system failure
 */
public class PersistentSQLQueue implements QueueInterface<MessageInterface> {
    private final MessageCreator messageCreator = new MessageCreator();
    private final SQLManagerOLD sqlManagerOLD;

    /* Checks if the queue table exists in the specified database, otherwise creates it */
    public PersistentSQLQueue(String url, String fileName) {
        sqlManagerOLD = new SQLManagerOLD(url, fileName);
        if (!sqlManagerOLD.CheckIfExists("queue")) {
            sqlManagerOLD.CreateNewTable("queue",
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
        data.MakeSQL(sqlManagerOLD);
    }

    /* Takes the first element in the queue from the sql */
    @Override
    public MessageInterface Take() {
        if (Size() != 0) {
            ResultSet rs = sqlManagerOLD.SelectFirstMessage("queue");
            return messageCreator.MakeMessageFromSQL(rs);
        } else {
            return null;
        }
    }

    @Override
    public ResultSet Take(int Number) {
        return sqlManagerOLD.SelectMessages("queue", Number);
    }

    @Override
    public ResultSet TakeAll() {
        return sqlManagerOLD.SelectAllMessages("queue");
    }

    @Override
    public void DeleteID(Long ID) {
        sqlManagerOLD.DeleteByID("queue", ID);
    }

    /* Deletes the first message in the Queue */
    @Override
    public synchronized void Delete() {
        sqlManagerOLD.DeleteFirstMessage("queue");
        if(sqlManagerOLD.TableSize("queue") == 0){
            sqlManagerOLD.ResetAutoIncrement("queue");
        }
    }

    /* returns the number of messages in the queue */
    @Override
    public synchronized int Size() {
        return sqlManagerOLD.TableSize("queue", "ToBeSent = 1");
    }

    @Override
    public synchronized void Failed() {
        sqlManagerOLD.ChangeStatusOfFirstToBeSentElement("queue");
    }

    @Override
    public synchronized MessageInterface TakeFailed() {
        return messageCreator.MakeMessageFromSQL(sqlManagerOLD.SelectFirstFailedMessage("queue"));
    }

    @Override
    public synchronized int SizeFailed() {
        return sqlManagerOLD.TableSize("queue", "ToBeSent = 0");
    }

    @Override
    public synchronized void DeleteFailed() {
        sqlManagerOLD.DeleteFirstFailedMessage("queue");
    }

    @Override
    public synchronized void DeleteAllFailed() {
        sqlManagerOLD.DeleteAllFailedMessages("queue");
    }

    /* Closes the queues connection to the sql database */
    public synchronized void CloseConnection () {
        sqlManagerOLD.CloseConnection();
    }

    public synchronized String getPath () {
        return sqlManagerOLD.getPath();
    }

    public synchronized String getFileName () {
        return sqlManagerOLD.getFileName();
    }
}