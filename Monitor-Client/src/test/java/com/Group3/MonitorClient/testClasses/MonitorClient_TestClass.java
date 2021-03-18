package com.Group3.monitorClient.testClasses;

import org.openapitools.client.ApiResponse;
import org.openapitools.client.api.MonitorApi;
import org.openapitools.client.model.TimingMonitorData;

import java.util.HashMap;

/*
 * class overwrites addMonitorData in MonitorApi to do nothing for testing purposes,
 * this removes the need for connecting to a server
 */
public class MonitorClient_TestClass extends MonitorApi {
    @Override
    public ApiResponse<Void> addMonitorDataWithHttpInfo(TimingMonitorData Data){
        return new ApiResponse<Void>(200, new HashMap<>());
    }
}
