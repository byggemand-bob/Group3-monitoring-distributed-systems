package com.group3.monitorClient.messenger.messages;

import com.group3.monitorClient.controller.Controllable;
import com.group3.monitorClient.messenger.messages.MessageInterface;

public interface MessengerInterface extends Runnable, Controllable {
    void AddMessage(MessageInterface message);

    long MessageQueueSize();
}
