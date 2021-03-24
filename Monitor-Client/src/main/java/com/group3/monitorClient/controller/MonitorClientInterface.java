package com.group3.monitorClient.controller;

import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.ErrorApi;
import org.openapitools.client.api.MonitorApi;
import org.openapitools.client.model.ErrorData;
import org.openapitools.client.model.TimingMonitorData;

import java.util.regex.Pattern;

public class MonitorClientInterface{
    private String monitorIP;
    private ErrorApi errorApi;
    private MonitorApi monitorApi;

    public MonitorClientInterface(String MonitorIP) {
        SetMonitorIP(MonitorIP);
        ApiClient client = new ApiClient();
        client.setBasePath(monitorIP);
        monitorApi = new MonitorApi(client);
        errorApi = new ErrorApi(client);
    }

    public void SetMonitorIP(String MonitorIP) {
        if (Pattern.matches("^http://\\d+.\\d+.\\d+.\\d+:\\d+$", MonitorIP)) {
            monitorIP = MonitorIP;
        } else if(Pattern.matches("^\\d+.\\d+.\\d+.\\d+:\\d+$", MonitorIP)){
            monitorIP = MonitorIP;
        } else if(Pattern.matches("^http://localhost:\\d+$", MonitorIP)){
            monitorIP = MonitorIP;
        } else if(Pattern.matches("^localhost:\\d+$", MonitorIP)){
            monitorIP = MonitorIP;
        } else {
            //TODO: crash and burn?
        }
    }

    public int addMonitorData (TimingMonitorData timingMonitorData) throws ApiException {
        return monitorApi.addMonitorDataWithHttpInfo(timingMonitorData).getStatusCode();
    }

    public  int addErrorData (ErrorData errorData) throws ApiException {
        return errorApi.addErrorDataWithHttpInfo(errorData).getStatusCode();
    }
}


