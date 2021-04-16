package com.group3.monitorServer.MessageProcessor;

import com.group3.monitorServer.controller.Controllable;
import org.openapitools.model.TimingMonitorData;
import com.group3.monitorServer.messages.SQLMessageManager;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Delegator implements Controllable {

    private final SQLMessageManager sqlMessageManager;
    private final MessageCreator messageCreator = new MessageCreator();

    public Delegator(SQLMessageManager sqlMessageManager) {
        this.sqlMessageManager = sqlMessageManager;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void run() {

    }

    private TimingMonitorDataMessage findTimingDataMatch (TimingMonitorDataMessage message) {
        String[] whereArgs = new String[2];
        TimingMonitorData.EventCodeEnum eventCodeEnum = matchingEventCode(message.getTimingMonitorData().getEventCode());
        String blob = message.getTimingMonitorData().getTargetEndpoint() + TimingMonitorDataMessage.separator +
                message.getTimingMonitorData().getEventID() + TimingMonitorDataMessage.separator +
                eventCodeEnum.ordinal();
        whereArgs[0] = "Message = '" + blob + "'";
        whereArgs[1] = "SenderID = '" + message.getTimingMonitorData().getSenderID() + "'";
        ResultSet resultSetQuery = sqlMessageManager.SelectMessage(whereArgs);
        try {
            if (resultSetQuery != null && resultSetQuery.next()) {
                //TODO: analyze TimingMessage
                TimingMonitorDataMessage result = (TimingMonitorDataMessage) messageCreator.MakeMessageFromSQL(resultSetQuery);
                if (!resultSetQuery.next()) {
                    return result;
                } else {
                    System.out.println("");
                }
            } else {
                System.out.println("resultsetquery is null");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    private TimingMonitorData.EventCodeEnum matchingEventCode (TimingMonitorData.EventCodeEnum eventCodeEnum) {
        switch (eventCodeEnum.toString()) {
            case "SendRequest":
                return TimingMonitorData.EventCodeEnum.RECEIVERESPONSE;
            case "ReceiveRequest":
                return TimingMonitorData.EventCodeEnum.SENDRESPONSE;
            case "SendResponse":
                return TimingMonitorData.EventCodeEnum.RECEIVEREQUEST;
            case "ReceiveResponse":
                return TimingMonitorData.EventCodeEnum.SENDREQUEST;
            default:
                throw new IllegalStateException("Unexpected value of event code enum: " + eventCodeEnum.toString());
        }
    }
}
