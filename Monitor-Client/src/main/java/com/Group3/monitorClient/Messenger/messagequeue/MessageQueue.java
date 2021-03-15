package com.Group3.monitorClient.Messenger.messagequeue;

import com.Group3.monitorClient.Messenger.QueueInterface;
import com.Group3.monitorClient.Messenger.SecureSynchronizedQueue;

import java.sql.*;

public class MessageQueue implements QueueInterface<MessageInterface> {
    private final SQLManager sqlManager;

    public MessageQueue(String url, String fileName) {
        sqlManager = new SQLManager(url, fileName);
        if (!sqlManager.CheckIfExists("queue")) {
            sqlManager.CreateNewTable("queue", "ID int PRIMARY KEY AUTOINCREMENT", "SenderID integer NOT NULL", "Timestamp text NOT NULL", "message BLOB");
        }
    }

    @Override
    public void put(MessageInterface data) {

    }

    @Override
    public MessageInterface take() {
        return null;
    }
}
