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

public class TaskScheduler {
	
	private Map<String, TaskDetails> tasks;
	private Scheduler scheduler;
	
	public void Initialize() throws SchedulerException {
		tasks = new HashMap<String, TaskDetails>();
		scheduler = new StdSchedulerFactory().getScheduler();
		scheduler.start();
		ConfigureBuiltInTasks();
	}
	
	public void ConfigureBuiltInTasks() {
		//TODO:Add clean up built in task
	}
	
	public void ScheduleTask(AbstractTask task) throws SchedulerException, TaskAlreadyExistsExecption {
		if(tasks.containsKey(task.GetName())) {
			throw new TaskAlreadyExistsExecption("Task with name " + task.GetName() + ", have already been scheduled");
		}
		
		//create a job based on task
		JobDetail job = JobBuilder.newJob(task.getClass())
				.withIdentity(task.GetName(), task.GetGroup())
				.build();		
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
	
	public boolean EnableTask(String taskName) {
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
		//If the map does not contain the task name return false
		if (!tasks.containsKey(taskName)) {
			return false;
		}
		
		//Get the task details using the task name and assign it to a variable
		TaskDetails taskDetails = tasks.get(taskName);
		//Return taskDetails boolean
		return taskDetails.GetEnabled();
	}
	
	public void Shutdown() throws SchedulerException {
		scheduler.shutdown();
	}
	
	public Trigger CreateTriggerForTask(AbstractTask task) {
		String triggerName = "cronTrigger4" + task.GetName();
		return TriggerBuilder.newTrigger()
		.withIdentity(triggerName, task.GetGroup())
		.withSchedule(CronScheduleBuilder.cronSchedule(task.GetCron()))
		.build();
	}
}
