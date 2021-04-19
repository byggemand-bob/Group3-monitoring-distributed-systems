package com.group3.monitorServer;

import com.group3.monitorServer.messages.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openapitools.model.ErrorData;
import org.openapitools.model.TimingMonitorData;

import java.io.File;
import java.time.OffsetDateTime;

public class AbstractSQLManagerTest {
    public static SQLManager sqlManager;// = new SQLManager("src/main/resources/sqlite/db/", "test.db");



    @BeforeAll
    static void setupDir() {
        File path = new File("src/main/resources/sqlite/db/");
        path.mkdirs();

        File db = new File("src/main/resources/sqlite/db/test.db");
        if (db.delete()) {
            System.out.println("there was already a test.db at location, and it was deleted");
        }
    }

    /* Creates a new sql database before each test */
    @BeforeEach
    public void setup () {
        SQLManager sqlManager = SQLManager.getInstance();
        sqlManager.Connect("src/main/resources/sqlite/db/", "test.db");
    }

    /* Deletes the sql database after each test */
    @AfterEach
    public void cleanUp() {
        File db = new File(getSQLPath() + getSQLFileName());
        sqlManager.CloseConnection();

        if (!db.delete()) {
            System.out.println(this.getClass().toString() + ".AbstractSQLManagerTest: Could not access database file. It was therefore not deleted!");
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
        return sqlManager.getPath();
    }

    public String getSQLFileName(){
        return sqlManager.getFileName();
    }
}