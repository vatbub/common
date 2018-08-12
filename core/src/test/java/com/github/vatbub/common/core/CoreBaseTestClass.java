package com.github.vatbub.common.core;

import com.github.vatbub.common.core.logging.FOKLogger;
import org.apache.commons.io.FileUtils;
import org.junit.After;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public abstract class CoreBaseTestClass {
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
