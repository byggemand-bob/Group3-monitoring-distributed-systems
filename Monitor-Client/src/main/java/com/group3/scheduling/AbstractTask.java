package com.group3.scheduling;

import org.quartz.Job;

public abstract class AbstractTask implements Job {
	
	private String name;
	private String group;
	private String cron;
	
	public AbstractTask(String name, String group, String cron) {
		this.name = name;
		this.group = group;
		this.cron = cron;
	}
	
	public AbstractTask() {
		//Default constructor is needed in order to run the task scheduled
		//The same is true for all subclasses.
	}
	
	public String GetName() {
		return name;
	}
	public String GetGroup() {
		return group;
	}
	public String GetCron() {
		return cron;
	}
}