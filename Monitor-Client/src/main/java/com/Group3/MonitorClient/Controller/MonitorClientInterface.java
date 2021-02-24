package com.Group3.MonitorClient.Controller;

import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.MonitorApi;
import org.openapitools.client.model.TimingMonitorData;
import org.threeten.bp.OffsetDateTime;

import javax.annotation.Nullable;
import java.util.regex.Pattern;

public class MonitorClientInterface{
    ApiClient client;
    private MonitorApi MonitorClient;

    public MonitorClientInterface(String MonitorIP){
        client = new ApiClient();
        SetMonitorIP(MonitorIP);
        MonitorClient = new MonitorApi(client);
    }

    public void SetMonitorIP(String MonitorIP){
        if (Pattern.matches("^http://\\d+.\\d+.\\d+.\\d+:\\d+$", MonitorIP)) {
            client.setBasePath(MonitorIP);
        } else if(Pattern.matches("^\\d+.\\d+.\\d+.\\d+:\\d+$", MonitorIP)){
            client.setBasePath("http://" + MonitorIP);
        } else if(Pattern.matches("^http://localhost:\\d+$", MonitorIP)){
            client.setBasePath(MonitorIP);
        } else if(Pattern.matches("^localhost:\\d+$", MonitorIP)){
            client.setBasePath("http://" + MonitorIP);
        }
    }

    public void addMonitorData(long EventID, long senderID, @Nullable String TargetEndPoint) throws ApiException {
        TimingMonitorData timingMonitorData = new TimingMonitorData();

        timingMonitorData.setEventID(EventID);
        timingMonitorData.setSenderID(senderID);
        timingMonitorData.timestamp(OffsetDateTime.now());
        if(TargetEndPoint != null){
            timingMonitorData.setTargetEndpoint(TargetEndPoint);
        }

        MonitorClient.addMonitorData(timingMonitorData);
    }
}
