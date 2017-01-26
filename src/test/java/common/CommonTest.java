package common;

/*-
 * #%L
 * FOKProjects Common
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


import org.apache.commons.lang.SystemUtils;
import org.junit.Test;

import java.io.File;
import java.util.Calendar;

@SuppressWarnings("ConstantConditions")
public class CommonTest {
    @Test
    public void getAppDataPathFailureTest() {
        // get app name should fail as no app name is set
        try {
            Common.setAppName(null);
            Common.getAppDataPath();
            assert false;
        } catch (NullPointerException e) {
            assert true;
        }
    }

    @Test
    public void getAppDataPathTest() {
        // set app name
        String appName = "UnitTests";

        Common.setAppName(appName);
        String res = Common.getAppDataPath();
        String workingDirectory;

        if (SystemUtils.IS_OS_WINDOWS) {
            // it is simply the location of the "AppData" folder
            workingDirectory = System.getenv("AppData");
        } else if (SystemUtils.IS_OS_MAC) {
            workingDirectory = System.getProperty("user.home");
            workingDirectory += "/Library/Application Support";
        } else {
            workingDirectory = System.getProperty("user.home");
            // Support"
            workingDirectory += "/.local/share";
        }

        assert res.equals(workingDirectory + File.separator + appName + File.separator);
    }

    @Test
    public void getCurrentTimeStampTest() {
        String timestamp = Common.getCurrentTimeStamp();

        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;

        String monthString;
        if (month < 10) {
            monthString = "0" + month;
        } else {
            monthString = Integer.toString(month);
        }

        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        String dayString;
        if (day < 10) {
            dayString = "0" + day;
        } else {
            dayString = Integer.toString(day);
        }

        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        String hourString;
        if (hour < 10) {
            hourString = "0" + hour;
        } else {
            hourString = Integer.toString(hour);
        }

        int minute = Calendar.getInstance().get(Calendar.MINUTE);

        String minuteString;
        if (minute < 10) {
            minuteString = "0" + minute;
        } else {
            minuteString = Integer.toString(minute);
        }

        int second = Calendar.getInstance().get(Calendar.SECOND);

        String secondString;
        if (second < 10) {
            secondString = "0" + second;
        } else {
            secondString = Integer.toString(second);
        }

        assert timestamp.equals(year + "-" + monthString + "-" + dayString + "_" + hourString + "-" + minuteString + "-"
                + secondString);
    }

    @Test
    public void setMockAppVersionTest() {
        String mockVersion = "111";
        // set app name
        String appName = "UnitTests";

        Common.setAppName(appName);
        assert Common.getAppVersion().equals(Common.UNKNOWN_APP_VERSION);
        assert Common.getMockAppVersion().equals("");

        Common.setMockAppVersion(mockVersion);
        assert Common.getMockAppVersion().equals(mockVersion);
        assert Common.getAppVersion().equals(mockVersion);

        Common.clearMockAppVersion();
        assert Common.getAppVersion().equals(Common.UNKNOWN_APP_VERSION);
        assert Common.getMockAppVersion().equals("");
    }

    @Test
    public void setMockBuildNumberTest() {
        String mockBuildNumber = "111";
        // set app name
        String appName = "UnitTests";

        Common.setAppName(appName);
        assert Common.getBuildNumber().equals(Common.UNKNOWN_BUILD_NUMBER);
        assert Common.getMockBuildNumber().equals("");

        Common.setMockBuildNumber(mockBuildNumber);
        assert Common.getBuildNumber().equals(mockBuildNumber);
        assert Common.getMockBuildNumber().equals(mockBuildNumber);

        Common.clearMockBuildVersion();
        assert Common.getBuildNumber().equals(Common.UNKNOWN_BUILD_NUMBER);
        assert Common.getMockBuildNumber().equals("");
    }

    @Test
    public void packagingTest() {
        // set app name
        String appName = "UnitTests";
        String packaging = "jar";

        Common.setAppName(appName);
        // no mock packaging set
        assert Common.getPackaging() == null;
        assert Common.getMockPackaging().equals("");

        Common.setMockPackaging(packaging);
        assert Common.getPackaging().equals(packaging);
        assert Common.getMockPackaging().equals(packaging);

        Common.clearMockPackaging();
        assert Common.getPackaging() == null;
        assert Common.getMockPackaging().equals("");
    }

    @Test
    public void buildNumberManifestEntryTest(){
        // no entry set, check for the default one
        assert Common.getBuildNumberManifestEntry().equals("Custom-Implementation-Build");

        String newEntry = "testEntryName";
        Common.setBuildNumberManifestEntry(newEntry);
        assert Common.getBuildNumberManifestEntry().equals(newEntry);
    }
}
