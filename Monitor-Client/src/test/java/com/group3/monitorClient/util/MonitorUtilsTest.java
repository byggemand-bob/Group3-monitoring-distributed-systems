package com.group3.monitorClient.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MonitorUtilsTest {

	@Test
	void GetMethodNameUsingMonitorUtilsGetCallingMethodName() {
		String actual = TestMethod(2);
		String execpted = "TestMethod";
		
		assertSame(execpted, actual);
	}
	
	@Test
	void GetTestNameUsingMonitorUtilsGetcallingMethodName() {
		String actual = TestMethod(3);
		String execpted = "GetTestNameUsingMonitorUtilsGetcallingMethodName";
		
		assertSame(execpted, actual);
	}
	
	@Test
	void IndexOutOfBoundTest() {
		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
			TestMethod(-1);
		});
	}
	
	private String TestMethod(int index) {
		return MonitorUtils.GetCallingMethodName(index);
	}

}
