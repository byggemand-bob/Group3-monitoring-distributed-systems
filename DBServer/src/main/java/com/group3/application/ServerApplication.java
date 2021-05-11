package com.group3.application;

import com.dummy.netbanking.database.AccountDBManager;
import com.dummy.netbanking.database.UserDBManager;
import com.group3.monitorClient.configuration.ConfigurationManager;
import com.group3.monitorClient.controller.Controller;
import com.group3.monitorClient.controller.requirements.AvailableCPURequirement;
import com.group3.monitorClient.messenger.Messenger;

import java.sql.SQLException;

import org.openapitools.model.Account;
import org.openapitools.model.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.dummy.netbanking.controller"})//"org.openapitools.api", 
public class ServerApplication {

	public static final boolean withMonitor = true;
	
	public static void main(String[] args) throws SQLException {
		if (withMonitor) {
			Controller controller = new Controller();
			controller.addRequirement(new AvailableCPURequirement(ConfigurationManager.getInstance().getPropertyAsDouble(ConfigurationManager.availableCPURequirementProp, 0.2)));
			controller.addThread(Messenger.getInstance());
			controller.start();			
		}
		SpringApplication.run(ServerApplication.class, args);
		
		//test();
	}
	
	private static void test() throws SQLException {
		UserDBManager userMan = new UserDBManager();
		AccountDBManager accMan = new AccountDBManager();
		
		User user = new User();
		user.setName("Alex");
		user.setPassword("ThisIsVeryLongAndSecurePasswordWhichShouldBeHashed");
		print("Insert user");
		userMan.insertUser(user);
		
		print("select user");
		User selectedUser = userMan.selectUser(user.getName());
		print("seleted user: <" + selectedUser + ">");
		
		print("delete user");
		boolean success = userMan.deleteUser(user.getName());
		print("Delete was succesful? <" + success + ">");
		
		print("");
		print("---------------------------------------------------------------");
		print("");
		print("Insert user for account checking");
		userMan.insertUser(user);
		
		print("Insert accounts");
		Account acc1 = new Account();
		acc1.setName("MyAwesomeAccount");
		acc1.setBalance(1000D);
		acc1.setOwner(user.getName());
		Account acc2 = new Account();
		acc2.setName("MySavingsAccount");
		acc2.setBalance(10000D);
		acc2.setOwner(user.getName());
		accMan.insertAccount(acc1);
		accMan.insertAccount(acc2);
		
		print("Select accounts");
		Account[] retA = accMan.selectAccountsForUser(user.getName());
		print("Selected <" + retA.length + "> accounts:");
		for (Account acc : retA) {
			print("\t" + acc);
		}
		
		print("Delete account");
		boolean retB = accMan.deleteAccount(acc1.getName());
		print("Deletion was success? <" + retB + ">");
		
		print("Select balance");
		double retD = accMan.selectBalance(acc2.getName());
		print("Selected balance <" + retD + ">");
		
		print("Update balance");
		retB = accMan.updateBalance(100D, acc2.getName());
		print("Update of balance was a success? <" + retB + ">");
	}
	
	private static void print(String msg) {
		System.out.println(msg);
	}
}
