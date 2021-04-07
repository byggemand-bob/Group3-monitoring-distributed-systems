package com.group3.monitorServer.controller;

import com.group3.monitorServer.controller.messages.MessageCreator;
import com.group3.monitorServer.controller.queue.PersistentSQLQueue;
import org.openapitools.api.ErrorApi;
import org.openapitools.api.MonitorApi;
import org.openapitools.model.ErrorData;
import org.openapitools.model.TimingMonitorData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class Controller implements MonitorApi {
    PersistentSQLQueue messageQueue;
    MessageCreator messageCreator;

    public Controller() {
        messageQueue = new PersistentSQLQueue("src/main/resources/sqlite/db/", "queue.db");
        messageCreator = new MessageCreator();
    }

//    @Override
//    public Optional<NativeWebRequest> getRequest() {
//        return Optional.empty();
//    }

    @Override
    public ResponseEntity<Void> addMonitorData(@Valid TimingMonitorData timingMonitorData) {
        messageQueue.Put(messageCreator.MakeMessage(timingMonitorData));
        System.out.println(timingMonitorData);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @Override
//    public ResponseEntity<Void> addErrorData(@Valid ErrorData errorData) {
//        messageQueue.Put(messageCreator.MakeMessage(errorData));
//        System.out.println(errorData);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
}
