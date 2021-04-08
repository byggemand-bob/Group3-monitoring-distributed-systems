package com.group3.scheduling.examples;

import java.util.Date;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.group3.scheduling.task.AbstractTask;

public class PrintEvery10secondsTask extends AbstractTask{

	public PrintEvery10secondsTask(String name, String group, String cron) {
		super(name, group, cron);
	}
	
	public PrintEvery10secondsTask() {}
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("PrintEvery10secondsTask --->>> Goodbye cruel world, Time is " + new Date());
	}

}
