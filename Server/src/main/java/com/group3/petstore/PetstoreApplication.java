package com.group3.petstore;

import com.group3.monitorClient.MonitorClientInterface;
import com.group3.monitorClient.messenger.Messenger;
import com.group3.monitorClient.messenger.messages.SQLManager;
import com.group3.monitorClient.messenger.messages.SQLMessageManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication
@ComponentScan(basePackages = "org.openapitools.api")
public class PetstoreApplication {

	public static void main(String[] args) {
		Messenger.initialize("src/main/resources/sqlite/db", "queue.db");//TODO: User-specification paths instead
		Messenger.getInstance().start();//TODO: add to some autogeneration
		//TODO: addController
		SpringApplication.run(PetstoreApplication.class, args);

	}
}
