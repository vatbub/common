package com.github.vatbub.common.core.logging;

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

import com.github.vatbub.common.core.Common;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Supplier;
import java.util.logging.*;

/**
 * A frame for all log actions. This class utilizes
 * {@link java.util.logging.Logger}
 *
 * @author Frederik Kammel
 */
@SuppressWarnings("unused")
public class FOKLogger {

    private static final Map<String, FOKLogger> loggerMap = new HashMap<>();
    //log uncaught exceptions
    private static final Thread.UncaughtExceptionHandler logUncaughtException = ((thread, throwable) -> FOKLogger.log(throwable.getStackTrace()[0].getClassName(), Level.SEVERE, "An uncaught exception occurred in the thread " + thread.getName(), throwable));
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
    final Logger log;

    /**
     * Creates a new {@link java.util.logging.Logger} instance and attaches
     * automatically a {@link FileHandler} and a {@link ConsoleHandler}.<br>
     * <b>Constructor will soon be declared private!!! Use {@link FOKLogger#getLoggerByClassName(String)} instead</b>
     *
     * @param className The name of the calling class. It is recommended to use the
     *                  fully qualified class name that you can get with
     *                  {@code (YourClassName).class.getName()}.
     */
    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated
    public FOKLogger(String className) {
        this(className, Common.getAppDataPath() + "Logs", "log_" + Common.getAppName() + "_DateTime.xml");
    }

    /**
     * Creates a new {@link java.util.logging.Logger} instance and attaches
     * automatically a {@link FileHandler} and a {@link ConsoleHandler}.
     *
     * @param className      The name of the calling class. It is recommended to use the
     *                       fully qualified class name that you can get with
     *                       {@code (YourClassName).class.getName()}.
     * @param newLogFilePath The file where the log file shall be saved in
     * @param newLogFileName The name of the log file
     */
    public FOKLogger(String className, String newLogFilePath, String newLogFileName) {
        logFilePath = newLogFilePath;
        logFileName = newLogFileName;
        loggerMap.put(className, this);

        // initialize the handlers
        if (!handlersInitialized) {
            FOKLogger.initLogHandlers();
        }

        log = Logger.getLogger(className);
        log.setLevel(Level.ALL);
    }

    /**
     * @return the fileLogLevel
     */
    @SuppressWarnings("unused")
    public static Level getFileLogLevel() {
        return fileLogLevel;
    }

    /**
     * @param newFileLogLevel the fileLogLevel to set
     */
    @SuppressWarnings("unused")
    public static void setFileLogLevel(Level newFileLogLevel) {
        fileLogLevel = newFileLogLevel;

        // set the handlers Log Levels
        if (handlersInitialized) {
            fileHandler.setLevel(fileLogLevel);
        }
    }

    /**
     * @return the consoleLogLevel
     */
    @SuppressWarnings("unused")
    public static Level getConsoleLogLevel() {
        return consoleLogLevel;
    }

    /**
     * @param newConsoleLogLevel the consoleLogLevel to set
     */
    @SuppressWarnings("unused")
    public static void setConsoleLogLevel(Level newConsoleLogLevel) {
        consoleLogLevel = newConsoleLogLevel;

        // set the handlers Log Levels
        if (handlersInitialized) {
            consoleHandler.setLevel(consoleLogLevel);
        }
    }

