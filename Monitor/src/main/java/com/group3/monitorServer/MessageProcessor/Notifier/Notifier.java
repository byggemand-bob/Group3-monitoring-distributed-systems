package com.group3.monitorServer.MessageProcessor.Notifier;

import com.group3.monitorServer.messages.ErrorDataMessage;
import com.group3.monitorServer.messages.TimingMonitorDataMessage;

public interface Notifier {
    void timingViolation(TimingMonitorDataMessage msg1, TimingMonitorDataMessage msg2);

    void errorViolation(ErrorDataMessage msg);
}
