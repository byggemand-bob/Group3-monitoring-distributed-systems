package com.group3.monitorClient.messenger.messages;

import com.group3.monitorClient.controller.MonitorClientInterface;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.TimingMonitorData;
import org.threeten.bp.OffsetDateTime;

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
        //TODO: maybe change to something unique to tell that it was not specified.
        if (timingMonitorData.getSenderID() == null) {
            timingMonitorData.setSenderID(-1L);
        }

        String timestamp = "";

        if (timingMonitorData.getTimestamp() == null) {
            timestamp = "not specified";

        } else {
            timestamp = timingMonitorData.getTimestamp().toString();
        }

        sqlManager.InsertMessage("queue", timingMonitorData.getSenderID(), messageTypeID, timestamp, blob);
    }

    public int getMessageTypeID () {
        return this.messageTypeID;
    }

    public TimingMonitorData getTimingMonitorData () {
        return this.timingMonitorData;
    }
}
