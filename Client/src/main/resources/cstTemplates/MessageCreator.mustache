package com.group3.monitorClient.messenger.messages;

import org.openapitools.client.model.ErrorData;
import org.openapitools.client.model.TimingMonitorData;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageCreator {
    /* receives a ResultSet, representing an message and converts it back into a message structure */
    public MessageInterface MakeMessageFromSQL(ResultSet rs){
        int TypeID = -1;
        try {
            TypeID = rs.getInt("MessageType");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(TypeID == MessageTypeID.TimingMonitorData.ordinal()){
            return CreateTimingMonitorData(rs);
        } else if (TypeID == MessageTypeID.ErrorData.ordinal()){
            return CreateErrorData(rs);
        }

        return null;
    }

    /* Converts TimingMonitorData into a Message format */
    public MessageInterface MakeMessage(TimingMonitorData timingMonitorData){
        return new TimingMonitorDataMessage(timingMonitorData);
    }

    /* Converts ErrorData into a Message format */
    public MessageInterface MakeMessage(ErrorData errorData) {
        return new ErrorDataMessage(errorData);
    }

    /* converts a resultSet representing a TimingMonitorData and reconstructs it into a message format */
    private MessageInterface CreateTimingMonitorData(ResultSet rs) {
        TimingMonitorData timingMonitorData = new TimingMonitorData();
        MessageInterface message = null;
        try {
            timingMonitorData.setSenderID(rs.getLong("SenderID"));

            String dateTimeString = rs.getString("Timestamp");
            timingMonitorData.setTimestamp(ConvertStringToDateTime(dateTimeString));

            String blob = rs.getString("Message");
            String[] blobSplit = blob.split(MessageInterface.separator);

            timingMonitorData.setTargetEndpoint(blobSplit[0]);

            timingMonitorData.setEventID(Long.valueOf(blobSplit[1]));

            timingMonitorData.setEventCode(TimingMonitorData.EventCodeEnum.values()[Integer.parseInt(blobSplit[2])]);

            message = MakeMessage(timingMonitorData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return message;
    }

    /* converts a resultSet representing a ErrorData and reconstructs it into a message format */
    private MessageInterface CreateErrorData(ResultSet rs) {
        ErrorData errorData = new ErrorData();
        MessageInterface message = null;
        try {
            errorData.setSenderID(rs.getLong("SenderID"));
            String dateTimeString = rs.getString("Timestamp");
            errorData.setTimestamp(ConvertStringToDateTime(dateTimeString));

            String blob = rs.getString("Message");
            String[] blobSplit = blob.split(MessageInterface.separator);

            if(!blobSplit[0].contains("null")){
                errorData.setHttpResponse(Integer.parseInt(blobSplit[0]));
            }


            errorData.setErrorMessageType(ErrorData.ErrorMessageTypeEnum.values()[Integer.parseInt(blobSplit[1])]);

            errorData.setComment(blobSplit[2]);

            message = MakeMessage(errorData);
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
