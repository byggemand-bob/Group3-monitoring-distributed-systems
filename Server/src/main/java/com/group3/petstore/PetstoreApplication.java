package com.group3.petstore;

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
		SQLManager sqlManager = SQLManager.getInstance();
		if (sqlManager.getPath() == null || sqlManager.getFileName() == null) {
			sqlManager.Connect("src/main/resources/sqlite/db", "queue.db");
		}
		//TODO: User-specification something
		SpringApplication.run(PetstoreApplication.class, args);
	}
}