    /**
     * @return the logFileName
     */
    @SuppressWarnings("unused")
    public static String getLogFileName() {
        return logFileName.replace("DateTime", Common.getLaunchTimeStamp());
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
                public void flush() {
                }

                @Override
                public void close() throws SecurityException {
                }
            };
        } catch (IOException e) {
            // Not com.github.vatbub.common.core.logging into the logger as it is not yet initialized.
            e.printStackTrace();
        }

        // set the handlers Log Levels
        fileHandler.setLevel(fileLogLevel);
        consoleHandler.setLevel(consoleLogLevel);

        // Remove all existing parent handlers
        Logger globalLogger = Logger.getLogger("");
        Handler[] handlers = globalLogger.getHandlers();
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < handlers.length; i++) {
            globalLogger.removeHandler(handlers[i]);
        }

        globalLogger.addHandler(fileHandler);
        globalLogger.addHandler(consoleHandler);

        FOKLogger.info(FOKLogger.class.getName(), "Saving log file \n" + getLogFilePathAndName());
    }

    public static FOKLogger getLoggerByClassName(String className) {
        if (loggerMap.containsKey(className)) {
            // logger exists already
            return loggerMap.get(className);
        } else {
            // create a new logger
            //noinspection deprecation
            return new FOKLogger(className);
        }
    }

    @SuppressWarnings("unused")
    public static void log(String className, LogRecord record) {
        getLoggerByClassName(className).getLogger().log(record);
    }

    public static void log(String className, Level level, String msg) {
        getLoggerByClassName(className).getLogger().log(level, msg);
    }

    public static void log(String className, Level level, Supplier<String> msgSupplier) {
        getLoggerByClassName(className).getLogger().log(level, msgSupplier);
    }

    @SuppressWarnings("unused")
    public static void log(String className, Level level, String msg, Object param1) {
        getLoggerByClassName(className).getLogger().log(level, msg, param1);
    }

    @SuppressWarnings("unused")
    public static void log(String className, Level level, String msg, Object params[]) {
        getLoggerByClassName(className).getLogger().log(level, msg, params);
    }

    @SuppressWarnings("unused")
    public static void log(String className, Level level, String msg, Throwable thrown) {
        getLoggerByClassName(className).getLogger().log(level, msg, thrown);
    }

    @SuppressWarnings("unused")
    public static void log(String className, Level level, Throwable thrown, Supplier<String> msgSupplier) {
        getLoggerByClassName(className).getLogger().log(level, thrown, msgSupplier);
    }

    @SuppressWarnings("unused")
    public static void logp(String className, Level level, String sourceClass, String sourceMethod, String msg) {
        getLoggerByClassName(className).getLogger().logp(level, sourceClass, sourceMethod, msg);
    }

    @SuppressWarnings("unused")
    public static void logp(String className, Level level, String sourceClass, String sourceMethod, Supplier<String> msgSupplier) {
        getLoggerByClassName(className).getLogger().logp(level, sourceClass, sourceMethod, msgSupplier);
    }

    @SuppressWarnings("unused")
    public static void logp(String className, Level level, String sourceClass, String sourceMethod,
                            String msg, Object param1) {
        getLoggerByClassName(className).getLogger().logp(level, sourceClass, sourceMethod, msg, param1);
    }

    @SuppressWarnings("unused")
    public static void logp(String className, Level level, String sourceClass, String sourceMethod,
                            String msg, Object params[]) {
        getLoggerByClassName(className).getLogger().logp(level, sourceClass, sourceMethod, msg, params);
    }

    @SuppressWarnings("unused")
    public static void logp(String className, Level level, String sourceClass, String sourceMethod,
                            String msg, Throwable thrown) {
        getLoggerByClassName(className).getLogger().logp(level, sourceClass, sourceMethod, msg, thrown);
    }

    @SuppressWarnings("unused")
    public static void logp(String className, Level level, String sourceClass, String sourceMethod,
                            Throwable thrown, Supplier<String> msgSupplier) {
        getLoggerByClassName(className).getLogger().logp(level, sourceClass, sourceMethod, thrown, msgSupplier);
    }

    @SuppressWarnings("unused")
    public static void logrb(String className, Level level, String sourceClass, String sourceMethod,
                             ResourceBundle bundle, String msg, Object... params) {
        getLoggerByClassName(className).getLogger().logrb(level, sourceClass, sourceMethod, bundle, msg, params);
    }

    @SuppressWarnings("unused")
    public static void logrb(String className, Level level, String sourceClass, String sourceMethod,
                             ResourceBundle bundle, String msg, Throwable thrown) {
        getLoggerByClassName(className).getLogger().logrb(level, sourceClass, sourceMethod, bundle, msg, thrown);
    }

    @SuppressWarnings("unused")
    public static void entering(String className, String sourceClass, String sourceMethod) {
        getLoggerByClassName(className).getLogger().entering(sourceClass, sourceMethod);
    }

    @SuppressWarnings("unused")
    public static void entering(String className, String sourceClass, String sourceMethod, Object param1) {
        getLoggerByClassName(className).getLogger().entering(sourceClass, sourceMethod, param1);
    }

    @SuppressWarnings("unused")
    public static void entering(String className, String sourceClass, String sourceMethod, Object params[]) {
        getLoggerByClassName(className).getLogger().entering(sourceClass, sourceMethod, params);
    }

    @SuppressWarnings("unused")
    public static void exiting(String className, String sourceClass, String sourceMethod) {
        getLoggerByClassName(className).getLogger().exiting(sourceClass, sourceMethod);
    }

    @SuppressWarnings("unused")
    public static void exiting(String className, String sourceClass, String sourceMethod, Object result) {
        getLoggerByClassName(className).getLogger().exiting(sourceClass, sourceMethod, result);
    }

    @SuppressWarnings("unused")
    public static void throwing(String className, String sourceClass, String sourceMethod, Throwable thrown) {
        getLoggerByClassName(className).getLogger().throwing(sourceClass, sourceMethod, thrown);
    }

    @SuppressWarnings("unused")
    public static void severe(String className, String msg) {
        log(className, Level.SEVERE, msg);
    }

    @SuppressWarnings("unused")
    public static void warning(String className, String msg) {
        log(className, Level.WARNING, msg);
    }

    @SuppressWarnings("unused")
    public static void info(String className, String msg) {
        log(className, Level.INFO, msg);
    }

    @SuppressWarnings("unused")
    public static void config(String className, String msg) {
        log(className, Level.CONFIG, msg);
    }

    @SuppressWarnings("unused")
    public static void fine(String className, String msg) {
        log(className, Level.FINE, msg);
    }

    @SuppressWarnings("unused")
    public static void finer(String className, String msg) {
        log(className, Level.FINER, msg);
    }

    @SuppressWarnings("unused")
    public static void finest(String className, String msg) {
        log(className, Level.FINEST, msg);
    }

    @SuppressWarnings("unused")
    public static void severe(String className, Supplier<String> msgSupplier) {
        log(className, Level.SEVERE, msgSupplier);
    }

    @SuppressWarnings("unused")
    public static void warning(String className, Supplier<String> msgSupplier) {
        log(className, Level.WARNING, msgSupplier);
    }

    @SuppressWarnings("unused")
    public static void info(String className, Supplier<String> msgSupplier) {
        log(className, Level.INFO, msgSupplier);
    }

    @SuppressWarnings("unused")
    public static void config(String className, Supplier<String> msgSupplier) {
        log(className, Level.CONFIG, msgSupplier);
    }

    @SuppressWarnings("unused")
    public static void fine(String className, Supplier<String> msgSupplier) {
        log(className, Level.FINE, msgSupplier);
    }

    @SuppressWarnings("unused")
    public static void finer(String className, Supplier<String> msgSupplier) {
        log(className, Level.FINER, msgSupplier);
    }

    @SuppressWarnings("unused")
    public static void finest(String className, Supplier<String> msgSupplier) {
        log(className, Level.FINEST, msgSupplier);
    }

    /**
     * Once called, all uncaught exceptions will be written to the log too
     */
    public static void enableLoggingOfUncaughtExceptions() {
        Thread.setDefaultUncaughtExceptionHandler(logUncaughtException);
    }

    /**
     * Once called, no uncaught exception will be written to the log anymore
     */
    public static void disableLoggingOfUncaughtExceptions() {
        Thread.setDefaultUncaughtExceptionHandler(null);
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
