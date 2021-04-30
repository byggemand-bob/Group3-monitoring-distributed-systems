package com.group3.petstore;

import com.group3.monitorClient.MonitorClientInterface;
import com.group3.monitorClient.controller.Controller;
import com.group3.monitorClient.controller.requirements.AvailableCPURequirement;
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
		Controller controller = new Controller();
		controller.addRequirement(new AvailableCPURequirement(0)); //TODO: Should be added to the configuration.properties and configurationManger such that i can handle doubles.
		controller.addThread(Messenger.getInstance());
		SpringApplication.run(PetstoreApplication.class, args);

	}
}
