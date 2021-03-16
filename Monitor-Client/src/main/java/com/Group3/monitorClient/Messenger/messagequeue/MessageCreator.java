package com.Group3.monitorClient.Messenger.messageQueue;

import com.Group3.monitorClient.Messenger.MessageInterface;
import org.openapitools.client.model.TimingMonitorData;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageCreator {
    private enum MessageTypeID{
        TimingMonitorData
    }

    public MessageInterface CreateMessageFromSQL(ResultSet SQLQuery){
        int TypeID = -1;
        try {
            TypeID = SQLQuery.getInt("MessageType");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(TypeID == MessageTypeID.TimingMonitorData.ordinal()){
            return CreateTimingMonitorData(SQLQuery);
        }

        return null;
    }



    public MessageInterface MakeMessage(TimingMonitorData timingMonitorData){
        return new TimingMonitorDataMessage(timingMonitorData, MessageTypeID.TimingMonitorData.ordinal());
    }

    private MessageInterface CreateTimingMonitorData(ResultSet sqlQuery) {
        TimingMonitorData timingMonitorData = new TimingMonitorData();
        MessageInterface message = null;
        try {
            timingMonitorData.setSenderID(sqlQuery.getLong("SenderID"));

            String dateTimeString = sqlQuery.getString("Timestamp");
            timingMonitorData.setTimestamp(ConvertStringToDataTime(dateTimeString));

            String blob = sqlQuery.getBlob("Message").toString();
            String[] blobSplit = blob.split(",");

            timingMonitorData.setTargetEndpoint(blobSplit[0]);

            timingMonitorData.setEventID(Long.valueOf(blobSplit[1]));

            message = MakeMessage(timingMonitorData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return message;
    }

    private OffsetDateTime ConvertStringToDataTime(String string){
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        return OffsetDateTime.parse(string, formatter);
    }
}
