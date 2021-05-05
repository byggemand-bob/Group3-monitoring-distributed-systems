package com.group3.monitorClient;

import com.group3.monitorClient.messenger.messages.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openapitools.client.model.ErrorData;
import org.openapitools.client.model.TimingMonitorData;
import org.threeten.bp.OffsetDateTime;

import java.io.File;

public class AbstractSQLMessageManagerTest extends AbstractMonitorTest {
    public static SQLMessageManager sqlMessageManager;// = new SQLManager("src/main/resources/sqlite/db/", "test.db");
    public static SQLMessageManager sqlFailedMessageManager;

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
        sqlMessageManager = new SQLMessageManager(sqlManager,SQLMessageManager.message_table_name);
        sqlFailedMessageManager = new SQLMessageManager(sqlManager, SQLMessageManager.failed_message_table_name);
    }

    /* Deletes the sql database after each test */
    @AfterEach
    public void cleanUp() {
        File db = new File(getSQLPath() + getSQLFileName());
        sqlMessageManager.CloseConnection();
        sqlFailedMessageManager.CloseConnection();

        if (!db.delete()) {
            System.out.println(this.getClass().toString() + ".AbstractSQLMessageManagerTest: Could not access database file. It was therefore not deleted!");
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
        errorData.setErrorMessageType(ErrorData.ErrorMessageTypeEnum.HTTPERROR);
        errorData.setSenderID(100L);
        errorData.setTimestamp(OffsetDateTime.now());
        errorData.setComment("test");
        errorData.setHttpResponse(200);
        return new ErrorDataMessage(errorData);
    }

    public String getSQLPath(){
        return sqlMessageManager.getPath();
    }

    public String getSQLFileName(){
        return sqlMessageManager.getFileName();
    }

    public String getSQLTableName(){
        return sqlMessageManager.getTableName();
    }
}
