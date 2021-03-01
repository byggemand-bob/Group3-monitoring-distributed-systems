package com.Group3.MonitorClient.LazyMessenger.Requirements;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

/*
* The AvailableCPURequirement classes purpose is to tests the systems CPU usage.
* It is utilized by the LazyMessenger to add an upper bound on CPU usage for the Messenger to run
*/
public class AvailableCPURequirement implements Requirement {
    private double limit;
    OperatingSystemMXBean os = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    /* Converts the required available CPU percentage into an upper bound */
    public AvailableCPURequirement(double RequiredAvailableMemory){
        if(RequiredAvailableMemory > 0 && RequiredAvailableMemory < 1){
            limit = 1 - RequiredAvailableMemory;
        } else if(RequiredAvailableMemory > 1 && RequiredAvailableMemory < 100){
            limit = 1 - RequiredAvailableMemory/100;
        } else {
            limit = 0.80;
            System.out.println("RequiredAvailableMemory in AvailableCPURequirement out of bounds, limit set to default 0.80");
        }
    }

    /* returns current CPU usage */
    private double SystemCpuUsage(){
        return os.getSystemCpuLoad();
    }

    /* tests if the Systems CPU usage is above the limit */
    @Override
    public boolean test() {
        return SystemCpuUsage() < limit;
    }
}
