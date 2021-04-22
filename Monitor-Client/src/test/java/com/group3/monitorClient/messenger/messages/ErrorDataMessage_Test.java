package com.group3.monitorClient.messenger.messages;

import com.group3.monitorClient.AbstractSQLMessageManagerTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openapitools.client.model.ErrorData;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ErrorDataMessage_Test extends AbstractSQLMessageManagerTest {
    /* verifies the ErrorDataMessage is able to save itself in an sql database */
    @Test
    public void testMakeSQLPass () throws SQLException {
        //setup
        ErrorDataMessage errorDataMessage = getDefaultErrorMessage();
        ErrorData errorData = errorDataMessage.getErrorData();

        long senderID_test = -9999L;
        String timeStamp_test = "";
        int messageTypeID_test = -9999;
        int httpResponse_test = -9999;
        String comment_test = "";
        ErrorData.ErrorMessageTypeEnum errorMessageType_test = null;

        //Act
        errorDataMessage.makeSQL(sqlMessageManager);
        ResultSet rs = sqlMessageManager.SelectFirstMessage();

        String blob = rs.getString("Message");
        httpResponse_test = Integer.parseInt(blob.split(MessageInterface.separator)[0]);
        errorMessageType_test = ErrorData.ErrorMessageTypeEnum.values()[Integer.parseInt(blob.split(MessageInterface.separator)[1])];
        comment_test = blob.split(MessageInterface.separator)[2];
        senderID_test = rs.getLong("SenderID");
        timeStamp_test = rs.getString("Timestamp");
        messageTypeID_test = rs.getInt("MessageType");

        //Assert
        Assertions.assertEquals(errorData.getSenderID(), senderID_test);
        Assertions.assertEquals(errorData.getHttpResponse(), httpResponse_test);
        Assertions.assertEquals(errorData.getTimestamp().toString(), timeStamp_test);
        Assertions.assertEquals(MessageTypeID.ErrorData.ordinal(), messageTypeID_test);
        Assertions.assertEquals(errorData.getComment(), comment_test);
        Assertions.assertEquals(errorData.getErrorMessageType(), errorMessageType_test);
    }
}
