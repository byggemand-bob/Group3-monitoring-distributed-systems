package com.group3.monitorClient;

import org.quartz.SchedulerException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.group3.scheduling.TaskScheduler;

@SpringBootApplication
public class MonitorClientApplication {

	private static final TaskScheduler scheduler = new TaskScheduler();
	
	public static void main(String[] args) {
		SpringApplication.run(MonitorClientApplication.class, args);
		
		try {
			setupScheduler();
		} catch (SchedulerException e) {
			System.err.println("Setting up of scheduler caused the following Exception...");
			e.printStackTrace();
		}
	}
	
	private static void setupScheduler() throws SchedulerException {
		scheduler.Initialize();
	}
}
