package com.Group3.petstore.Controller;

import com.Group3.MonitorClient.Controller.MonitorClientInterface;

import org.openapitools.api.PetApi;
import org.openapitools.client.ApiException;
import org.openapitools.model.Pet;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PetstoreController implements PetApi {
    MonitorClientInterface MonitorClient = new MonitorClientInterface("http://85.191.161.150:8888");

    @Override
    public ResponseEntity<List<Pet>> getAllPets() throws ApiException {
        MonitorClient.addMonitorData(1L,1L, null);

        List<Pet> pets = new ArrayList<>();

        var pet1 = new Pet();
        var pet2 = new Pet();

        pet1.name("fido");
        pet2.name("spot");

        pets.add(pet1);
        pets.add(pet2);

        return new ResponseEntity<>(pets, HttpStatus.OK);
    }
}
