package com.group3.monitorClient;

import com.group3.monitorClient.configuration.ConfigurationManager;
import com.group3.monitorClient.exception.MonitorConfigException;
import com.group3.monitorClient.messenger.Messenger;
import com.group3.monitorClient.messenger.messages.MessageCreator;
import com.group3.monitorClient.messenger.messages.SQLManager;
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
    private final SQLMessageManager sqlMessageManager;
    private final MessageCreator messageCreator;
    private final ApiClient client;
    private final MonitorApi monitorClient;
    private final ErrorApi errorApi;
    private static final AtomicLong eventIDSequence = new AtomicLong(1L);
    private final long senderID = ConfigurationManager.getInstance().getPropertyAsLong(ConfigurationManager.IDProp);
    private final String monitorURL = ConfigurationManager.getInstance().getProperty(ConfigurationManager.monitorServerAddressProp);
    private final int daysToKeepMessages = ConfigurationManager.getInstance().getPropertyAsInteger(ConfigurationManager.daysToKeepMessagesProp, 30);
    private final String sqlPath = ConfigurationManager.getInstance().getProperty(ConfigurationManager.sqlPathProp, "src/main/resources/sqlite/db/");
    private final String dbFileName = ConfigurationManager.getInstance().getProperty(ConfigurationManager.dbFileNameProp, "queue.db");
    private final double availableCPURequirementProp = ConfigurationManager.getInstance().getPropertyAsDouble(ConfigurationManager.availableCPURequirementProp, 0.2);

    public MonitorClientInterface(){
        client = new ApiClient();
        ValidateAndSetMonitorIP(monitorURL);
        monitorClient = new MonitorApi(client);
        errorApi = new ErrorApi(client);
        sqlMessageManager = Messenger.getSqlMessageManager();
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
        return errorApi.addErrorDataWithHttpInfo(errorData).getStatusCode();
    }

    public long queueMonitorData(long eventID, @Nullable String targetEndPoint, EventCodeEnum eventCode) {
        addMonitorData(eventID, targetEndPoint, eventCode);
        return eventID;
    }

    public long getNextEventID() {
        return eventIDSequence.getAndIncrement();
    }
}