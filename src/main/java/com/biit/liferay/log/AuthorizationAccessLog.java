package com.biit.liferay.log;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class AuthorizationAccessLog {
	private static final Logger logger = Logger.getLogger("AuthorizationAccessLog");
	private static final Level logLevel = Level.ALL; // INFO, OFF, ALL, ...
	private static final int MAX_BYTES = 50000000;
	private static final int NUMBER_MAX_OF_FILES = 10;

	static {
		try {
			FileHandler fh = new FileHandler(System.getProperty("java.io.tmpdir") + File.separator
					+ "AuthorizationAccessLog.log", MAX_BYTES, NUMBER_MAX_OF_FILES, true);
			logger.addHandler(fh);
			logger.setLevel(logLevel);
			// fh.setFormatter(new SimpleFormatter());
			fh.setFormatter(getCustomFormatter());
		} catch (IOException ex) {
			AuthorizationAccessLog.severe(AuthorizationAccessLog.class.getName(),
					"Logger failed. Probably the log file can not be created. Error Message: " + ex.getMessage());
		} catch (SecurityException ex) {
			AuthorizationAccessLog.severe(AuthorizationAccessLog.class.getName(),
					"Logger failed. Probably the log file can not be created. Error Message: " + ex.getMessage());
		}
	}

	/**
	 * Defines our own formatter.
	 */
	public static Formatter getCustomFormatter() {
		return new Formatter() {
			// StackTraceElement[] stackTraceElements =
			// Thread.currentThread().getStackTrace();
			@Override
			public String format(LogRecord record) {
				String text = record.getLevel() + " [" + new Date() + "] " + " - " + record.getMessage() + "\n";
				return text;
			}
		};
	}

	private AuthorizationAccessLog() {
	}

	private static void info(String message) {
		logger.info(message);
	}

	public static void info(String className, String message) {
		info(className + ": " + message);
	}

	private static void config(String message) {
		logger.config(message);
	}

	public static void config(String className, String message) {
		config(className + ": " + message);
	}

	private static void warning(String message) {
		logger.warning(message);
	}

	public static void warning(String className, String message) {
		warning(className + ": " + message);
	}

	private static void debug(String message) {
		logger.finest(message);
	}

	public static void debug(String className, String message) {
		debug(className + ": " + message);
	}

	private static void severe(String message) {
		logger.severe(message);
	}

	public static void severe(String className, String message) {
		severe(className + ": " + message);
	}

	private static void fine(String message) {
		logger.fine(message);
	}

	public static void fine(String className, String message) {
		fine(className + ": " + message);
	}

	private static void finer(String message) {
		logger.finer(message);
	}

	public static void finer(String className, String message) {
		finer(className + ": " + message);
	}

	private static void finest(String messsage) {
		logger.finest(messsage);
	}

	public static void finest(String className, String message) {
		finest(className + ": " + message);
	}

	public static void entering(String className, String method) {
		debug(className, "ENTRY (" + method + ")");
	}

	public static void exiting(String className, String method) {
		debug(className, "RETURN (" + method + ")");
	}

	public static void errorMessage(String className, Throwable throwable) {
		String error = getStackTrace(throwable);
		severe(className, error);
	}

	private static String getStackTrace(Throwable throwable) {
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		throwable.printStackTrace(printWriter);
		return writer.toString();
	}
}
