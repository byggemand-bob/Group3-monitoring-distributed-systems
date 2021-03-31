package com.group3.monitor.api.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import com.group3.monitor.api.MonitorApi;
import com.group3.monitor.model.TimingMonitorData;
import com.group3.monitor.model.TimingMonitorData.EventCodeEnum;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

public class MonitorApiTest {
	
	public static MockWebServer mockBackend;
	public MonitorApi api;
	
	@BeforeAll
	public static void setUp() throws IOException {
		mockBackend = new MockWebServer();
		mockBackend.start();
	}
	
	@AfterAll
	public static void tearDown() throws IOException {
		mockBackend.shutdown();
	}
	
	@BeforeEach
	public void initializa() {
		// Set the base URL with the port the backend server is running on.
		String baseUrl = String.format("http://localhost:%s", mockBackend.getPort());
		ApiClient client = new ApiClient();
		client.setBasePath(baseUrl);
		api = new MonitorApi(client);
	}
	
	@Test
	public void testSendTiminingMonitorDataWithResponseCode200OK() throws ApiException, InterruptedException, ClassNotFoundException, IOException {
		// Setup data to send and/or receive
		final long eventId = 1L;
		final long senderId = 30L;
		final String targetEndpoint = "/monitor";
		
		TimingMonitorData timing1 = new TimingMonitorData();
		OffsetDateTime now = OffsetDateTime.now();
		timing1.setEventCode(EventCodeEnum.SENDREQUEST);
		timing1.setEventID(eventId);
		timing1.setSenderID(senderId);
		timing1.setTargetEndpoint(targetEndpoint);
		timing1.setTimestamp(now);
		
		// Object with information about received request in MockServer
		RecordedRequest request;
		
		// Enqueue response in mock backend
		// Here body, headers, params, and other can be added to the response
		// These are sent back in FIFO sequence
		mockBackend.enqueue(new MockResponse().setResponseCode(HttpStatus.OK.value()));
		
		// Make the call, should receive OK
		api.addMonitorData(timing1);
		request = mockBackend.takeRequest();
		assertEquals(HttpMethod.POST.name(), request.getMethod());
		assertEquals(targetEndpoint, request.getPath());
	}
	
	@Test
	public void testSendTimingMonitorDataWithResponseCode404NotFound() {
		// Setup data to send and/or receive
		final long eventId = 1L;
		final long senderId = 30L;
		final String targetEndpoint = "/monitor";
		
		TimingMonitorData timing1 = new TimingMonitorData();
		OffsetDateTime now = OffsetDateTime.now();
		timing1.setEventCode(EventCodeEnum.SENDREQUEST);
		timing1.setEventID(eventId);
		timing1.setSenderID(senderId);
		timing1.setTargetEndpoint(targetEndpoint);
		timing1.setTimestamp(now);
		
		// Object with information about received request in MockServer
		RecordedRequest request;
		
		// Enqueue response in mock backend
		// Here body, headers, params, and other can be added to the response
		// These are sent back in FIFO sequence
		mockBackend.enqueue(new MockResponse().setResponseCode(HttpStatus.NOT_FOUND.value()));
		
		// Make the call, this will receive NOT_FOUND and throw an ApiException
		assertThrows(ApiException.class, () -> {
			api.addMonitorData(timing1);		
		});		
	}
	
	@Test
	public void testSendTimingMonitorDataWithResponseCode500InternalServerError() {
		// Setup data to send and/or receive
		final long eventId = 1L;
		final long senderId = 30L;
		final String targetEndpoint = "/monitor";
		
		TimingMonitorData timing1 = new TimingMonitorData();
		OffsetDateTime now = OffsetDateTime.now();
		timing1.setEventCode(EventCodeEnum.SENDREQUEST);
		timing1.setEventID(eventId);
		timing1.setSenderID(senderId);
		timing1.setTargetEndpoint(targetEndpoint);
		timing1.setTimestamp(now);
		
		// Object with information about received request in MockServer
		RecordedRequest request;
		
		// Enqueue response in mock backend
		// Here body, headers, params, and other can be added to the response
		// These are sent back in FIFO sequence
		mockBackend.enqueue(new MockResponse().setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value()));
		
		// Make the call, this will receive INTERNAL SERVER ERROR and throw an ApiException
		assertThrows(ApiException.class, () -> {
			api.addMonitorData(timing1);		
		});
	}

}
