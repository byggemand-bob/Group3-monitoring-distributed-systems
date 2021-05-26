package com.group3.monitorClient.testClasses;

import com.group3.monitorClient.controller.requirements.Requirement;

/* test requirement class, simulates a requirement returning either true or false for testing purposes */
public class TrueFalseRequirement_TestClass implements Requirement {
    public boolean bool;

    public TrueFalseRequirement_TestClass(boolean bool){
        this.bool = bool;
    }

    @Override
    public boolean Test() {
        return bool;
    }
}
