package com.group3.monitorServer.messageProcessor.notifier.htmlNotifier;

import com.group3.monitorServer.constraint.analyze.ConstraintAnalyzer;
import com.group3.monitorServer.constraint.store.ConstraintStore;
import com.group3.monitorServer.messageProcessor.notifier.Notifier;
import org.openapitools.model.ErrorData;
import org.openapitools.model.TimingMonitorData;

public class HTMLNotifier implements Notifier {
    private final HTMLWriter htmlWriter;
    private final ConstraintAnalyzer constAnalyzer;

    /*
     * Checks the user-specification file for html location
     * otherwise creates html in default location
     * "src/main/resources/Warnings.html"
     */
    public HTMLNotifier(ConstraintStore constStore) {
        htmlWriter = new HTMLWriter();
        constAnalyzer = new ConstraintAnalyzer(constStore);
    }

    /* Creates a html at the specified path */
    public HTMLNotifier(String htmlPath, ConstraintStore constStore) {
        htmlWriter = new HTMLWriter(htmlPath);
        constAnalyzer = new ConstraintAnalyzer(constStore);
    }

    @Override
    public boolean timingViolation(TimingMonitorData msg1, TimingMonitorData msg2) {
        //TODO: make an informative message

        if(constAnalyzer.analyzeTimings(msg1.getTimestamp(), msg2.getTimestamp(), msg1.getTargetEndpoint())){
            return true;
        } else {
            String Message = "node: \"" + msg1.getSenderID() + "\" violated timing constraints when contacting node: \"" +
                             msg1.getTargetEndpoint() + "\". Response time was = \"";
            
        }

        return true;
    }

    @Override
    public boolean errorViolation(ErrorData msg) {
        if (msg.getErrorMessageType() == ErrorData.ErrorMessageTypeEnum.HTTPERROR) {
            return createHttpErrorMessage(msg);
        } else if (msg.getErrorMessageType() == ErrorData.ErrorMessageTypeEnum.NOCONNECTION){
            return createNoConnectionMessage(msg);
        }else if (msg.getErrorMessageType() == ErrorData.ErrorMessageTypeEnum.UNKNOWNERROR){
            return createUnknownErrorMessage(msg);
        }

        return false;
    }


    private boolean createUnknownErrorMessage(ErrorData errorData) {
        String errorMessage = "An unknown ErrorType was encountered by node: \"" +
                              errorData.getSenderID() +
                              "\" at " + errorData.getTimestamp() + " o'clock";

        return htmlWriter.AddMessage(errorMessage);
}

    private boolean createNoConnectionMessage(ErrorData errorData) {
        String errorMessage = "Node \"" +
                              errorData.getSenderID() +
                              "\": " +
                              errorData.getComment();

        return htmlWriter.AddMessage(errorMessage);
    }

    private boolean createHttpErrorMessage(ErrorData errorData) {
        String errorMessage = "Node \"" +
                              errorData.getSenderID() +
                              "\": after 10 successive unsuccessful attempts to send data to monitor server, it received the following httpcode: " +
                              errorData.getHttpResponse();
        return htmlWriter.AddMessage(errorMessage);
    }
}
