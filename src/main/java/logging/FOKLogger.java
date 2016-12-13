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

import common.Common;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;

/**
 * A frame for all log actions. This class utilizes
 * {@link java.util.logging.Logger}
 * 
 * @author Frederik Kammel
 *
 */
public class FOKLogger {

	Logger log;
	private static Handler fileHandler;
	private static Handler consoleHandler;
	private static boolean handlersInitialized;

	/**
	 * Log messages must have the specified log level or higher to be saved in
	 * the log file. {@code Level.ALL} will enable all log levels. Default
	 * Value: {@code Level.ALL}
	 */
	private static Level fileLogLevel = Level.ALL;

	/**
	 * Log messages must have the specified log level or higher to appear on the
	 * console. {@code Level.ALL} will enable all log levels. Default Value:
	 * {@code Level.ALL}
	 */
	private static Level consoleLogLevel = Level.INFO;

	/**
	 * The name scheme for the file name of the log. Use the place holder
	 * {@code DateTime} for the current date and time.
	 */
	private static String logFileName;

	private static String logFilePath;

	/**
	 * @return the fileLogLevel
	 */
    @SuppressWarnings("unused")
	public static Level getFileLogLevel() {
		return fileLogLevel;
	}

	/**
	 * @param newFileLogLevel
	 *            the fileLogLevel to set
	 */
    @SuppressWarnings("unused")
	public static void setFileLogLevel(Level newFileLogLevel) {
		fileLogLevel = newFileLogLevel;

		// set the handlers Log Levels
		fileHandler.setLevel(fileLogLevel);
		consoleHandler.setLevel(consoleLogLevel);
	}

	/**
	 * @return the consoleLogLevel
	 */
    @SuppressWarnings("unused")
	public static Level getConsoleLogLevel() {
		return consoleLogLevel;
	}

	/**
	 * @param newConsoleLogLevel
	 *            the consoleLogLevel to set
	 */
    @SuppressWarnings("unused")
	public static void setConsoleLogLevel(Level newConsoleLogLevel) {
		consoleLogLevel = newConsoleLogLevel;

		// set the handlers Log Levels
		fileHandler.setLevel(fileLogLevel);
		consoleHandler.setLevel(consoleLogLevel);
	}

	/**
	 * @return the logFileName
	 */
	@SuppressWarnings("unused")
	public static String getLogFileName() {
		return logFileName;
	}

	/**
	 * @return the logFilePath
	 */
	@SuppressWarnings("unused")
	public static String getLogFilePath() {
		return logFilePath;
	}

	public static String getLogFilePathAndName() {
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
		this(className, Common.getAppDataPath() + "Logs", "log_" + Common.getAppName() + "_DateTime.xml");
	}

	/**
	 * Creates a new {@link java.util.logging.Logger} instance and attaches
	 * automatically a {@link FileHandler} and a {@link ConsoleHandler}.
	 * 
	 * @param className
	 *            The name of the calling class. It is recommended to use the
	 *            fully qualified class name that you can get with
	 *            {@code (YourClassName).class.getName()}.
	 * @param newLogFilePath
	 *            The file where the log file shall be saved in
	 * @param newLogFileName
	 *            The name of the log file
	 */
	public FOKLogger(String className, String newLogFilePath, String newLogFileName) {
		logFilePath = newLogFilePath;
		logFileName = newLogFileName;

		// initialize the handlers
		if (!handlersInitialized) {
			FOKLogger.initLogHandlers();
		}

		log = Logger.getLogger(className);
		log.setLevel(Level.ALL);
	}

	public static void initLogHandlers() {

		handlersInitialized = true;

		fileHandler = null;
		consoleHandler = null;
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
							setFormatter(new OneLineFormatter());
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
			System.out.println("Cannot read a config file for logging, thus using the default configuration. Reason: "
					+ e.getLocalizedMessage());
		}

		System.out.println("Saving log file \n" + getLogFilePathAndName());

		// Remove all existing parent handlers
		Logger globalLogger = Logger.getLogger("");
		Handler[] handlers = globalLogger.getHandlers();
		//noinspection ForLoopReplaceableByForEach
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
