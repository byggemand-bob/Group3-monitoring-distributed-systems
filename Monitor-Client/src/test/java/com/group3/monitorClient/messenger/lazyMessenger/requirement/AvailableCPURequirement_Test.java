package com.group3.monitorClient.messenger.lazyMessenger.requirement;

import com.group3.monitorClient.testClasses.AvailableCPURequirement_TestClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AvailableCPURequirement_Test {
    /* tests when the system usage is above the minimum required available memory*/
    @Test
    public void UnderLimit(){
        AvailableCPURequirement_TestClass testClass = new AvailableCPURequirement_TestClass(40);

        Assertions.assertTrue(testClass.Test());
    }

    /* tests when the system usage is below the minimum required available memory*/
    @Test
    public void OverLimit(){
        AvailableCPURequirement_TestClass testClass = new AvailableCPURequirement_TestClass(60);

        Assertions.assertFalse(testClass.Test());
    }
}

