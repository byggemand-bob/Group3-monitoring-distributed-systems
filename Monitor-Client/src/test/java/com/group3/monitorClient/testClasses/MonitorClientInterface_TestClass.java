package com.group3.monitorClient.testClasses;

import com.group3.monitorClient.MonitorClientInterface;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.ErrorData;
import org.openapitools.client.model.TimingMonitorData;

public class MonitorClientInterface_TestClass extends MonitorClientInterface {

    int responseCode = 200;

    public MonitorClientInterface_TestClass(String MonitorIP) {
        super();
    }

    public MonitorClientInterface_TestClass(String MonitorIP, int ResponseCode) {
        super();
        responseCode = ResponseCode;
    }

    public void ChangeReturnedStatusCode(int ResponseCode){
        responseCode = ResponseCode;
    }

    @Override
    public int sendMonitorData (TimingMonitorData timingMonitorData) throws ApiException {
        return responseCode;
    }

    @Override
    public int sendErrorData (ErrorData errorData) throws ApiException {
        return responseCode;
    }
}
