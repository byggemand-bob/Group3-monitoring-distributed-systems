package com.group3.monitorServer.controller.messages;

import org.openapitools.model.TimingMonitorData;

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
    public int send(MonitorClientInterface monitorClientInterface) throws Exception {
        throw new Exception("MessageInterface.send() was called. This function should never be called on the monitorServer");
    }

    /* the message converts itself into an sql format, and saves itself using provided SQLManager */
    @Override
    public void MakeSQL(SQLMessageManager sqlMessageManager) {
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

        sqlMessageManager.InsertMessage(timingMonitorData.getSenderID(), messageTypeID, timingMonitorData.getTimestamp().toString(), blob);
    }

    public int getMessageTypeID () {
        return this.messageTypeID;
    }

    public TimingMonitorData getTimingMonitorData () {
        return this.timingMonitorData;
    }
}
