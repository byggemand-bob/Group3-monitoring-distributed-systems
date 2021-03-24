package com.group3.monitorClient.messenger.messages;

import org.openapitools.client.model.TimingMonitorData;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageCreator {


    /* receives a ResultSet, representing an message and converts it back into a message structure */
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

    /* Converts TimingMonitorData into a Message format */
    public MessageInterface MakeMessage(TimingMonitorData timingMonitorData){
        return new TimingMonitorDataMessage(timingMonitorData);
    }

    /* converts a sqlQuery representing a TimingMonitorData and reconstructs it into a message format */
    private MessageInterface CreateTimingMonitorData(ResultSet sqlQuery) {
        TimingMonitorData timingMonitorData = new TimingMonitorData();
        MessageInterface message = null;
        try {
            timingMonitorData.setSenderID(sqlQuery.getLong("SenderID"));

            String dateTimeString = sqlQuery.getString("Timestamp");
            timingMonitorData.setTimestamp(ConvertStringToDateTime(dateTimeString));

            String blob = sqlQuery.getString("Message");
            String[] blobSplit = blob.split(MessageInterface.Separator);

            timingMonitorData.setTargetEndpoint(blobSplit[0]);

            timingMonitorData.setEventID(Long.valueOf(blobSplit[1]));

            message = MakeMessage(timingMonitorData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return message;
    }

    /* Converts a given string into a Datetime data-structure */
    private OffsetDateTime ConvertStringToDateTime(String string){
        return OffsetDateTime.parse(string, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
