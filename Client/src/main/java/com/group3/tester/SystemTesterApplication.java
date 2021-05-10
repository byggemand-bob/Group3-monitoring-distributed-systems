package com.group3.tester;

import com.group3.monitorClient.configuration.ConfigurationManager;
import com.group3.monitorClient.controller.Controller;
import com.group3.monitorClient.controller.requirements.AvailableCPURequirement;
import com.group3.monitorClient.messenger.Messenger;
import org.openapitools.client.ApiException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class SystemTesterApplication {

	public static void main(String[] args) throws IOException, ApiException {
		SpringApplication.run(SystemTesterApplication.class, args);
	}
}

//Controller controller = new Controller();
//controller.addRequirement(new AvailableCPURequirement(ConfigurationManager.getInstance().getPropertyAsDouble(ConfigurationManager.availableCPURequirementProp, 0.2)));
//controller.addThread(Messenger.getInstance());
//controller.start();