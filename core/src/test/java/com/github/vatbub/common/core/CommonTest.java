package com.github.vatbub.common.core;

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


import com.amazonaws.auth.BasicAWSCredentials;
import com.google.common.hash.Hashing;
import org.apache.commons.lang.SystemUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.math.BigInteger;
import java.util.Calendar;

public class CommonTest {
    @Before
    public void resetConfig() {
        Common.resetInstance();
    }

    @Test
    public void getAppDataPathFailureTest() {
        // get app name should fail as no app name is set
        try {
            Common.getInstance().setAppName(null);
            Common.getInstance().getAppDataPath();
            assert false;
        } catch (NullPointerException e) {
            assert true;
        }
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void getAppDataPathTest() {
        // set app name
        String appName = "UnitTests";

        Common.getInstance().setAppName(appName);
        String res = Common.getInstance().getAppDataPath();
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
    public void getAppDataPathAsFileTest() {
        // set app name
        String appName = "UnitTests";

        Common.getInstance().setAppName(appName);
        File res = Common.getInstance().getAndCreateAppDataPathAsFile();
        Assert.assertNotEquals(null, res);
    }

    @Test
    public void getCurrentTimeStampTest() {
        String timestamp = Common.getInstance().getCurrentTimeStamp();

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

        Common.getInstance().setAppName(appName);
        Assert.assertEquals(Common.UNKNOWN_APP_VERSION, Common.getInstance().getAppVersion());
        Assert.assertEquals("", Common.getInstance().getMockAppVersion());

        Common.getInstance().setMockAppVersion(mockVersion);
        Assert.assertEquals(mockVersion, Common.getInstance().getMockAppVersion());
        Assert.assertEquals(mockVersion, Common.getInstance().getAppVersion());

        Common.getInstance().clearMockAppVersion();
        Assert.assertEquals(Common.UNKNOWN_APP_VERSION, Common.getInstance().getAppVersion());
        Assert.assertEquals("", Common.getInstance().getMockAppVersion());
    }

    @Test
    public void setMockBuildNumberTest() {
        String mockBuildNumber = "111";
        // set app name
        String appName = "UnitTests";

        Common.getInstance().setAppName(appName);
        Assert.assertEquals(Common.UNKNOWN_BUILD_NUMBER, Common.getInstance().getBuildNumber());
        Assert.assertEquals("", Common.getInstance().getMockBuildNumber());

        Common.getInstance().setMockBuildNumber(mockBuildNumber);
        Assert.assertEquals(mockBuildNumber, Common.getInstance().getBuildNumber());
        Assert.assertEquals(mockBuildNumber, Common.getInstance().getMockBuildNumber());

        Common.getInstance().clearMockBuildVersion();
        Assert.assertEquals(Common.UNKNOWN_BUILD_NUMBER, Common.getInstance().getBuildNumber());
        Assert.assertEquals("", Common.getInstance().getMockBuildNumber());
    }

    @Test
    public void packagingTest() {
        // set app name
        String appName = "UnitTests";
        String packaging = "jar";

        Common.getInstance().setAppName(appName);
        // no mock packaging set
        Assert.assertNull(Common.getInstance().getPackaging());
        Assert.assertEquals("", Common.getInstance().getMockPackaging());

        Common.getInstance().setMockPackaging(packaging);
        Assert.assertEquals(packaging, Common.getInstance().getPackaging());
        Assert.assertEquals(packaging, Common.getInstance().getMockPackaging());

        Common.getInstance().clearMockPackaging();
        Assert.assertNull(Common.getInstance().getPackaging());
        Assert.assertEquals("", Common.getInstance().getMockPackaging());
    }

    @Test
    public void buildNumberManifestEntryTest() {
        // no entry set, check for the default one
        Assert.assertEquals("Custom-Implementation-Build", Common.getInstance().getBuildNumberManifestEntry());

        String newEntry = "testEntryName";
        Common.getInstance().setBuildNumberManifestEntry(newEntry);
        Assert.assertEquals(newEntry, Common.getInstance().getBuildNumberManifestEntry());
    }

    @Test
    public void awsCredentialsTest() {
        String awsKey = "myAwsKey";
        String awsSecret = "myAwsSecret";

        try {
            // should throw a NullPointer
            Common.getInstance().getAWSCredentials();
            Assert.fail("No NullPointerException thrown");
        } catch (NullPointerException e) {
            assert true;
        }

        Common.getInstance().setAwsAccessKey(awsKey);
        Common.getInstance().setAwsSecretAccessKey(awsSecret);

        Assert.assertEquals(awsKey, Common.getInstance().getAwsAccessKey());
        Assert.assertEquals(awsSecret, Common.getInstance().getAwsSecretAccessKey());

        BasicAWSCredentials credentials = Common.getInstance().getAWSCredentials();
        Assert.assertNotNull(credentials);
        Assert.assertEquals(awsKey, credentials.getAWSAccessKeyId());
        Assert.assertEquals(awsSecret, credentials.getAWSSecretKey());
    }

    @Test
    public void getPathAndNameOfCurrentJarTest() {
        String name = Common.getInstance().getPathAndNameOfCurrentJar();
        System.out.println(name);
        Assert.assertNotEquals("", name);
    }

    @Test
    public void deviceIdentifierTest() {
        System.out.println("Calculating 1st md5 hash...");
        String identifier1 = Common.getInstance().getUniqueDeviceIdentifier();
        System.out.println("Calculating 2nd md5 hash...");
        String identifier2 = Common.getInstance().getUniqueDeviceIdentifier();

        System.out.println("1st md5 hash is: " + identifier1);
        System.out.println("2nd md5 hash is: " + identifier2);

        Assert.assertNotEquals("", identifier1);
        Assert.assertNotEquals("", identifier2);
        Assert.assertEquals(identifier1, identifier2);

        System.out.println("Calculating 1st sha256 hash...");
        identifier1 = Common.getInstance().getUniqueDeviceIdentifier(Hashing.sha256().newHasher());
        System.out.println("Calculating 2nd sha256 hash...");
        identifier2 = Common.getInstance().getUniqueDeviceIdentifier(Hashing.sha256().newHasher());

        System.out.println("1st sha256 hash is: " + identifier1);
        System.out.println("2nd sha256 hash is: " + identifier2);

        Assert.assertNotEquals("", identifier1);
        Assert.assertNotEquals("", identifier2);
        Assert.assertEquals(identifier1, identifier2);

        System.out.println("Calculating 1st crc32c hash...");
        int identifier12 = Common.getInstance().getUniqueDeviceIdentifierAsDecInt();
        System.out.println("Calculating 2nd crc32c hash...");
        int identifier22 = Common.getInstance().getUniqueDeviceIdentifierAsDecInt();

        System.out.println("1st crc32c hash is: " + identifier12);
        System.out.println("2nd crc32c hash is: " + identifier22);

        Assert.assertEquals(identifier1, identifier2);
    }

    @Test
    public void decDeviceIdentifierTest() {
        System.out.println("Calculating md5 hash...");
        String identifierHex = Common.getInstance().getUniqueDeviceIdentifier();
        System.out.println("md5 hash is: " + identifierHex);
        BigInteger identifierDec = Common.getInstance().getUniqueDeviceIdentifierAsDec();
        System.out.println("md5 hash converted to dec is: " + identifierDec);

        Assert.assertEquals(identifierHex, identifierDec.toString(16));

        System.out.println("Calculating sha256 hash...");
        identifierHex = Common.getInstance().getUniqueDeviceIdentifier(Hashing.sha256().newHasher());
        identifierDec = Common.getInstance().getUniqueDeviceIdentifierAsDec(Hashing.sha256().newHasher());
        System.out.println("sha256 hash is: " + identifierHex);
        Assert.assertEquals(identifierHex, identifierDec.toString(16));
        System.out.println("sha256 hash converted to dec is: " + identifierDec);

        System.out.println("Calculating crc32c hash...");
        identifierHex = Common.getInstance().getUniqueDeviceIdentifier(Hashing.crc32c().newHasher());
        System.out.println("crc32c hash is: " + identifierHex);
        int identifierDec2 = Common.getInstance().getUniqueDeviceIdentifierAsDecInt();
        System.out.println("crc32c hash converted to dec is: " + identifierDec2);
        Assert.assertEquals(Integer.toString(Integer.parseInt(identifierHex, 16), 16), Integer.toString(identifierDec2, 16));
    }
}
