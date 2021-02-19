package com.Group3.petstore;

import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.PetApi;
import org.openapitools.client.model.Pet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
//import java.time.OffsetDateTime;
import org.threeten.bp.OffsetDateTime;

@SpringBootApplication
public class PetstoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetstoreApplication.class, args);
		try {
			awaitInput();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static void awaitInput() throws IOException {
		String input = null;
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Ready for Input!");
		while (true) {
			input = reader.readLine();
			createCall();
			System.out.println("Received Input: " + input);
		}
	}

	private static void createCall() {
        ApiClient client = new ApiClient();
        client.setBasePath("http://85.191.161.150:8080");
        PetApi api = new PetApi(client);
        List<Pet> AllPets = new ArrayList<Pet>();

        try {
            AllPets = api.getAllPets();
        } catch (ApiException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        for (Pet pet : AllPets){
            System.out.println(pet);
        }
    }
}
