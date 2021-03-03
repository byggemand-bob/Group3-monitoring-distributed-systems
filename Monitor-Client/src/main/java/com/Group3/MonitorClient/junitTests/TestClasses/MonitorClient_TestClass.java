package com.Group3.MonitorClient.junitTests.TestClasses;

import org.openapitools.client.api.MonitorApi;
import org.openapitools.client.model.TimingMonitorData;

/*
 * class overwrites addMonitorData in MonitorApi to do nothing for testing purposes,
 * this removes the need for connecting to a server
 */
public class MonitorClient_TestClass extends MonitorApi {
    @Override
    public void addMonitorData(TimingMonitorData Data){}
}
