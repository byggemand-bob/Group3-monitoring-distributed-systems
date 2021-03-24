package com.group3.monitorClient.testClasses;

import com.group3.monitorClient.controller.MonitorClientInterface;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.ErrorData;
import org.openapitools.client.model.TimingMonitorData;

public class MonitorClientInterface_TestClass extends MonitorClientInterface {

    int responseCode = 200;

    public MonitorClientInterface_TestClass(String MonitorIP) {
        super(MonitorIP);
    }

    public MonitorClientInterface_TestClass(String MonitorIP, int ResponseCode) {
        super(MonitorIP);
        responseCode = ResponseCode;
    }

    public void ChangeReturnedStatusCode(int ResponseCode){
        responseCode = ResponseCode;
    }

    @Override
    public int addMonitorData (TimingMonitorData timingMonitorData) throws ApiException {
        return responseCode;
    }

    @Override
    public  int addErrorData (ErrorData errorData) throws ApiException {
        return responseCode;
    }
}
