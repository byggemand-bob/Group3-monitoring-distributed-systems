package com.group3.monitorClient.testClasses;

import com.group3.monitorClient.messenger.lazyMessenger.requirements.AvailableCPURequirement;

/* Test class that overrides the SystemCpuUsage() method to return a constant predictable value for testing */
public class AvailableCPURequirement_TestClass extends AvailableCPURequirement {
    public AvailableCPURequirement_TestClass(double minimumAvailableMemory) {
        super(minimumAvailableMemory);
    }

    @Override
    public double SystemCpuUsage(){
        return 0.5;
    }
}
