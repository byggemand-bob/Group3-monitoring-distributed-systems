package com.Group3.monitorClient.Messenger.messages;

import org.openapitools.client.ApiException;
import org.openapitools.client.api.MonitorApi;
import org.openapitools.client.model.TimingMonitorData;

public class TimingMonitorDataMessage implements MessageInterface {
    private TimingMonitorData timingMonitorData;
    private int messageTypeID;

    public TimingMonitorDataMessage(TimingMonitorData timingMonitorData, int messageTypeID){
        this.timingMonitorData = timingMonitorData;
        this.messageTypeID = messageTypeID;
    }

    /* Message sends itself using the given MonitorAPI */
    @Override
    public int send(MonitorApi MonitorClient) throws ApiException {
            return MonitorClient.addMonitorDataWithHttpInfo(timingMonitorData).getStatusCode();
    }

    /* the message converts itself into an sql format, and saves itself using provided SQLManager */
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
