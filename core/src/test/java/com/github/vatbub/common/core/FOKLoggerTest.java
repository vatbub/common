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
import org.junit.contrib.java.lang.system.SystemOutRule;

public class FOKLoggerTest {
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Test
    public void noAppNameSetTest() {
        FOKLogger.resetAllLoggers();
        Common.getInstance().setAppName(null);
        FOKLogger.info(FOKLoggerTest.class.getName(), "Test without app name");
        Assert.assertEquals(2, StringCommon.countOccurrencesInString(systemOutRule.getLogWithNormalizedLineSeparator(), "\n"));

        Common.getInstance().setAppName("fokprojectUnitTests");
        FOKLogger.info(FOKLoggerTest.class.getName(), "Test with app name");
        Assert.assertEquals(5, StringCommon.countOccurrencesInString(systemOutRule.getLogWithNormalizedLineSeparator(), "\n"));
    }
}
