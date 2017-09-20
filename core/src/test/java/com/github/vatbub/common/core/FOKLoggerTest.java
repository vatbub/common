package com.github.vatbub.common.core;

import com.github.vatbub.common.core.logging.FOKLogger;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

public class FOKLoggerTest {
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Test
    public void noAppNameSetTest() {
        Common.setAppName(null);
        FOKLogger.info(FOKLoggerTest.class.getName(), "Test without app name");
        Assert.assertEquals(2, StringCommon.countOccurrencesInString(systemOutRule.getLogWithNormalizedLineSeparator(), "\n"));

        Common.setAppName("fokprojectUnitTests");
        FOKLogger.info(FOKLoggerTest.class.getName(), "Test with app name");
        Assert.assertEquals(5, StringCommon.countOccurrencesInString(systemOutRule.getLogWithNormalizedLineSeparator(), "\n"));
    }
}
