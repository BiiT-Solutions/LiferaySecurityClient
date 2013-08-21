package com.biit.liferay.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import org.apache.log4j.Logger;

public class LiferayAuthenticationClientLogger {
	private static final Logger logger = Logger.getLogger("org.biit.liferay.access");

	private LiferayAuthenticationClientLogger() {
	}

	private static void info(String message) {
		logger.info(message);
	}

	public static void info(String className, String message) {
		info(className + ": " + message);
	}

	private static void warning(String message) {
		logger.warn(message);
	}

	public static void warning(String className, String message) {
		warning(className + ": " + message);
	}

	private static void debug(String message) {
		logger.debug(message);
	}

	public static void debug(String className, String message) {
		debug(className + ": " + message);
	}

	private static void error(String message) {
		logger.error(message);
	}

	public static void error(String className, String message) {
		error(className + ": " + message);
	}

	private static void fatal(String message) {
		logger.fatal(message);
	}

	public static void fatal(String className, String message) {
		fatal(className + ": " + message);
	}

	public static void entering(String className, String method) {
		debug(className, "ENTRY (" + method + ")");
	}

	public static void exiting(String className, String method) {
		debug(className, "RETURN (" + method + ")");
	}

	public static void errorMessage(String className, Throwable throwable) {
		String error = getStackTrace(throwable);
		error(className, error);
	}

	private static String getStackTrace(Throwable throwable) {
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		throwable.printStackTrace(printWriter);
		return writer.toString();
	}
}
