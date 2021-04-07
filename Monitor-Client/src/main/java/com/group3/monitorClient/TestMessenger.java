package com.group3.monitorClient;


import com.group3.monitorClient.messenger.GreedyMessenger;
import com.group3.monitorClient.messenger.messages.MessageCreator;
import com.group3.monitorClient.messenger.messages.MessageInterface;
import com.group3.monitorClient.messenger.queue.PersistentSQLQueue;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.ApiResponse;
import org.openapitools.client.api.MonitorApi;
import org.openapitools.client.model.TimingMonitorData;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.threeten.bp.OffsetDateTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;

@SpringBootApplication
public class TestMessenger {
	private static SocketTimeoutException socketTimeoutException;

	public static void main(String[] args){
		SpringApplication.run(TestMessenger.class, args);
		PersistentSQLQueue queue = new PersistentSQLQueue("src/main/resources/sqlite/db/", "queue.db");
		GreedyMessenger messenger = new GreedyMessenger("85.191.161.150:8888", queue);
		messenger.Start();
		try {
			awaitInput(queue);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void awaitInput(PersistentSQLQueue queue) throws IOException {
		String input = null;
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));


		while(true){
			System.out.println("Press enter to send call");
			input = reader.readLine();
			createCall(queue);
			System.out.println("Call send");
		}
	}

	private static void createCall(PersistentSQLQueue queue) {
		MessageCreator messageCreator = new MessageCreator();

		TimingMonitorData data = new TimingMonitorData();
		data.setSenderID(1L);
		data.setEventID(2L);
		data.setTargetEndpoint("test");
		data.setTimestamp(OffsetDateTime.now());

		MessageInterface message = messageCreator.MakeMessage(data);

		queue.Put(message);
	}
}
