package com.Group3.MonitorClient.junitTests.Messenger.LazyMessenger.Requirement;

import com.Group3.MonitorClient.junitTests.TestClasses.AvailableCPURequirement_TestClass;
import org.junit.Test;
import org.junit.Assert;

public class AvailableCPURequirement_Test {
    /* tests when the system usage is above the minimum required available memory*/
    @Test
    public void Test_testMethod1(){
        AvailableCPURequirement_TestClass testClass = new AvailableCPURequirement_TestClass(40);

        Assert.assertTrue(testClass.test());
    }

    /* tests when the system usage is below the minimum required available memory*/
    @Test
    public void Test_testMethod2(){
        AvailableCPURequirement_TestClass testClass = new AvailableCPURequirement_TestClass(60);

        Assert.assertFalse(testClass.test());
    }
}

