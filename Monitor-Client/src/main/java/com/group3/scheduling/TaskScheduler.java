package com.group3.scheduling;

import java.util.HashMap;
import java.util.Map;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.group3.monitorClient.util.MonitorUtils;
import com.group3.scheduling.task.AbstractTask;
import com.group3.scheduling.task.CleanUpOldFailedMessagesTask;

/**
 * A functionality wrapper for the Quartz {@link Scheduler}.
 * Provides functionality for configuring and scheduling tasks.
 *
 */
public class TaskScheduler {
	
	private Map<String, TaskDetails> tasks;
	private Scheduler scheduler;
	
	/**
	 * Initializes the TaskScheduler.
	 * This needs to be run before any configuring or scheduling of tasks can be done.
	 * 
	 * Also automatically schedules built-in tasks.
	 * 
	 * @throws SchedulerException
	 */
	public void Initialize() throws SchedulerException {
		tasks = new HashMap<String, TaskDetails>();
		scheduler = new StdSchedulerFactory().getScheduler();
		scheduler.start();
		ConfigureBuiltInTasks();
	}
	
	public void ConfigureBuiltInTasks() throws TaskAlreadyExistsExecption, SchedulerException {
		CheckIfInitialized();
		CleanUpOldFailedMessagesTask cleanupMessageTask = new CleanUpOldFailedMessagesTask(CleanUpOldFailedMessagesTask.class.getName(), "cleanup", "0 0 22 ? * * *");
		ScheduleTask(cleanupMessageTask);
	}
	
	public void ScheduleTask(AbstractTask task) throws SchedulerException, TaskAlreadyExistsExecption {
		CheckIfInitialized();
		if(tasks.containsKey(task.GetName())) {
			throw new TaskAlreadyExistsExecption("Task with name " + task.GetName() + ", have already been scheduled");
		}
		
		//create a job based on task
		JobDetail job = CreateJobForTask(task);
		//make a trigger for the job
		Trigger trigger = CreateTriggerForTask(task);
		//schedule job with trigger
		scheduler.scheduleJob(job, trigger);
		//Create TaskDetails for task
		TaskDetails taskDetails = new TaskDetails(task, job.getKey(), trigger.getKey());
		//set enabled to true
		taskDetails.SetEnabled(true);
		//save TaskDetails in hashMap
		tasks.put(task.GetName(), taskDetails);
	}

	public JobDetail CreateJobForTask(AbstractTask task) {
		return JobBuilder.newJob(task.getClass())
				.withIdentity(task.GetName(), task.GetGroup())
				.build();
	}
	
	public boolean EnableTask(String taskName) {
		CheckIfInitialized();
		if (!tasks.containsKey(taskName)) {
			return false;
		}
		
		//Get the task details using the task name and assign it to a variable
		TaskDetails taskDetails = tasks.get(taskName);
		try {
			Trigger newTrigger = CreateTriggerForTask(taskDetails.GetTask());
			scheduler.rescheduleJob(taskDetails.GetTriggerKey(), newTrigger);
			taskDetails.SetTriggerKey(newTrigger.getKey());
		} catch (SchedulerException e) {
			System.err.println("The following execption for task " + taskName + ", while trying to resume the task");
			e.printStackTrace();
			return false;
		}
		
		taskDetails.SetEnabled(true);
		return true;
	}
	
	public boolean DisableTask(String taskName) {
		CheckIfInitialized();
		if (!tasks.containsKey(taskName)) {
			return false;
		}
		
		//Get the task details using the task name and assign it to a variable
		TaskDetails taskDetails = tasks.get(taskName);
		
		try {
			scheduler.pauseJob(taskDetails.GetJobKey());
		} catch (SchedulerException e) {
			System.err.println("The following execption for task " + taskName + ", while trying to pause the task");
			e.printStackTrace();
			return false;
		}
		
		taskDetails.SetEnabled(false);
		return true;
	}
	
	public boolean IsTaskEnabled(String taskName) {
		CheckIfInitialized();
		//If the map does not contain the task name return false
		if (!tasks.containsKey(taskName)) {
			return false;
		}
		
		//Get the task details using the task name and assign it to a variable
		TaskDetails taskDetails = tasks.get(taskName);
		//Return taskDetails boolean
		return taskDetails.IsEnabled();
	}
	
	public void Shutdown() throws SchedulerException {
		CheckIfInitialized();
		scheduler.shutdown();
	}
	
	public Trigger CreateTriggerForTask(AbstractTask task) {
		String triggerName = "cronTrigger4" + task.GetName();
		return TriggerBuilder.newTrigger()
		.withIdentity(triggerName, task.GetGroup())
		.withSchedule(CronScheduleBuilder.cronSchedule(task.GetCron()))
		.build();
	}
	
	private void CheckIfInitialized() {
		if (tasks == null || scheduler == null) {
			throw new NotInitializedException("Method \"Initialize()\" have to be called before <" + MonitorUtils.GetCallingMethodName(3) + "> on " + this.getClass().getSimpleName());
		}
	}
}
