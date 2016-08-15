package logging;

/*
 * #%L
 * examTrainer
 * %%
 * Copyright (C) 2016 Frederik Kammel
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import java.io.File;
import java.io.IOException;
import java.util.logging.*;

import common.Common;

/**
 * A frame for all log actions. This class utilizes
 * {@link java.util.logging.Logger}
 * 
 * @author Frederik Kammel
 *
 */
public class FOKLogger {

	Logger log;
	// private static Handler fileHandler;
	// private static Handler consoleHandler;
	private static boolean handlersInitialized;
	
	/**
	 * Log messages must have the specified log level or higher to be saved in
	 * the log file. {@code Level.ALL} will enable all log levels. Default Value: {@code Level.ALL}
	 */
	private static Level fileLogLevel = Level.ALL;

	/**
	 * Log messages must have the specified log level or higher to appear on the
	 * console. {@code Level.ALL} will enable all log levels. Default Value: {@code Level.ALL}
	 */
	private static Level consoleLogLevel = Level.ALL;
	
	/**
	 * The name scheme for the file name of the log. Use the place holder
	 * {@code DateTime} for the current date and time.
	 */
	private static String logFileName = "log_" + Common.getAppName() + "_DateTime.xml";
	
	private static String logFilePath = Common.getAppDataPath() + "Logs";

	/**
	 * @return the fileLogLevel
	 */
	public static Level getFileLogLevel() {
		return fileLogLevel;
	}

	/**
	 * @param fileLogLevel the fileLogLevel to set
	 */
	public static void setFileLogLevel(Level newFileLogLevel) {
		fileLogLevel = newFileLogLevel;
	}

	/**
	 * @return the consoleLogLevel
	 */
	public static Level getConsoleLogLevel() {
		return consoleLogLevel;
	}

	/**
	 * @param consoleLogLevel the consoleLogLevel to set
	 */
	public static  void setConsoleLogLevel(Level newConsoleLogLevel) {
		consoleLogLevel = newConsoleLogLevel;
	}

	/**
	 * @return the logFileName
	 */
	public static String getLogFileName() {
		return logFileName;
	}

	/**
	 * @param logFileName the logFileName to set
	 */
	public static void setLogFileName(String newLogFileName) {
		logFileName = newLogFileName;
	}

	/**
	 * @return the logFilePath
	 */
	public static String getLogFilePath() {
		return logFilePath;
	}

	/**
	 * @param logFilePath the logFilePath to set
	 */
	public static void setLogFilePath(String newLogFilePath) {
		logFilePath = newLogFilePath;
	}
	
	public static String getLogFilePathAndName(){
		return logFilePath + File.separator + logFileName.replace("DateTime", Common.getLaunchTimeStamp());
	}

	/**
	 * Creates a new {@link java.util.logging.Logger} instance and attaches
	 * automatically a {@link FileHandler} and a {@link ConsoleHandler}.
	 * 
	 * @param className
	 *            The name of the calling class. It is recommended to use the
	 *            fully qualified class name that you can get with
	 *            {@code (YourClassName).class.getName()}.
	 */
	public FOKLogger(String className) {
		// initialize the handlers
		if (!handlersInitialized) {
			FOKLogger.initLogHandlers();
		}

		log = Logger.getLogger(className);
		log.setLevel(Level.ALL);
	}

	public static void initLogHandlers() {
		
		handlersInitialized = true;

		Handler fileHandler = null;
		Handler consoleHandler = null;
		// Create a directory; all non-existent ancestor directories are
		// automatically created
		// Source:
		// http://stackoverflow.com/questions/4801971/how-to-create-empty-folder-in-java
		(new File(logFilePath)).mkdirs();

		try {
			fileHandler = new FileHandler(getLogFilePathAndName());
			consoleHandler = new Handler() {
				@Override
				public void publish(LogRecord record) {
					if (record.getLevel().intValue() >= this.getLevel().intValue()) {
						if (getFormatter() == null) {
							setFormatter(new SimpleFormatter());
						}

						try {
							String message = getFormatter().format(record);
							if (record.getLevel().intValue() >= Level.WARNING.intValue()) {
								System.err.write(message.getBytes());
							} else {
								System.out.write(message.getBytes());
							}
						} catch (Exception exception) {
							reportError(null, exception, ErrorManager.FORMAT_FAILURE);
						}
					}

				}

				@Override
				public void close() throws SecurityException {
				}

				@Override
				public void flush() {
				}
			};
		} catch (IOException e) {
			// Not logging into the logger as it is not yet initialized.
			e.printStackTrace();
		}

		// set the handlers Log Levels
		fileHandler.setLevel(fileLogLevel);
		consoleHandler.setLevel(consoleLogLevel);

		// Try to read a config file
		System.setProperty("java.util.logging.config.file", "logging.properties");
		try {
			LogManager.getLogManager().readConfiguration();
		} catch (Exception e) {
			System.out.println("Cannot read a config file for logging, thus using the default configuration. Reason: " + e.getLocalizedMessage());
		}

		System.out.println("Saving log file \n" + getLogFilePathAndName());

		// Remove all existing parent handlers
		Logger globalLogger = Logger.getLogger("");
		Handler[] handlers = globalLogger.getHandlers();
		for (int i = 0; i < handlers.length; i++) {
			globalLogger.removeHandler(handlers[i]);
		}

		globalLogger.addHandler(fileHandler);
		globalLogger.addHandler(consoleHandler);
	}

	/**
	 * Returns the actual Logger object by {@link java.util.logging.Logger}
	 * 
	 * @return The actual Logger object by {@link java.util.logging.Logger}
	 */
	public Logger getLogger() {
		return log;
	}
}
