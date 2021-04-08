package com.group3.monitorClient.messenger.messages;

import com.group3.monitorClient.controller.MonitorClientInterface;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.TimingMonitorData;

import java.util.ArrayList;
import java.util.List;

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
        List<String> errorMessages = new ArrayList<>();
        if (timingMonitorData.getEventID() == null) {
            errorMessages.add("EventID");
        }
        if (timingMonitorData.getTimestamp() == null) {
            errorMessages.add("Timestamp");
        }
        if (timingMonitorData.getSenderID() == null) {
            errorMessages.add("SenderID");
        }
        if (timingMonitorData.getEventCode() == null) {
            errorMessages.add("EventCode");
        }

        if (!errorMessages.isEmpty()) {
            String errorMessage = "TimingMonitorDataMessage did not contain the required fields\n";
            for (String msg : errorMessages) {
                errorMessage += msg + " was null\n";
            }
            throw new NullPointerException(errorMessage);
        }

        String blob = timingMonitorData.getTargetEndpoint() + separator + timingMonitorData.getEventID() + separator + timingMonitorData.getEventCode().ordinal();

        sqlManager.InsertMessage("queue", timingMonitorData.getSenderID(), messageTypeID, timingMonitorData.getTimestamp().toString(), blob);
    }

    public int getMessageTypeID () {
        return this.messageTypeID;
    }

    public TimingMonitorData getTimingMonitorData () {
        return this.timingMonitorData;
    }
}
