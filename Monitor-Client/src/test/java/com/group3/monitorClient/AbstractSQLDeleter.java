package com.group3.monitorClient;

import com.group3.monitorClient.messenger.messages.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openapitools.client.model.ErrorData;
import org.openapitools.client.model.TimingMonitorData;
import org.threeten.bp.OffsetDateTime;

import java.io.File;

public class AbstractSQLDeleter {
    public String path = "src/main/resources/sqlite/db/";
    public String fileName = "test.db";

    @BeforeAll
    static void setupDir() {
        File path = new File("src/main/resources/sqlite/db/");
        path.mkdirs();

        File db = new File("src/main/resources/sqlite/db/test.db");
        if (db.delete()) {
            System.out.println("there was already a test.db at location, and it was deleted");
        }
    }

    /* Deletes the sql database after each test */
    @AfterEach
    public void cleanUp() {
        File db = new File(getSQLPath() + getSQLFileName());
        try {
            if (!db.delete()) {
                System.out.println(this.getClass().toString() + ".AbstractSQLDeleter: Could not access database file. It was therefore not deleted!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TimingMonitorDataMessage getDefaultTimingMessage(){
        TimingMonitorData timingMonitorData = new TimingMonitorData();
        timingMonitorData.setEventCode(TimingMonitorData.EventCodeEnum.RECEIVEREQUEST);
        timingMonitorData.setTimestamp(OffsetDateTime.now());
        timingMonitorData.setTargetEndpoint("/test");
        timingMonitorData.setEventID(99L);
        timingMonitorData.setSenderID(100L);

        MessageCreator messageCreator = new MessageCreator();

        MessageInterface timingMessage = messageCreator.MakeMessage(timingMonitorData);
        return (TimingMonitorDataMessage) timingMessage;
    }

    public ErrorDataMessage getDefaultErrorMessage(){
        ErrorData errorData = new ErrorData();
        errorData.setErrorMessageType(ErrorData.ErrorMessageTypeEnum.NOCONNECTION);
        errorData.setSenderID(100L);
        errorData.setTimestamp(OffsetDateTime.now());
        errorData.setComment("test");
        return new ErrorDataMessage(errorData);
    }

    public String getSQLPath(){
        return path;
    }

    public String getSQLFileName(){
        return fileName;
    }
}
