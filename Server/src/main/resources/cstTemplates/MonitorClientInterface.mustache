package com.group3.monitorClient;

import com.group3.monitorClient.configuration.ConfigurationManager;
import com.group3.monitorClient.exception.MonitorConfigException;
import com.group3.monitorClient.messenger.messages.MessageCreator;
import com.group3.monitorClient.messenger.messages.SQLMessageManager;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.ErrorApi;
import org.openapitools.client.api.MonitorApi;
import org.openapitools.client.model.ErrorData;
import org.openapitools.client.model.TimingMonitorData;
import org.openapitools.client.model.TimingMonitorData.EventCodeEnum;

import java.time.OffsetDateTime;

import javax.annotation.Nullable;

import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

public class MonitorClientInterface {
    SQLMessageManager sqlMessageManager;
    MessageCreator messageCreator;
    private ApiClient client;
    private MonitorApi monitorClient;
    private ErrorApi ErrorClient;
    private String monitorURL;
    private static AtomicLong eventIDSequence = new AtomicLong(1L);
    private static final long senderID = ConfigurationManager.getInstance().getPropertyAsLong(ConfigurationManager.getInstance().IDProp);

    public MonitorClientInterface(){
        monitorURL = ConfigurationManager.getInstance().getProperty(ConfigurationManager.getInstance().monitorServerAddressProp);
        client = new ApiClient();
        ValidateAndSetMonitorIP(monitorURL);
        monitorClient = new MonitorApi(client);
        sqlMessageManager = new SQLMessageManager(SQLMessageManager.message_table_name);
        messageCreator = new MessageCreator();
    }

    private void ValidateAndSetMonitorIP(String MonitorIP){
        if (Pattern.matches("^http://\\d+.\\d+.\\d+.\\d+:\\d+$", MonitorIP)) {
            client.setBasePath(MonitorIP);
        } else if(Pattern.matches("^\\d+.\\d+.\\d+.\\d+:\\d+$", MonitorIP)){
            client.setBasePath("http://" + MonitorIP);
        } else if (Pattern.matches("^localhost:\\d+$", MonitorIP)){
            client.setBasePath("http://" + MonitorIP);
        } else if (Pattern.matches("^http://localhost:\\d+$", MonitorIP)){
            client.setBasePath(MonitorIP);
        } else {
            String msg = String.format("IP address defined in configuration property <%s> is malformed, please check...\n"+
                            "Properties found at <%s>",
                    ConfigurationManager.getInstance().monitorServerAddressProp,
                    ConfigurationManager.getInstance().getPropertiesPath());
            throw new MonitorConfigException(msg);
        }
    }

    private void addMonitorData(long eventID, @Nullable String TargetEndPoint, EventCodeEnum eventCode) {
        TimingMonitorData timingMonitorData = new TimingMonitorData();
        timingMonitorData.setEventCode(eventCode);
        timingMonitorData.setEventID(eventID);
        timingMonitorData.setSenderID(senderID);
        timingMonitorData.timestamp(OffsetDateTime.now());
        if(TargetEndPoint != null){
            timingMonitorData.setTargetEndpoint(TargetEndPoint);
        }
        messageCreator.MakeMessage(timingMonitorData).makeSQL(sqlMessageManager);
    }

    public int sendMonitorData(TimingMonitorData timingMonitorData) throws ApiException {
        return monitorClient.addMonitorDataWithHttpInfo(timingMonitorData).getStatusCode();
    }

    public int sendErrorData(ErrorData errorData) throws ApiException {
        return ErrorClient.addErrorDataWithHttpInfo(errorData).getStatusCode();
    }

    public long queueMonitorData(@Nullable String targetEndPoint, EventCodeEnum eventCode) {
        long eventID = getNextEventID();
        addMonitorData(eventID, targetEndPoint, eventCode);
        return eventID;
    }

    public long queueMonitorData(long eventID, @Nullable String targetEndPoint, EventCodeEnum eventCode) {
        addMonitorData(eventID, targetEndPoint, eventCode);
        return eventID;
    }

    private long getNextEventID() {
        return eventIDSequence.getAndIncrement();
    }
}