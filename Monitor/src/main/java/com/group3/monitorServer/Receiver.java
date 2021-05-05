package com.group3.monitorServer;

import com.group3.monitorClient.configuration.ConfigurationManager;
import com.group3.monitorServer.constraint.store.ConstraintImporter;
import com.group3.monitorServer.constraint.store.ConstraintStore;
import com.group3.monitorServer.controller.Controller;
import com.group3.monitorServer.controller.requirements.AvailableCPURequirement;
import com.group3.monitorServer.messageProcessor.Delegator;
import com.group3.monitorServer.messageProcessor.notifier.htmlNotifier.HTMLNotifier;
import com.group3.monitorServer.messages.MessageCreator;
import com.group3.monitorServer.messages.SQLManager;
import com.group3.monitorServer.messages.SQLMessageManager;
import org.openapitools.api.ErrorApi;
import org.openapitools.api.MonitorApi;
import org.openapitools.model.ErrorData;
import org.openapitools.model.TimingMonitorData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import javax.validation.Valid;

import java.io.File;
import java.io.IOException;

import java.util.Optional;

@RestController
public class Receiver implements MonitorApi, ErrorApi {
    SQLMessageManager sqlMessageManager;
    MessageCreator messageCreator;


    public Receiver() throws IOException {
        this(ConfigurationManager.getInstance().getProperty(ConfigurationManager.sqlPathProp, "src"+File.separator+"main"+File.separator+"resources"+File.separator+"sqlite"+File.separator+"db"+File.separator), ConfigurationManager.getInstance().getProperty(ConfigurationManager.dbFileNameProp, "Messages.db"));
    }

    public Receiver(String sqlPath, String sqlFileName) throws IOException {
        SQLManager sqlManager = SQLManager.getInstance();
        sqlManager.Connect(sqlPath, sqlFileName);
        sqlMessageManager = new SQLMessageManager(sqlManager, "UnprocessedMessages");
        messageCreator = new MessageCreator();
        ConstraintImporter constraintImporter = new ConstraintImporter();
        ConstraintStore constraintStore = constraintImporter.importConstraints(constraintImporter.getDefaultConstraintPath());
        Controller controller = new Controller();
        controller.addRequirement(new AvailableCPURequirement(0));
        controller.addThread(new Delegator(sqlMessageManager, new HTMLNotifier(constraintStore)));
        controller.start();
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    @Override
    public ResponseEntity<Void> addMonitorData(@Valid TimingMonitorData timingMonitorData) {
        messageCreator.MakeMessage(timingMonitorData).makeSQL(sqlMessageManager);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> addErrorData(@Valid ErrorData errorData) {
        messageCreator.MakeMessage(errorData).makeSQL(sqlMessageManager);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
