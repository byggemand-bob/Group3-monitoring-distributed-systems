package com.group3.tester;

import java.lang.reflect.Type;

import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;

public class SystemTestData {

	private okhttp3.Call call;
	private ApiClient client;
	private Type returnType;
	
	public SystemTestData(okhttp3.Call call, ApiClient client, Type returnType) {
		this.call = call;
		this.client = client;
		this.returnType = returnType;
	}
	
	public SystemTestData(okhttp3.Call call, ApiClient client) {
		this(call, client, null);
	}
	
	public void execute() throws ApiException {
		client.execute(call, returnType);
	}
}
