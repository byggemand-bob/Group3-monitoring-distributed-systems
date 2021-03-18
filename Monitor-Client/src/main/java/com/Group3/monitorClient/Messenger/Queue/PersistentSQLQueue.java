package com.Group3.monitorClient.Messenger.Queue;

import com.Group3.monitorClient.Messenger.messages.MessageInterface;
import com.Group3.monitorClient.Messenger.messages.MessageCreator;
import com.Group3.monitorClient.Messenger.messages.SQLManager;

import java.sql.ResultSet;

public class PersistentSQLQueue implements QueueInterface<MessageInterface> {
    private final MessageCreator messageCreator = new MessageCreator();
    private final SQLManager sqlManager;

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

    @Override
    public void Put(MessageInterface data) {
        data.MakeSQL(sqlManager);
    }

    @Override
    public MessageInterface Take() {
        ResultSet rs = sqlManager.SelectFirst("queue");
        return messageCreator.CreateMessageFromSQL(rs);
    }

    @Override
    public void Delete() {
        sqlManager.DeleteFirstMessage("queue");
    }

    @Override
    public int Size() {
        return sqlManager.TableSize("queue");
    }

    public void CloseConnection () {
        sqlManager.CloseConnection();
    }
}
