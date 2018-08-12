package com.github.vatbub.common.core;

/*-
 * #%L
 * FOKProjects Common Core
 * %%
 * Copyright (C) 2016 - 2018 Frederik Kammel
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
import org.apache.commons.io.FileUtils;
import org.junit.After;

import java.io.File;
import java.io.IOException;

public abstract class CoreBaseTestClass {
    public static final String DEFAULT_APP_NAME = "CommonLibraryUnitTests";
    private String appDataPathToClean;
    private boolean cleanAppDataPathAfterEachTest = true;

    public String getAppDataPathToClean() {
        return appDataPathToClean;
    }

    public void setAppDataPathToClean(String appDataPathToClean) {
        this.appDataPathToClean = appDataPathToClean;
    }

    @After
    public void deleteAppDataPath() throws IOException {
        if (!isCleanAppDataPathAfterEachTest())
            return;

        if (getAppDataPathToClean() == null) {
            if (Common.getInstance().getAppName() == null)
                return;

            setAppDataPathToClean(Common.getInstance().getAppDataPath());
        }

        FOKLogger.resetAllLoggers();
        File appDataPath = new File(getAppDataPathToClean());
        FileUtils.deleteDirectory(appDataPath);
    }

    public boolean isCleanAppDataPathAfterEachTest() {
        return cleanAppDataPathAfterEachTest;
    }

    public void setCleanAppDataPathAfterEachTest(boolean cleanAppDataPathAfterEachTest) {
        this.cleanAppDataPathAfterEachTest = cleanAppDataPathAfterEachTest;
    }
}
