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
	
	/**
	 * Configures the built-in scheduled tasks of the monitoring framework.
	 * 
	 * @throws TaskAlreadyExistsExecption Thrown if a task with the same name has already been scheduled.
	 * @throws SchedulerException
	 */
	public void ConfigureBuiltInTasks() throws TaskAlreadyExistsExecption, SchedulerException {
		CheckIfInitialized();
		CleanUpOldFailedMessagesTask cleanupMessageTask = new CleanUpOldFailedMessagesTask(CleanUpOldFailedMessagesTask.class.getName(), "cleanup", "0 0 22 ? * * *");
		ScheduleTask(cleanupMessageTask);
	}
	
	/**
	 * Schedules a new task in the TaskScheduler.
	 * 
	 * @param task The {@link AbstractTask} that is to be scheduled.
	 * @throws TaskAlreadyExistsExecption Thrown if a task with the same name has already been scheduled.
	 * @throws SchedulerException
	 */
	public void ScheduleTask(AbstractTask task) throws TaskAlreadyExistsExecption, SchedulerException {
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

	/**
	 * Creates a {@link JobDetail} instance from the given Task.
	 * 
	 * @param task The {@link AbstractTask} used to create the Quartz {@link JobDetail}.
	 * @return An instance of {@link JobDetail} which is used to schedule the job.
	 */
	public JobDetail CreateJobForTask(AbstractTask task) {
		return JobBuilder.newJob(task.getClass())
				.withIdentity(task.GetName(), task.GetGroup())
				.build();
	}
	
	/**
	 * Enables a disabled Task in the TaskScheduler.
	 * 
	 * @param taskName The name of the task to enable.
	 * @return true if task is successfully enabled, false otherwise.
	 */
	public boolean EnableTask(String taskName) {
		CheckIfInitialized();
		if (!tasks.containsKey(taskName)) {
			return false;
		}
		
		//Get the task details using the task name and assign it to a variable
		TaskDetails taskDetails = tasks.get(taskName);
		try {
			Trigger newTrigger = CreateTriggerForTask(taskDetails.GetTask());
			// Reschedule the job instead of resume to stop the job executing multiple times when resuming
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
	
	/**
	 * Disables a Task in the TaskScheduler.
	 * 
	 * @param taskName The name of the task to disable.
	 * @return true if task is successfully disabled, false otherwise.
	 */
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
	
	/**
	 * Checks whether the task with the given name is enabled.
	 * 
	 * @param taskName The name of the task to check.
	 * @return true if the task exist and is enabled, false otherwise.
	 */
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
	
	/**
	 * Shutdown the scheduler for good.
	 * This is to be run on termination.
	 * 
	 * @throws SchedulerException
	 */
	public void Shutdown() throws SchedulerException {
		CheckIfInitialized();
		scheduler.shutdown();
	}
	
	/**
	 * Creates a new {@link Trigger} based on the {@link AbstractTask} provided.
	 * 
	 * @param task The {@link AbstractTask} used to create the {@link Trigger}.
	 * @return A new {@link Trigger} instance.
	 */
	public Trigger CreateTriggerForTask(AbstractTask task) {
		String triggerName = "cronTrigger4" + task.GetName();
		return TriggerBuilder.newTrigger()
		.withIdentity(triggerName, task.GetGroup())
		.withSchedule(CronScheduleBuilder.cronSchedule(task.GetCron()))
		.build();
	}
	
	/**
	 * Checks whether the TaskScheduler has been initialized before any scheduling methods are called.
	 */
	private void CheckIfInitialized() {
		if (tasks == null || scheduler == null) {
			throw new NotInitializedException("Method \"Initialize()\" have to be called before <" + MonitorUtils.GetCallingMethodName(3) + "> on " + this.getClass().getSimpleName());
		}
	}
}
