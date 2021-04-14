package com.group3.monitorClient.scheduling.examples;

import com.group3.monitorClient.scheduling.TaskScheduler;

public class MyApp {

	public static void main(String[] args) {
		try {
			
			TaskScheduler taskScheduler = new TaskScheduler();
			taskScheduler.Initialize();
			
			// Create two Task to be scheduled
			PrintEvery5secondsTask job = new PrintEvery5secondsTask("PrintEvery5Sec", "group1", "0/5 * * * * ?");
			PrintEvery10secondsTask job1 = new PrintEvery10secondsTask("PrintEvery10Sec", "group1", "3/10 * * * * ?");
			
			// Schedule the tasks
			taskScheduler.ScheduleTask(job);
			taskScheduler.ScheduleTask(job1);
			
			// Wait 10 seconds and disable a job
			Thread.sleep(10000);
			taskScheduler.DisableTask(job.GetName());
			System.out.println("disabled job: " + job.GetName());
			
			// Wait 20 seconds and enable the job again
			Thread.sleep(20000);
			taskScheduler.EnableTask(job.GetName());
			System.out.println("enabled job: " + job.GetName());
			
			// Wait 100 seconds and shutdown the scheduler
			Thread.sleep(100000);
			taskScheduler.Shutdown();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}