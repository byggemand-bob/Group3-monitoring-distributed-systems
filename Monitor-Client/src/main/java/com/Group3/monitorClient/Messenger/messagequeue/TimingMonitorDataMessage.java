package com.Group3.monitorClient.Messenger.messageQueue;

import com.Group3.monitorClient.Messenger.MessageInterface;
import org.openapitools.client.ApiException;
import org.openapitools.client.ApiResponse;
import org.openapitools.client.api.MonitorApi;
import org.openapitools.client.model.TimingMonitorData;

public class TimingMonitorDataMessage implements MessageInterface {
    private TimingMonitorData timingMonitorData;
    private int messageTypeID;

    public TimingMonitorDataMessage(TimingMonitorData timingMonitorData, int messageTypeID){
        this.timingMonitorData = timingMonitorData;
        this.messageTypeID = messageTypeID;
    }

    @Override
    public ApiResponse<Void> send(MonitorApi MonitorClient) {
        try {
            return MonitorClient.addMonitorDataWithHttpInfo(timingMonitorData);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void MakeSQL(SQLManager sqlManager) {
        String blob = timingMonitorData.getTargetEndpoint() + "," + timingMonitorData.getEventID();
        sqlManager.InsertMessage("queue", timingMonitorData.getSenderID(), messageTypeID, timingMonitorData.getTimestamp().toString(), blob);
    }

    public int getMessageTypeID () {
        return this.messageTypeID;
    }

    public TimingMonitorData getTimingMonitorData () {
        return this.timingMonitorData;
    }
}
