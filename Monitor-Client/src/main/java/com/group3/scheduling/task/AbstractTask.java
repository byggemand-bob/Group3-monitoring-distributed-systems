package com.group3.scheduling.task;

import org.quartz.Job;

/**
 * Abstract class which all tasks to be scheduled using the {@link TaskScheduler} need to extend.
 */
public abstract class AbstractTask implements Job {
	
	private String name;
	private String group;
	private String cron;
	
	/**
	 * Constructor for an AbstractTask.
	 * 
	 * @param name The name of the task.
	 * @param group The group which the task belong to.
	 * @param cron The cron expression used to specify the execution schedule of the task.
	 */
	public AbstractTask(String name, String group, String cron) {
		this.name = name;
		this.group = group;
		this.cron = cron;
	}
	
	public AbstractTask() {
		//Default constructor is needed in order to run the task scheduled
		//The same is true for all subclasses.
	}
	
	/**
	 * Get the name of the task.
	 * 
	 * @return The name of the task.
	 */
	public String GetName() {
		return name;
	}
	
	/**
	 * Get the group the task belongs to.
	 * 
	 * @return The name of the group the task belong to.
	 */
	public String GetGroup() {
		return group;
	}
	
	/**
	 * Get the cron expression for the task.
	 * 
	 * @return The cron expression as a string.
	 */
	public String GetCron() {
		return cron;
	}
}