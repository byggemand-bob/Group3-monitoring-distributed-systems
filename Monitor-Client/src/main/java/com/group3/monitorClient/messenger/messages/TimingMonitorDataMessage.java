package com.group3.monitorClient.messenger.messages;

import com.group3.monitorClient.controller.MonitorClientInterface;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.TimingMonitorData;

public class TimingMonitorDataMessage implements MessageInterface {
    private TimingMonitorData timingMonitorData;
    private int messageTypeID;

    public TimingMonitorDataMessage(TimingMonitorData timingMonitorData){
        this.timingMonitorData = timingMonitorData;
        this.messageTypeID = MessageTypeID.TimingMonitorData.ordinal();
    }

    /* Message sends itself using the given MonitorClientInterface */
    @Override
    public int send(MonitorClientInterface monitorClientInterface) throws ApiException {
            return monitorClientInterface.addMonitorData(timingMonitorData);
    }

    /* the message converts itself into an sql format, and saves itself using provided SQLManager */
    @Override
    public void MakeSQL(SQLManager sqlManager) {
        String blob = timingMonitorData.getTargetEndpoint() + separator + timingMonitorData.getEventID();
        sqlManager.InsertMessage("queue", timingMonitorData.getSenderID(), messageTypeID, timingMonitorData.getTimestamp().toString(), blob);
    }

    public int getMessageTypeID () {
        return this.messageTypeID;
    }

    public TimingMonitorData getTimingMonitorData () {
        return this.timingMonitorData;
    }
}
