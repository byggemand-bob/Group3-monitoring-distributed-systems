package com.Group3.MonitorServer.Controller;

import org.openapitools.api.MonitorApi;
import org.openapitools.model.TimingMonitorData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class Controller implements MonitorApi {
    @Override
    public ResponseEntity<Void> addMonitorData(@Valid TimingMonitorData timingMonitorData) {
        System.out.println(timingMonitorData);
        return null;
    }
}
