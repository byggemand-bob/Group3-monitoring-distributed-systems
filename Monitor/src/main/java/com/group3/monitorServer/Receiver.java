package com.group3.monitorServer;

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
import java.util.Optional;

@RestController
public class Receiver implements MonitorApi, ErrorApi {
    SQLMessageManager sqlMessageManager;
    MessageCreator messageCreator;

    public Receiver() {
        this("src/main/resources/sqlite/db/", "queue.db"); //TODO: should check for user specified path and db name
    }

    public Receiver(String sqlPath, String sqlFileName) {
        SQLManager sqlManager = SQLManager.getInstance();
        sqlManager.Connect(sqlPath, sqlFileName);
        sqlMessageManager = new SQLMessageManager(sqlManager, "UnprocessedMessages");
        messageCreator = new MessageCreator();
        //TODO: Create and start MessageProcessor
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
