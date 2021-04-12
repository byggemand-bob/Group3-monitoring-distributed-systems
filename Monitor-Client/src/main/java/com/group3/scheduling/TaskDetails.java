package com.group3.scheduling;

import org.quartz.JobKey;
import org.quartz.TriggerKey;

import com.group3.scheduling.task.AbstractTask;

/**
 * Class to contain the details of an associated task, that is to be scheduled in the {@link TaskScheduler}.
 *
 */
public class TaskDetails {
	
	private AbstractTask task;
	private boolean enabled;
	private JobKey jobKey;
	private TriggerKey triggerKey;
	
	/**
	 * Constructor for the TaskDetails class.
	 * 
	 * @param task The {@link AbstractTask} which are associated with the details.
	 * @param jobKey The {@link JobKey} of the Quartz {@link Job} created from the Task.
	 * @param triggerKey The {@link TriggerKey} for the Quartz {@link Job} created to run scheduled according to the cron defined in the {@link AbstractTask}.
	 */
	public TaskDetails(AbstractTask task, JobKey jobKey, TriggerKey triggerKey) {
		this.task = task;
		this.enabled = false;
		this.jobKey = jobKey;
		this.triggerKey = triggerKey;
	}
	
	/**
	 * Get the whether the task associated with the details, is enabled
	 * 
	 * @return true if enabled, false if not
	 */
	public boolean IsEnabled() {
		return enabled;
	}
	
	/**
	 * Set the enabled status of the Task<br>
	 * <b>This is pseudo information as the actual enabling is determined in the {@link TaskScheduler}</b>
	 * 
	 * @param enabled Whether the Task associated with the details is enabled
	 */
	public void SetEnabled(boolean enabled) {
			this.enabled = enabled;
	}
	
	/**
	 * Get the {@link JobKey} created in the Quartz scheduler instance in the {@link TaskScheduler} for the associated task.
	 * 
	 * @return The {@link JobKey} for the associated Task.
	 */
	public JobKey GetJobKey() {
		return jobKey;
	}
	
	/**
	 * Get the {@link TriggerKey} created in the Quartz scheduler instance in the {@link TaskScheduler} for the associated task.
	 * 
	 * @return The {@link TriggerKey} of the {@link Trigger} for the Task.
	 */
	public TriggerKey GetTriggerKey() {
		return triggerKey;
	}
	
	/**
	 * Set the {@link TriggerKey} created in the Quartz scheduler instance in the {@link TaskScheduler} for the associated task.
	 * 
	 * @param triggerKey The {@link TriggerKey} of the {@link Trigger} for the Task.
	 */
	public void SetTriggerKey(TriggerKey triggerKey) {
		this.triggerKey = triggerKey;
	}
	
	/**
	 * Get the associated {@link AbstractTask} for the TaskDetails.
	 * 
	 * @return The {@link AbstractTask} associated with the TaskDetails.
	 */
	public AbstractTask GetTask() {
		return task;
	}
}
