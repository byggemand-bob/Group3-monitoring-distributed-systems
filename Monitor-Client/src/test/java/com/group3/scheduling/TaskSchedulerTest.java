package com.group3.scheduling;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import com.group3.scheduling.task.AbstractTask;
import com.group3.scheduling.testClasses.PrintEvery5SecondsTestTask;

class TaskSchedulerTest {

	public static TaskScheduler taskScheduler = new TaskScheduler(); 
	
	@BeforeEach
	void Initialize() throws SchedulerException {
		taskScheduler.Initialize();
	}
	
	@AfterEach
	void TearDown() throws SchedulerException{
		taskScheduler.Shutdown();
	}
	
	AbstractTask ScheduleATestTask() throws TaskAlreadyExistsException, SchedulerException {
		PrintEvery5SecondsTestTask task = new PrintEvery5SecondsTestTask("PrintEvery5Sec", "group1", "0/5 * * * * ?");
		taskScheduler.ScheduleTask(task);
		return task;
	}
	
	@Test
	void TaskAlreadyExistsExceptionTest() {		
		Assertions.assertThrows(TaskAlreadyExistsException.class, () -> {
			taskScheduler.ConfigureBuiltInTasks();
		});
	}
	
	@Test
	void SchedulingATaskTest() throws TaskAlreadyExistsException, SchedulerException {
		AbstractTask task = ScheduleATestTask();
		assertTrue(taskScheduler.IsTaskEnabled(task.GetName()));
	}
	
	@Test
	void DisableAScheduledTaskTest() throws TaskAlreadyExistsException, SchedulerException {
		AbstractTask task = ScheduleATestTask();
		taskScheduler.DisableTask(task.GetName());
		assertFalse(taskScheduler.IsTaskEnabled(task.GetName()));
	}
	
	@Test
	void ReEnableASchedulingTaskTest() throws TaskAlreadyExistsException, SchedulerException {
		AbstractTask task = ScheduleATestTask();
		taskScheduler.EnableTask(task.GetName());
		assertTrue(taskScheduler.IsTaskEnabled(task.GetName()));
	}
	
	@Test
	void ScheduleATaskWhileSchedulerIsShutdownTest() throws SchedulerException {
		Assertions.assertThrows(SchedulerException.class, () -> {
			taskScheduler.Shutdown();
			ScheduleATestTask();
		});
	}
	
	@Test 
	void CreateJobForTaskTest() throws TaskAlreadyExistsException, SchedulerException {
		AbstractTask task = ScheduleATestTask();
		JobDetail jobDetail = taskScheduler.CreateJobForTask(task);
		String execpted = task.GetGroup() + "." + task.GetName();
		assertEquals(jobDetail.getKey().toString(), execpted);
		
	}
	
	@Test 
	void CreateTriggerForTaskTest() throws TaskAlreadyExistsException, SchedulerException {
		AbstractTask task = ScheduleATestTask();
		Trigger trigger = taskScheduler.CreateTriggerForTask(task);
		String execpted = task.GetGroup() + ".cronTrigger4" + task.GetName();
		assertEquals(trigger.getKey().toString(), execpted);		
	}
	
	@Test
	void NotInitializedExceptionTest() throws SchedulerException {
		TaskScheduler taskScheduler = new TaskScheduler();
		PrintEvery5SecondsTestTask task = new PrintEvery5SecondsTestTask("PrintEvery5Sec", "group1", "0/5 * * * * ?");
		
		Assertions.assertThrows(NotInitializedException.class, () -> {
			taskScheduler.ConfigureBuiltInTasks();
		});
		Assertions.assertThrows(NotInitializedException.class, () -> {
			taskScheduler.EnableTask(null);
		});
		Assertions.assertThrows(NotInitializedException.class, () -> {
			taskScheduler.DisableTask(null);
		});
		Assertions.assertThrows(NotInitializedException.class, () -> {
			taskScheduler.IsTaskEnabled(null);
		});
		Assertions.assertThrows(NotInitializedException.class, () -> {
			taskScheduler.ScheduleTask(null);
		});
		Assertions.assertThrows(NotInitializedException.class, () -> {
			taskScheduler.Shutdown();
		});
		Assertions.assertDoesNotThrow(() -> {
			taskScheduler.CreateJobForTask(task);
		});
		Assertions.assertDoesNotThrow(() -> {
			taskScheduler.CreateTriggerForTask(task);
		});
	}
}
