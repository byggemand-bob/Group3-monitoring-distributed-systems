package com.Group3.monitorClient.Messenger.messagequeue;

import com.Group3.monitorClient.Messenger.MessageInterface;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.MonitorApi;
import org.openapitools.client.model.TimingMonitorData;

public class TimingMonitorDataMessage implements MessageInterface {
    TimingMonitorData timingMonitorData;
    int messageTypeID;

    public TimingMonitorDataMessage(TimingMonitorData timingMonitorData, int messageTypeID){
        this.timingMonitorData = timingMonitorData;
        this.messageTypeID = messageTypeID;
    }

    @Override
    public void send(MonitorApi MonitorClient) {
        try {
            MonitorClient.addMonitorData(timingMonitorData);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void MakeSQL(SQLManager sqlManager) {
        String blob = timingMonitorData.getTargetEndpoint() + "," + timingMonitorData.getEventID();
        sqlManager.InsertMessage(timingMonitorData.getSenderID(), messageTypeID, timingMonitorData.getTimestamp().toString(), blob);
    }
}
