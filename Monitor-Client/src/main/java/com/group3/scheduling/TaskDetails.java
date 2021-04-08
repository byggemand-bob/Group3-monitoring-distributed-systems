package com.group3.scheduling;

import org.quartz.JobKey;
import org.quartz.TriggerKey;

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
	
	public boolean GetEnabled() {
		return enabled;
	}
	public void SetEnabled(boolean enabled) {
			this.enabled = enabled;
	}
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
