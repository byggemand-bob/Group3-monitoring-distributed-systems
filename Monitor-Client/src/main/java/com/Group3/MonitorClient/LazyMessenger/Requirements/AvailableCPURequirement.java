package com.Group3.MonitorClient.LazyMessenger.Requirements;

public class AvailableCPURequirement implements Requirement {
    private double limit;

    public AvailableCPURequirement(double RequiredAvailableMemory){
        if(RequiredAvailableMemory > 0 && RequiredAvailableMemory < 1){
            limit = 1 - RequiredAvailableMemory;
        } else if(RequiredAvailableMemory > 1 && RequiredAvailableMemory < 100){
            limit = 1 - RequiredAvailableMemory/100;
        }
    }

    private double SystemCPUusage(){
        return 1; //TODO: return CPU usage
    }

    @Override
    public boolean test() {
        return SystemCPUusage() < limit;
    }
}
