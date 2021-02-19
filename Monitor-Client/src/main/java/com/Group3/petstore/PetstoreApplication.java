package com.Group3.petstore;

import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.MonitorApi;
import org.openapitools.client.model.TimingMonitorData;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
//import java.time.OffsetDateTime;
import org.threeten.bp.OffsetDateTime;
import com.Group3.petstore.Controller.MonitorClientInterface;

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
		MonitorClientInterface Minterface = new MonitorClientInterface("http://httpbin.org/post");

		try {
			Minterface.addMonitorData(1L, 1L, null);
		} catch (ApiException e){
			e.printStackTrace();
		}

//		ApiClient client = new ApiClient();
//		client.setBasePath("http://httpbin.org/post");
//		MonitorApi api = new MonitorApi(client);
//
//		TimingMonitorData timing = new TimingMonitorData();
//		timing.eventID(1L);
//		timing.timestamp(OffsetDateTime.now());
//		timing.senderID(1L);
//
//		try {
//			api.addMonitorData(timing);
//		} catch (ApiException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		System.out.println(timing);
	}
}
