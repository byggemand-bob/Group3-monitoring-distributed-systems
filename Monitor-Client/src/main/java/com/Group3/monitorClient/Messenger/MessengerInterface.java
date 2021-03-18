package com.Group3.monitorClient.Messenger;

import com.Group3.monitorClient.Messenger.messages.MessageInterface;
import org.openapitools.client.model.TimingMonitorData;

public interface MessengerInterface extends Runnable {
    void Start();

    void Stop();

    void Pause();

    void Resume();

    void AddMessage(MessageInterface message);
}
