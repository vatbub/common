package com.github.vatbub.common.core;

/*-
 * #%L
 * FOKProjects Common Core
 * %%
 * Copyright (C) 2016 - 2017 Frederik Kammel
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


import com.github.vatbub.common.core.logging.FOKLogger;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemErrRule;
import org.junit.contrib.java.lang.system.SystemOutRule;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class FOKLoggerTest  extends CoreBaseTestClass{
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();
    public final SystemErrRule systemErrRule = new SystemErrRule().enableLog();

    @Test
    public void noAppNameSetTest() {
        FOKLogger.resetAllLoggers();
        Common.getInstance().setAppName(null);
        FOKLogger.info(FOKLoggerTest.class.getName(), "Test without app name");
        Assert.assertEquals(2, StringCommon.countOccurrencesInString(systemOutRule.getLogWithNormalizedLineSeparator(), "\n"));

        Common.getInstance().setAppName(DEFAULT_APP_NAME);
        FOKLogger.info(FOKLoggerTest.class.getName(), "Test with app name");
        Assert.assertEquals(5, StringCommon.countOccurrencesInString(systemOutRule.getLogWithNormalizedLineSeparator(), "\n"));
    }

    @Test
    public void fileLogLevelTest() throws IOException {
        FOKLogger.resetAllLoggers();
        Common.getInstance().setAppName(DEFAULT_APP_NAME);
        Level initialLevel = FOKLogger.getFileLogLevel();
        Assert.assertEquals(Level.ALL, initialLevel);

        Level levelToTest = Level.SEVERE;
        FOKLogger.setFileLogLevel(levelToTest);
        Assert.assertEquals(levelToTest, FOKLogger.getFileLogLevel());

        String logMessageThatShouldAppear = "shouldAppear";
        String logMessageThatShouldNotAppear = "does not exist";
        FOKLogger.info(FOKLoggerTest.class.getName(), logMessageThatShouldNotAppear);
        FOKLogger.log(FOKLoggerTest.class.getName(), levelToTest, logMessageThatShouldAppear);

        File logFile = new File(FOKLogger.getLogFilePathAndName());
        String logFileContents = StringCommon.fromFile(logFile);
        Assert.assertTrue(logFileContents.contains(logMessageThatShouldAppear));
        Assert.assertFalse(logFileContents.contains(logMessageThatShouldNotAppear));

        FOKLogger.setFileLogLevel(initialLevel);
        Assert.assertEquals(initialLevel, FOKLogger.getFileLogLevel());
    }

    /* @Test
    public void consoleLogLevelTest() {
        FOKLogger.resetAllLoggers();
        Common.getInstance().setAppName("FOKLoggerUnitTest" + Math.round(Math.random() * 100000));
        Level initialLevel = FOKLogger.getConsoleLogLevel();
        Assert.assertEquals(Level.INFO, initialLevel);

        Level levelToTest = Level.SEVERE;
        FOKLogger.setConsoleLogLevel(levelToTest);
        Assert.assertEquals(levelToTest, FOKLogger.getConsoleLogLevel());

        String logMessageThatShouldAppear = "shouldAppear";
        String logMessageThatShouldNotAppear = "does not exist";
        FOKLogger.info(FOKLoggerTest.class.getName(), logMessageThatShouldNotAppear);
        FOKLogger.log(FOKLoggerTest.class.getName(), levelToTest, logMessageThatShouldAppear);

        String ttt = systemErrRule.getLog();
        String ddd = systemOutRule.getLog();
        Assert.assertTrue(systemErrRule.getLog().contains(logMessageThatShouldAppear));
        Assert.assertFalse(systemOutRule.getLog().contains(logMessageThatShouldAppear));
        Assert.assertFalse(systemOutRule.getLog().contains(logMessageThatShouldNotAppear));
        Assert.assertFalse(systemErrRule.getLog().contains(logMessageThatShouldNotAppear));

        FOKLogger.setConsoleLogLevel(initialLevel);
        Assert.assertEquals(initialLevel, FOKLogger.getConsoleLogLevel());

        File logFile = new File(FOKLogger.getLogFilePathAndName());
        logFile.deleteOnExit();
    }*/
}
