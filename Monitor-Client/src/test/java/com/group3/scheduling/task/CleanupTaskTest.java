package com.group3.scheduling.task;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.openapitools.client.model.ErrorData;
import org.openapitools.client.model.ErrorData.ErrorMessageTypeEnum;
import org.openapitools.client.model.TimingMonitorData;
import org.openapitools.client.model.TimingMonitorData.EventCodeEnum;
import java.time.OffsetDateTime;

import com.group3.monitorClient.AbstractSQLMessageManagerTest;
import com.group3.monitorClient.messenger.messages.MessageCreator;
import com.group3.monitorClient.scheduling.task.CleanUpOldFailedMessagesTask;

public class CleanupTaskTest extends AbstractSQLMessageManagerTest {
	
    /* Ensures that the cleanupOldMessages method works as it could. 
     * 	Delete the correct amount of messages with the specified DaysToKeep int
     */
    @Test
    void CleanUpOfOldMessagesTest() {
    	//Setup
    	TimingMonitorData monitorData = new TimingMonitorData();
    	MessageCreator messageCreator = new MessageCreator();
    	ErrorData errorData = new ErrorData();
    	
    	EmptyQueue();
    	
    	monitorData.setEventID(1L);
    	monitorData.setTargetEndpoint("/monitorClient");
    	monitorData.setSenderID(3L);
    	monitorData.setEventCode(EventCodeEnum.SENDREQUEST);
    	
    	OffsetDateTime offsetDateTime = OffsetDateTime.now().minusDays(31);
    	monitorData.setTimestamp(offsetDateTime);
    	messageCreator.MakeMessage(monitorData).makeSQL(sqlFailedMessageManager);
    	
    	offsetDateTime = OffsetDateTime.now().minusDays(12);
    	monitorData.setTimestamp(offsetDateTime);
    	messageCreator.MakeMessage(monitorData).makeSQL(sqlFailedMessageManager);
    	
    	errorData.setSenderID(1L);
    	errorData.setHttpResponse(410);
    	errorData.errorMessageType(ErrorMessageTypeEnum.HTTPERROR);
  
    	offsetDateTime = OffsetDateTime.now().minusDays(8);
    	errorData.setTimestamp(offsetDateTime);
    	messageCreator.MakeMessage(errorData).makeSQL(sqlFailedMessageManager);
    	
    	offsetDateTime = OffsetDateTime.now().minusDays(48);
    	errorData.setTimestamp(offsetDateTime);
    	messageCreator.MakeMessage(errorData).makeSQL(sqlFailedMessageManager);
    	
    	CleanUpOldFailedMessagesTask cleanupTask = new CleanUpOldFailedMessagesTask();
    	
    	//Act
    	final long expectedSize = 2;
    	final int daysToKeep = 30;
        final String whereClause = cleanupTask.generateWhereClause(daysToKeep);
        
        sqlFailedMessageManager.Delete(whereClause);
       
    	final long SizeAfter = sqlFailedMessageManager.TableSize();
    	
    	//Assert
    	assertEquals(expectedSize, SizeAfter);
    }
    
    private void EmptyQueue() {
    	sqlMessageManager.ResetTable();
    }
}
