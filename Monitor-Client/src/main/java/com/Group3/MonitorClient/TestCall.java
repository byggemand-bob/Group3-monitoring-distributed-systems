package com.Group3.monitorClient;


import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.ApiResponse;
import org.openapitools.client.api.MonitorApi;
import org.openapitools.client.model.TimingMonitorData;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.SocketTimeoutException;

import org.threeten.bp.OffsetDateTime;

@SpringBootApplication
public class TestCall {
	private static SocketTimeoutException socketTimeoutException;

	public static void main(String[] args){
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
		ApiClient client = new ApiClient();
		client.setBasePath("http://85.191.161.150:8080");
		MonitorApi monitorClient = new MonitorApi(client);

		TimingMonitorData data = new TimingMonitorData();
		data.setEventID(1L);
		data.setSenderID(1L);
		data.setTargetEndpoint("yolo");
		data.setTimestamp(OffsetDateTime.now());

		ApiResponse<Void> Response = null;

		try {
			Response = monitorClient.addMonitorDataWithHttpInfo(data);
		} catch(ApiException e) {
			String exception = e.toString();

			if(exception.contains("java.net.SocketTimeoutException")){
				System.out.println("IT WORKS!!!");
			}

			//System.out.println(e.toString());
		}

		if(Response != null){
			System.out.println(Response);
		}

//		MonitorClientInterface MonitorInterface = new MonitorClientInterface("http://85.191.161.150:8080");
//
//		try {
//			MonitorInterface.addMonitorData(1L, 1L, null);
//		} catch (ApiException e) {
//			e.printStackTrace();
//		}
	}
}
