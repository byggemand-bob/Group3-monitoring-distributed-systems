package com.Group3.monitorClient.controller;

public class MonitorClientInterface{
//    private String monitorIP;
//    private Messenger messenger;
//
//    public MonitorClientInterface(String MonitorIP) {
//        SetMonitorIP(MonitorIP);
//        messenger = new LazyMessenger(monitorIP);
//        ((LazyMessenger)messenger).AddRequirement(new AvailableCPURequirement(40));
//        messenger.Start();
//    }
//
//    public void SetMonitorIP(String MonitorIP) {
//        if (Pattern.matches("^http://\\d+.\\d+.\\d+.\\d+:\\d+$", MonitorIP)) {
//            monitorIP = MonitorIP;
//        } else if(Pattern.matches("^\\d+.\\d+.\\d+.\\d+:\\d+$", MonitorIP)){
//            monitorIP = MonitorIP;
//        } else if(Pattern.matches("^http://localhost:\\d+$", MonitorIP)){
//            monitorIP = MonitorIP;
//        } else if(Pattern.matches("^localhost:\\d+$", MonitorIP)){
//            monitorIP = MonitorIP;
//        } else {
//            //TODO: crash and burn?
//        }
//    }
//
//    public void addMonitorData(long EventID, long senderID, @Nullable String TargetEndPoint) throws ApiException {
//        TimingMonitorData timingMonitorData = new TimingMonitorData();
//
//        timingMonitorData.setEventID(EventID);
//        timingMonitorData.setSenderID(senderID);
//        timingMonitorData.timestamp(OffsetDateTime.now());
//        if(TargetEndPoint != null){
//            timingMonitorData.setTargetEndpoint(TargetEndPoint);
//        }
//
//        messenger.AddMonitorData(timingMonitorData);
//    }
}


