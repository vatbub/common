package com.github.vatbub.common.core;

import android.content.Context;
import android.telephony.TelephonyManager;
import org.junit.Assert;
import org.junit.Before;

import java.io.File;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommonAndroidTest extends CommonTest {
    private Context context;

    @Before
    public void switchToAndroidImplementation() {
        Common.getInstance().setAppName(DEFAULT_APP_NAME);
        String appDataPath = Common.getInstance().getAppDataPath();
        setAppDataPathToClean(appDataPath);

        context = mock(Context.class);
        when(context.getFilesDir()).thenReturn(new File(appDataPath));
        Common.useAndroidImplementation(context);
    }

    private void mockTelephonyManager() {
        TelephonyManager telephonyManager = mock(TelephonyManager.class);
        when(context.getSystemService(Context.TELEPHONY_SERVICE)).thenReturn(telephonyManager);
        when(telephonyManager.getDeviceId()).thenReturn("990000862471854");
    }

    @Override
    public void md5DeviceIdentifierTest() {
        mockTelephonyManager();
        super.md5DeviceIdentifierTest();
    }

    @Override
    public void sha256DeviceIdentifierTest() {
        mockTelephonyManager();
        super.sha256DeviceIdentifierTest();
    }

    @Override
    public void murmur3_32DeviceIdentifierTest() {
        mockTelephonyManager();
        super.murmur3_32DeviceIdentifierTest();
    }

    @Override
    public void decMd5DeviceIdentifierTest() {
        mockTelephonyManager();
        super.decMd5DeviceIdentifierTest();
    }

    @Override
    public void decSha256DeviceIdentifierTest() {
        mockTelephonyManager();
        super.decSha256DeviceIdentifierTest();
    }

    @Override
    public void decMurmur3_32DeviceIdentifierTest() {
        mockTelephonyManager();
        super.decMurmur3_32DeviceIdentifierTest();
    }

    @Override
    public void getPathAndNameOfCurrentJarTest() {
        try {
            Common.getInstance().getPathAndNameOfCurrentJar();
            Assert.fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            System.out.println("Expected UnsupportedOperationException was thrown");
        }
    }

    @Override
    public void packagingTest() {
        Assert.assertEquals("apk", Common.getInstance().getPackaging());
    }
}
