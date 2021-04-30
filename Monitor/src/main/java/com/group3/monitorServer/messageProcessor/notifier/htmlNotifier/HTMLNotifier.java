package com.group3.monitorServer.messageProcessor.notifier.htmlNotifier;

import com.group3.monitorServer.constraint.Constraint;
import com.group3.monitorServer.constraint.analyze.ConstraintAnalysisDetails;
import com.group3.monitorServer.constraint.analyze.ConstraintAnalyzer;
import com.group3.monitorServer.constraint.store.ConstraintStore;
import com.group3.monitorServer.messageProcessor.notifier.Notifier;
import org.openapitools.model.ErrorData;
import org.openapitools.model.TimingMonitorData;
import java.time.format.DateTimeFormatter;


import java.time.OffsetDateTime;


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
        ConstraintAnalysisDetails constDetails = constAnalyzer.analyzeTimings(msg1.getTimestamp(),
                                                                              msg2.getTimestamp(),
                                                                              msg1.getTargetEndpoint());

        if(constDetails.isViolated()){
            Constraint constraint = constDetails.getConstraint();
            StringBuilder Message = new StringBuilder("<b>Timing constraint violation:</b> ");

            if(constraint.getName() != null){
                Message.append("with name <" + constDetails.getConstraint().getName() + "> and ");
            }

            Message.append("for target-endpoint <" + constraint.getEndpoint() + ">, ");
            Message.append("Details: time difference = <" + constDetails.getDiff() + ">, ");
            Message.append("bounds ");

            if(constraint.getMin() != null){
                Message.append("min: <" + constraint.getMin() + ">, ");
            }

            Message.append("max: <" + constraint.getMax() + ">");

            return htmlWriter.AddMessage(Message.toString());
        } else {
            return true;
        }
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
        String errorMessage = "<b>Error violation:</b> An unknown ErrorType was encountered by node: <" +
                              errorData.getSenderID() + "> at ";
        String dateTime = errorData.getTimestamp().format(DateTimeFormatter.RFC_1123_DATE_TIME);
        errorMessage += dateTime.substring(0, dateTime.length()-6);

        return htmlWriter.AddMessage(errorMessage);
}

    private boolean createNoConnectionMessage(ErrorData errorData) {
        String errorMessage = "<b>Error violation:</b> Node <" + errorData.getSenderID() +
                              ">: " + errorData.getComment();

        return htmlWriter.AddMessage(errorMessage);
    }

    private boolean createHttpErrorMessage(ErrorData errorData) {
        String errorMessage = "<b>Error violation:</b> Node <" +
                              errorData.getSenderID() +
                              ">: after 10 successive unsuccessful attempts to send data to monitor server, it received the following httpcode: " +
                              errorData.getHttpResponse();
        return htmlWriter.AddMessage(errorMessage);
    }
}
