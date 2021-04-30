package com.group3.monitorClient.scheduling.task;

import com.group3.monitorClient.messenger.Messenger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.group3.monitorClient.configuration.ConfigurationManager;
import com.group3.monitorClient.messenger.messages.SQLMessageManager;

public class CleanUpOldFailedMessagesTask extends AbstractTask {

	public static final String cleanupWhere = "datetime(Timestamp) < datetime('now', '-%d day')";
	
	/**
	 * Constructor for the CleanUpOldFailedMessagesTask.
	 * 
	 * @param name The name of the task.
	 * @param group The group the task belongs to.
	 * @param cron The cron expression used to specify the execution schedule of the task.
	 * 
	 * @see AbstractTask
	 */
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
		final String whereClause = generateWhereClause(daysToKeep);
		
		SQLMessageManager failedMessageManager = Messenger.getSqlFailedMessageManager();
		int removedMessagesCount = failedMessageManager.Delete(whereClause);
		
		if (removedMessagesCount > -1) {
			System.out.println("Finished cleanup of old failed message, removed <" + removedMessagesCount + "> old messages...");			
		} else {
			System.err.println("Failed in running cleanup of old failed messages!!!");
		}
	}
	
	public String generateWhereClause(int daysToKeep) {
		daysToKeep = Math.max(0, daysToKeep);
		return String.format(cleanupWhere, daysToKeep);
	}

}
