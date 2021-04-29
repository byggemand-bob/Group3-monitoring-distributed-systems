package com.group3.petstore;

import com.group3.monitorClient.messenger.Messenger;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.PetApiClient;
import org.openapitools.client.model.Pet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class PetstoreApplication {

	public static void main(String[] args) throws IOException, ApiException {
		Messenger.initialize("src/main/resources/sqlite/db", "queue.db");//TODO: User-specification paths instead
		Messenger.getInstance().start();//TODO: add to some autogeneration
		//TODO: addController
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

		while (true) {
			System.out.println("Press enter to send call");
			input = reader.readLine();
			createCall();
			System.out.println("Done");
		}
	}

	private static void createCall() {
		PetApiClient api = new PetApiClient();
		List<Pet> Pets = new ArrayList<Pet>();

		try {
			Pets = api.getAllPets();
		} catch (ApiException e) {
			e.printStackTrace();
		}

		for (Pet pet : Pets) {
			System.out.println(pet);
		}
	}
}
