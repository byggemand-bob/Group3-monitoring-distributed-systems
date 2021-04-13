package com.group3.scheduling.testClasses;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.group3.scheduling.task.AbstractTask;

public class PrintEvery5SecondsTestTask extends AbstractTask {

	public PrintEvery5SecondsTestTask(String name, String group, String cron) {
		super(name, group, cron);
	}
	
	public PrintEvery5SecondsTestTask() {}
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("PrintEvery5secondsTask --->>> Hello world, Time is " + new Date());	
	}
}
