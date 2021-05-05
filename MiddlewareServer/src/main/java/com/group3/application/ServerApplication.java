package com.group3.application;

import com.group3.monitorClient.configuration.ConfigurationManager;
import com.group3.monitorClient.controller.Controller;
import com.group3.monitorClient.controller.requirements.AvailableCPURequirement;
import com.group3.monitorClient.messenger.Messenger;
import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.openapitools.api", "com.dummy.netbanking.controller"})
public class ServerApplication {

	public static void main(String[] args) {
		Controller controller = new Controller();
		controller.addRequirement(new AvailableCPURequirement(ConfigurationManager.getInstance().getPropertyAsDouble(ConfigurationManager.availableCPURequirementProp, 0.2)));
		controller.addThread(Messenger.getInstance());
		controller.start();
		SpringApplication.run(ServerApplication.class, args);
	}
}
