package com.Group3.MonitorClient;

import org.openapitools.client.ApiException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
//import java.time.OffsetDateTime;
import com.Group3.MonitorClient.Controller.MonitorClientInterface;

@SpringBootApplication
public class TestCall {

	public static void main(String[] args) {
		SpringApplication.run(TestCall.class, args);
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


		while(true){
			System.out.println("Press enter to send call");
			input = reader.readLine();
			createCall();
			System.out.println("Call send");
		}
	}

	private static void createCall() {
		MonitorClientInterface MonitorInterface = new MonitorClientInterface("http://localhost:8888");

		try {
			MonitorInterface.addMonitorData(1L, 1L, null);
		} catch (ApiException e){
			e.printStackTrace();
		}
	}
}
