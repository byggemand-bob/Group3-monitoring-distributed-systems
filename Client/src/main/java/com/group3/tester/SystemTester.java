package com.group3.tester;

import java.io.PrintStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.AccountApiClient;
import org.openapitools.client.api.UserApiClient;
import org.openapitools.client.model.Account;
import org.openapitools.client.model.User;

import com.google.gson.reflect.TypeToken;

public class SystemTester {

	private AccountApiClient account;
	private UserApiClient user;
	private static final String path = "http://localhost:%s";
	private long clock = 0;
	private List<Long> results;
	
	List<SystemTestData> testData;
	
	public SystemTester(String port) {
		ApiClient client = new ApiClient();
		client.setBasePath(String.format(path, port));
		
		account = new AccountApiClient(client);
		user = new UserApiClient(client);
		
		testData = new ArrayList<SystemTestData>();
		results = new ArrayList<Long>();
	}
	
	public void initializeTestData(final long repetitions) throws ApiException {
		int userSuffix = 0;
		int accountSuffix = 0;
		final String userPrefix = "User";
		final String accountPrefix = "Account";
		final String password = "p4ssword";
		
		for (int i = 0; i < repetitions; i++) {
			// Create 3 user
			User u1 = createUser(userPrefix + ++userSuffix, password);
			okhttp3.Call call1 = user.createNewUserCall(u1, null);
			SystemTestData data1 = new SystemTestData(call1, user.getApiClient());
			testData.add(data1);
			
			User u2 = createUser(userPrefix + ++userSuffix, password);
			okhttp3.Call call2 = user.createNewUserCall(u2, null);
			SystemTestData data2 = new SystemTestData(call2, user.getApiClient());
			testData.add(data2);

			User u3 = createUser(userPrefix + ++userSuffix, password);
			okhttp3.Call call3 = user.createNewUserCall(u3, null);
			SystemTestData data3 = new SystemTestData(call3, user.getApiClient());
			testData.add(data3);
			
			// Add 7 accounts
			Account a1 = createAccount(accountPrefix + ++accountSuffix, u1.getName());
			okhttp3.Call call4 = account.createAccountCall(a1, null);
			SystemTestData data4 = new SystemTestData(call4, account.getApiClient());
			testData.add(data4);
			
			Account a2 = createAccount(accountPrefix + ++accountSuffix, u2.getName());
			okhttp3.Call call5 = account.createAccountCall(a2, null);
			SystemTestData data5 = new SystemTestData(call5, account.getApiClient());
			testData.add(data5);
			
			Account a3 = createAccount(accountPrefix + ++accountSuffix, u3.getName());
			okhttp3.Call call6 = account.createAccountCall(a3, null);
			SystemTestData data6 = new SystemTestData(call6, account.getApiClient());
			testData.add(data6);
			
			Account a4 = createAccount(accountPrefix + ++accountSuffix, u1.getName());
			okhttp3.Call call7 = account.createAccountCall(a4, null);
			SystemTestData data7 = new SystemTestData(call7, account.getApiClient());
			testData.add(data7);
			
			Account a5 = createAccount(accountPrefix + ++accountSuffix, u2.getName());
			okhttp3.Call call8 = account.createAccountCall(a5, null);
			SystemTestData data8 = new SystemTestData(call8, account.getApiClient());
			testData.add(data8);
			
			Account a6 = createAccount(accountPrefix + ++accountSuffix, u3.getName());
			okhttp3.Call call9 = account.createAccountCall(a6, null);
			SystemTestData data9 = new SystemTestData(call9, account.getApiClient());
			testData.add(data9);
			
			Account a7 = createAccount(accountPrefix + ++accountSuffix, u1.getName());
			okhttp3.Call call10 = account.createAccountCall(a7, null);
			SystemTestData data10 = new SystemTestData(call10, account.getApiClient());
			testData.add(data10);
			
			// Delete 1 user
			okhttp3.Call call11 = user.deleteUserCall(u2.getName(), null);
			SystemTestData data11 = new SystemTestData(call11, user.getApiClient());
			testData.add(data11);
			
			// Delete 2 accounts
			okhttp3.Call call12 = account.deleteAccountCall(a6.getName(), null);
			SystemTestData data12 = new SystemTestData(call12, account.getApiClient());
			testData.add(data12);

			okhttp3.Call call13 = account.deleteAccountCall(a1.getName(), null);
			SystemTestData data13 = new SystemTestData(call13, account.getApiClient());
			testData.add(data13);
			
			// Get 5 account balances
			okhttp3.Call call14 = account.getAccountBalanceCall(a3.getName(), null);
			Type returnType1 = new TypeToken<Double>(){}.getType();
			SystemTestData data14 = new SystemTestData(call14, account.getApiClient(), returnType1);
			testData.add(data14);

			okhttp3.Call call15 = account.getAccountBalanceCall(a4.getName(), null);
			Type returnType2 = new TypeToken<Double>(){}.getType();
			SystemTestData data15 = new SystemTestData(call15, account.getApiClient(), returnType2);
			testData.add(data15);
			
			okhttp3.Call call16 = account.getAccountBalanceCall(a7.getName(), null);
			Type returnType3 = new TypeToken<Double>(){}.getType();
			SystemTestData data16 = new SystemTestData(call16, account.getApiClient(), returnType3);
			testData.add(data16);
			
			okhttp3.Call call17 = account.getAccountBalanceCall(a3.getName(), null);
			Type returnType4 = new TypeToken<Double>(){}.getType();
			SystemTestData data17 = new SystemTestData(call17, account.getApiClient(), returnType4);
			testData.add(data17);
			
			okhttp3.Call call18 = account.getAccountBalanceCall(a4.getName(), null);
			Type returnType5 = new TypeToken<Double>(){}.getType();
			SystemTestData data18 = new SystemTestData(call18, account.getApiClient(), returnType5);
			testData.add(data18);
			
			// Get 2 users
			okhttp3.Call call19 = user.getUserCall(u1.getName(), null);
			Type returnType6 = new TypeToken<User>() {}.getType();
			SystemTestData data19 = new SystemTestData(call19, user.getApiClient(), returnType6);
			testData.add(data19);

			okhttp3.Call call20 = user.getUserCall(u3.getName(), null);
			Type returnType7 = new TypeToken<User>() {}.getType();
			SystemTestData data20 = new SystemTestData(call20, user.getApiClient(), returnType7);
			testData.add(data20);
			
			// Get all accounts 1 user
			okhttp3.Call call21 = account.getUsersAllAccountCall(u1.getName(), null);
			Type returnType8 = new TypeToken<List<User>>() {}.getType();
			SystemTestData data21 = new SystemTestData(call21, account.getApiClient(), returnType8);
			testData.add(data21);
			
			// Update 4 balances
			okhttp3.Call call22 = account.updateAccountBalanceCall(a3.getName(), 700D, null);
			SystemTestData data22 = new SystemTestData(call22, account.getApiClient());
			testData.add(data22);

			okhttp3.Call call23 = account.updateAccountBalanceCall(a4.getName(), 55.55D, null);
			SystemTestData data23 = new SystemTestData(call23, account.getApiClient());
			testData.add(data23);
			
			okhttp3.Call call24 = account.updateAccountBalanceCall(a7.getName(), -10.99D, null);
			SystemTestData data24 = new SystemTestData(call24, account.getApiClient());
			testData.add(data24);
			
			okhttp3.Call call25 = account.updateAccountBalanceCall(a3.getName(), -1700D, null);
			SystemTestData data25 = new SystemTestData(call25, account.getApiClient());
			testData.add(data25);
		}
	}
	
	private Account createAccount(String accName, String owner) {
		Account acc = new Account();
		acc.setName(accName);
		acc.setBalance(10000D);
		acc.setOwner(owner);
		return acc;
	}
	
	private User createUser(String userName, String password) {
		User user = new User();
		user.setName(userName);
		user.setPassword(password);
		return user;
	}
	
	public void run() throws ApiException {
		for (SystemTestData data : testData) {
			data.execute();
		}
	}
	
	public void clock(boolean start) {
		if (start) {
			clock = System.currentTimeMillis();
		} else {
			results.add(System.currentTimeMillis() - clock);
		}
	}
	
	public void clear() {
		testData.clear();
	}

	public void printResult(PrintStream out) {
		for (int i = 0; i < results.size(); i++) {
			out.println("\tResult of Run <" + i+1 + ">: clocked time <" + results.get(i) + "ms>");
		}
	}
}
