package com.Group3.monitorClient.messenger;

import com.Group3.monitorClient.messenger.messages.MessageInterface;

public interface MessengerInterface extends Runnable {
    void Start();

    void Stop();

    void Pause();

    void Resume();

    void AddMessage(MessageInterface message);
}
