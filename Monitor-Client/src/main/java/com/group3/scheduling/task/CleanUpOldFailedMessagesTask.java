package com.group3.scheduling.task;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.group3.monitorClient.configuration.ConfigurationManager;
import com.group3.monitorClient.messenger.messages.SQLManager;
import com.group3.monitorClient.messenger.queue.PersistentSQLQueue;

public class CleanUpOldFailedMessagesTask extends AbstractTask {

	private final String cleanupSQL = "DELETE FROM queue WHERE ToBeSent = 0 AND datetime(Timestamp) < datetime('now', ?)";
	
	public CleanUpOldFailedMessagesTask(String name, String group, String cron) {
		super(name, group, cron);
	}
	
	public CleanUpOldFailedMessagesTask() {
		// intentionally left empty, as it is needed in order to allow for scheduling of Task
	}
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("Started cleanup of old failed messages...");
		
		int daysToKeep = ConfigurationManager.getInstance().getPropertyAsInteger(ConfigurationManager.daysToKeepMessages);
		
		// Ensure that daysToKeep do not go below zero
		daysToKeep = Math.max(0, daysToKeep);
		
		PersistentSQLQueue sqlQueue = new PersistentSQLQueue(PersistentSQLQueue.queueDBPath, PersistentSQLQueue.queueDBFile);
		int removedMessagesCount = sqlQueue.cleanupOldMessages(cleanupSQL, daysToKeep);		
		
		if (removedMessagesCount > -1) {
			System.out.println("Finished cleanup of old failed message, removed <" + removedMessagesCount + "> old messages...");			
		} else {
			System.err.println("Failed in running cleanup of old failed messages!!!");
		}
		
		// Always remember to close connection
		sqlQueue.CloseConnection();
	}

}
