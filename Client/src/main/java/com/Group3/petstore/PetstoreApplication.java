package com.Group3.petstore;

import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.DefaultApi;
import org.openapitools.client.model.Timing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
		DefaultApi api = new DefaultApi(client);
		Timing timing = new Timing();
		timing.id("test1");
		timing.timeStamp("1234");
		Timing response = null;
		try {
			api.postTiming(timing);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(timing);
	}
}
