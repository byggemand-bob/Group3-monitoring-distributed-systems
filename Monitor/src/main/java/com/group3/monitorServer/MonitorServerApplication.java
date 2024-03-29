package com.group3.monitorServer;

import com.group3.monitorClient.scheduling.TaskScheduler;
import org.quartz.SchedulerException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MonitorServerApplication {

	private static TaskScheduler scheduler;
	
	public static void main(String[] args) throws SchedulerException {
		SpringApplication.run(MonitorServerApplication.class, args);
		scheduler = new TaskScheduler();
		scheduler.Initialize();
	}
}
