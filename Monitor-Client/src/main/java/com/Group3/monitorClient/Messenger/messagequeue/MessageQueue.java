package com.Group3.monitorClient.Messenger.messageQueue;

import com.Group3.monitorClient.Messenger.MessageInterface;
import com.Group3.monitorClient.Messenger.QueueInterface;

import java.sql.ResultSet;

public class MessageQueue implements QueueInterface<MessageInterface> {
    private final MessageCreator messageCreator = new MessageCreator();
    private final SQLManager sqlManager;

    public MessageQueue(String url, String fileName) {
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
    public void put(MessageInterface data) {
        data.MakeSQL(sqlManager);
    }

    @Override
    public MessageInterface take() {
        ResultSet rs = sqlManager.SelectMessage("queue");
        return messageCreator.CreateMessageFromSQL(rs);
    }

    public void CloseConnection () {
        sqlManager.CloseConnection();
    }
}
