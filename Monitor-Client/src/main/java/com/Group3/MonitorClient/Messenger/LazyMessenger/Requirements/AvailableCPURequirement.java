package com.Group3.MonitorClient.Messenger.LazyMessenger.Requirements;

import com.Group3.MonitorClient.Messenger.LazyMessenger.Requirements.Requirement;
import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

/*
 * The AvailableCPURequirement classes purpose is to tests the systems CPU usage.
 * It is utilized by the LazyMessenger to add an upper bound on CPU usage for the Messenger to run
 */
public class AvailableCPURequirement implements Requirement {
    private double limit;
    private OperatingSystemMXBean os = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    /* Converts the minimum required available CPU percentage into an upper bound */
    public AvailableCPURequirement(double minimumAvailableMemory){
        if(minimumAvailableMemory > 0 && minimumAvailableMemory < 1){
            limit = 1 - minimumAvailableMemory;
        } else if(minimumAvailableMemory > 1 && minimumAvailableMemory < 100){
            limit = 1 - minimumAvailableMemory/100;
        } else {
            limit = 0.80;
            System.out.println("minimumAvailableMemory in AvailableCPURequirement out of bounds, set to default 0.20");
        }
    }

    /* returns current CPU usage */
    public double SystemCpuUsage(){
        //on Java 14 this should call os.getCpuLoad() instead.
        return os.getSystemCpuLoad();
    }

    /* tests if the Systems CPU usage is above the limit */
    @Override
    public boolean test() {
        return SystemCpuUsage() < limit;
    }
}
