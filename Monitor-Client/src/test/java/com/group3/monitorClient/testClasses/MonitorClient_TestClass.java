package com.group3.monitorClient.testClasses;

import org.openapitools.client.ApiResponse;
import org.openapitools.client.api.MonitorApi;
import org.openapitools.client.model.TimingMonitorData;

import java.util.HashMap;

/*
 * class overwrites addMonitorData in MonitorApi to do nothing for testing purposes,
 * this removes the need for connecting to a server
 */
public class MonitorClient_TestClass extends MonitorApi {
    int ResponseStatusCode = 200;

    public MonitorClient_TestClass(){}

    /* Specifies a different response code to return */
    public MonitorClient_TestClass(int ResponseCode){
        ResponseStatusCode = ResponseCode;
    }

    public void ChangeReturnedStatusCode(int StatusCode){
        ResponseStatusCode = StatusCode;
    }

    @Override
    public ApiResponse<Void> addMonitorDataWithHttpInfo(TimingMonitorData Data){
        return new ApiResponse<Void>(ResponseStatusCode, new HashMap<>());
    }
}
