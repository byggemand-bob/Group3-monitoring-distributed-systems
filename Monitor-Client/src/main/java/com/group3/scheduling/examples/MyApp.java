package com.group3.scheduling.examples;

import com.group3.scheduling.TaskScheduler;

public class MyApp {

	public static void main(String[] args) {
		try {
			
			TaskScheduler taskScheduler = new TaskScheduler();
			taskScheduler.Initialize();
			
			PrintEvery5secondsTask job = new PrintEvery5secondsTask("PrintEvery5Sec", "group1", "0/5 * * * * ?");
			PrintEvery10secondsTask job1 = new PrintEvery10secondsTask("PrintEvery10Sec", "group1", "3/10 * * * * ?");
			
			taskScheduler.ScheduleTask(job);
			taskScheduler.ScheduleTask(job1);
			
			Thread.sleep(10000);
			taskScheduler.DisableTask(job.GetName());
			System.out.println("disabled job");
			
			Thread.sleep(20000);
			taskScheduler.EnableTask(job.GetName());
			System.out.println("enabled job");
			
			Thread.sleep(100000);
			taskScheduler.Shutdown();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}