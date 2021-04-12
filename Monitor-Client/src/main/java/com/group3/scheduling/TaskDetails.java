package com.group3.scheduling;

import org.quartz.JobKey;
import org.quartz.TriggerKey;

import com.group3.scheduling.task.AbstractTask;

public class TaskDetails {
	
	private AbstractTask task;
	private boolean enabled;
	private JobKey jobKey;
	private TriggerKey triggerKey;
	
	
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
	 * Get the {@link JobKey} from the Quartz scheduler instance in the {@link TaskScheduler}
	 * 
	 * @return
	 */
	public JobKey GetJobKey() {
		return jobKey;
	}
	
	public TriggerKey GetTriggerKey() {
		return triggerKey;
	}
	
	public TriggerKey SetTriggerKey(TriggerKey triggerKey) {
		this.triggerKey = triggerKey;
		return triggerKey;
	}
	
	public AbstractTask GetTask() {
		return task;
	}
}
