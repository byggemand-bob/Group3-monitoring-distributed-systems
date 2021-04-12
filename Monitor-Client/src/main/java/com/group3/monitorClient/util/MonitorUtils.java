package com.group3.monitorClient.util;

public class MonitorUtils {

	public static String GetCallingMethodName(int elementIndex) {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		StackTraceElement element = stackTrace[elementIndex];
		return element.getMethodName();
	}
}
