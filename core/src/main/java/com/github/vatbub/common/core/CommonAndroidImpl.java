package com.github.vatbub.common.core;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import oshi.hardware.HWDiskStore;
import oshi.hardware.UsbDevice;

import java.nio.charset.Charset;

public class CommonAndroidImpl extends CommonPlatformIndependentImplementations{
    private Context context;
    protected CommonAndroidImpl(Context androidContext){
        setContext(androidContext);
    }

    @Override
    public String getAppDataPath() {
        return getContext().getFilesDir().getPath();
    }

    @Override
    public String getPathAndNameOfCurrentJar() {
        throw new UnsupportedOperationException("Operation not supported on Android");
    }

    @Override
    public String getPackaging() {
        return "apk";
    }

    @Override
    public Hasher get32bitHasher() {
        return Hashing.murmur3_32().newHasher();
    }

    @Override
    public String getUniqueDeviceIdentifier(Hasher hasher) {
        hasher.putString(Settings.Secure.ANDROID_ID, Charset.forName("UTF-8"));

        TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
        hasher.putString(telephonyManager.getDeviceId(), Charset.forName("UTF-8"));

        return hasher.hash().toString();
    }

    @Override
    public boolean isRemovableDrive(HWDiskStore store, UsbDevice[] usbDevices, double jaccardSimilarityThreshold) {
        throw new UnsupportedOperationException("Operation not supported on Android");
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
