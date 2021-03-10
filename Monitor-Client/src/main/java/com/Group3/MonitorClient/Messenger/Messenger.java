package com.Group3.monitorClient.Messenger;

import org.openapitools.client.model.TimingMonitorData;

public interface Messenger extends Runnable {
    void Start();

    void Stop();

    void Pause();

    void Resume();

    void AddMonitorData(TimingMonitorData monitorData);
}
