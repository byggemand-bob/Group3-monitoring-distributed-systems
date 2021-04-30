package com.group3.monitorServer.messageProcessor.notifier;

import org.openapitools.model.ErrorData;
import org.openapitools.model.TimingMonitorData;

public interface Notifier {
    boolean timingViolation(TimingMonitorData msg1, TimingMonitorData msg2);

    boolean errorViolation(ErrorData msg);
}
