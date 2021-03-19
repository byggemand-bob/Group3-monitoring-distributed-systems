package com.Group3.monitorClient.Messenger.Queue;

import com.Group3.monitorClient.Messenger.messages.MessageInterface;
import com.Group3.monitorClient.Messenger.messages.MessageCreator;
import com.Group3.monitorClient.Messenger.messages.SQLManager;

import java.sql.ResultSet;

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
                    "Message BLOB");
        }
    }

    /* puts the given message into the sql database */
    @Override
    public void Put(MessageInterface data) {
        data.MakeSQL(sqlManager);
    }

    /* Takes the first element in the queue from the sql */
    @Override
    public MessageInterface Take() {
        ResultSet rs = sqlManager.SelectFirst("queue");
        return messageCreator.CreateMessageFromSQL(rs);
    }

    /* Deletes the first message in the Queue */
    @Override
    public void Delete() {
        sqlManager.DeleteFirstMessage("queue");
        if(sqlManager.TableSize("queue") == 0){
            sqlManager.ResetAutoIncrement("queue");
        }
    }

    /* returns the number of messages in the queue */
    @Override
    public int Size() {
        return sqlManager.TableSize("queue");
    }

    /* Closes the queues connection to the sql database */
    public void CloseConnection () {
        sqlManager.CloseConnection();
    }
}
