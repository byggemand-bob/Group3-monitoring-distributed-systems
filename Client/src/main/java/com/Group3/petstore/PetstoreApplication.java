package com.Group3.petstore;

import java.io.IOException;

import org.openapitools.client.ApiException;
import org.openapitools.client.api.PetApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PetstoreApplication {

	public static void main(String[] args) throws IOException, ApiException {
		SpringApplication.run(PetstoreApplication.class, args);
		sendDummyMessages();
	}
	
	public static void sendDummyMessages() throws IOException, ApiException {
		PetApi petApi = new PetApi();
		while (true) {
			System.in.read();
			petApi.getAllPets();
		}
	}

}
