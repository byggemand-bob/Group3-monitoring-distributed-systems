package com.group3.monitorClient.scheduling.examples;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.group3.monitorClient.scheduling.task.AbstractTask;

public class PrintEvery5secondsTask extends AbstractTask {

	public PrintEvery5secondsTask(String name, String group, String cron) {
		super(name, group, cron);
	}
	
	public PrintEvery5secondsTask() {}

	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("PrintEvery5secondsTask --->>> Hello world, Time is " + new Date());
	}

}
