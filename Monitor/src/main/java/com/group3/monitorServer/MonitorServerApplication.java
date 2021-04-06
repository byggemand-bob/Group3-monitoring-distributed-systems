package com.group3.monitorServer;

import com.group3.monitorServer.controller.messages.MessageCreator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.OffsetDateTime;

@SpringBootApplication
public class MonitorServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonitorServerApplication.class, args);
		String timestamp = "1990-12-31T23:59:60Z";

		MessageCreator messageCreator = new MessageCreator();

		OffsetDateTime javatime = messageCreator.ConvertStringToDateTime(timestamp);


	}

}
