package com.Group3.petstore.Controller;

import org.openapitools.api.TimingApi;
import org.openapitools.model.Timing;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class Controller implements TimingApi {
    @Override
    public ResponseEntity<Void> postTiming(@Valid Timing timing) {
        System.out.println(timing.getId());

        return null;
    }
}
