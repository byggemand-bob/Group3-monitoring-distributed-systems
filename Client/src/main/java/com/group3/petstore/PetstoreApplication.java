package com.group3.petstore;

import com.group3.monitorClient.configuration.ConfigurationManager;
import com.group3.monitorClient.controller.Controller;
import com.group3.monitorClient.controller.requirements.AvailableCPURequirement;
import com.group3.monitorClient.messenger.Messenger;
import org.openapitools.client.ApiException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class PetstoreApplication {

	public static void main(String[] args) throws IOException, ApiException {
		Controller controller = new Controller();
		controller.addRequirement(new AvailableCPURequirement(ConfigurationManager.getInstance().getPropertyAsDouble(ConfigurationManager.availableCPURequirementProp, 0.2)));
		controller.addThread(Messenger.getInstance());
		controller.start();
		SpringApplication.run(PetstoreApplication.class, args);
	}
}
