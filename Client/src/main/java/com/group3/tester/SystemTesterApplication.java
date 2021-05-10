package com.group3.tester;

import org.openapitools.client.ApiException;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class SystemTesterApplication {

	public static void main(String[] args) throws IOException, ApiException {
		
		if (args.length < 3) {
			System.out.println("################## Usage ##################");
			System.out.println("Arguments");
			System.out.println("\t[0]<port_number>: The port number that the testing request are sent to");
			System.out.println("\t[1]<repetitions>: The number of repetitions of 25 requests that are sent as testing data");
			System.out.println("\t[2]<Repeats>: The number of times the test is repeated");
			System.out.println("###########################################");
			System.exit(1);
		}
		
		System.out.println("Starting system test on port <" + args[0] + "> and with <" + (Integer.parseInt(args[1]) * 25) + "> requests and repeated <" + args[2] + "> times!");
		System.out.println("Results:");
		SystemTester tester = new SystemTester(args[0]);
		final long t0 = System.currentTimeMillis();
		final int repeats = Integer.parseInt(args[2]);
		for (int i = 0; i < repeats; i++) {	
			tester.initializeTestData(Long.parseLong(args[1]));
			tester.clock(true);
			tester.run();
			tester.clock(false);
			tester.clear();
			tester.printResult(System.out, i);
		}
		final long t1 = System.currentTimeMillis();
		System.out.println("Ended system test after <" + (t1 - t0) + "ms> on port <" + args[0] + "> and with <" + (Integer.parseInt(args[1]) * 25) + "> requests and repeated <" + args[2] + "> times!");
	}
}

//Controller controller = new Controller();
//controller.addRequirement(new AvailableCPURequirement(ConfigurationManager.getInstance().getPropertyAsDouble(ConfigurationManager.availableCPURequirementProp, 0.2)));
//controller.addThread(Messenger.getInstance());
//controller.start();
//SpringApplication.run(SystemTesterApplication.class, args);