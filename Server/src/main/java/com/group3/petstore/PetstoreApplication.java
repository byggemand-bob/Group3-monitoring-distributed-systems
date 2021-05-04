package com.group3.petstore;

import com.group3.monitorClient.controller.Controller;
import com.group3.monitorClient.controller.requirements.AvailableCPURequirement;
import com.group3.monitorClient.messenger.Messenger;
import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "org.openapitools.api")
public class PetstoreApplication {

	public static void main(String[] args) {
		Messenger.initialize("src" + File.separator + "main" + File.separator + "resources" + File.separator + "sqlite" + File.separator + "db" + File.separator, "queue.db");//TODO: User-specification paths instead
		Messenger.getInstance().start();
		Controller controller = new Controller();
		controller.addRequirement(new AvailableCPURequirement(0)); //TODO: Should be added to the configuration.properties and configurationManger such that i can handle doubles.
		controller.addThread(Messenger.getInstance());
		controller.start();
		SpringApplication.run(PetstoreApplication.class, args);

	}
}
