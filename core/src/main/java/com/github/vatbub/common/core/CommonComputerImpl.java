package com.github.vatbub.common.core;

import com.github.vatbub.common.core.logging.FOKLogger;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.text.similarity.JaccardSimilarity;
import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.software.os.OperatingSystem;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.logging.Level;

/**
 * Implements all platform dependent methods of{@link Common} for computer-like machines that run linux, mac or windows.
 */
public class CommonComputerImpl extends CommonPlatformIndependentImplementations{
    @Override
    public String getAppDataPath() {
        if (getAppName() == null) {
            throw new NullPointerException(
                    "Cannot retrieve AppDataPath. No appName specified. Use setAppName(String appName) to set one.");
        }

        String workingDirectory;

        if (org.apache.commons.lang.SystemUtils.IS_OS_WINDOWS) {
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

        return workingDirectory + File.separator + getAppName() + File.separator;
    }

    @Override
    public String getPathAndNameOfCurrentJar() {
        String path = Common.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        try {
            return new File(URLDecoder.decode(path, "UTF-8")).getAbsolutePath();
        } catch (UnsupportedEncodingException e) {
            FOKLogger.log(Common.class.getName(), Level.SEVERE, FOKLogger.DEFAULT_ERROR_TEXT, e);
            return null;
        }
    }

    @Override
    public String getPackaging() {
        if (isMockPackagingInUse()) {
            // return the mock packaging
            return getMockPackaging();
        } else {
            // return the true packaging
            String path = getPathAndNameOfCurrentJar();

            int positionOfLastDot = path.lastIndexOf('.');
            if (positionOfLastDot != -1) {
                return path.substring(positionOfLastDot + 1); // $COVERAGE-IGNORE$
            } else {
                return null;
            }
        }
    }

    @Override
    public Hasher get32bitHasher() {
        return Hashing.murmur3_32().newHasher();
    }

    @Override
    public String getUniqueDeviceIdentifier(Hasher hasher) {
        SystemInfo systemInfo = new SystemInfo();
        OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
        HardwareAbstractionLayer hardwareAbstractionLayer = systemInfo.getHardware();
        CentralProcessor centralProcessor = hardwareAbstractionLayer.getProcessor();
        ComputerSystem computerSystem = hardwareAbstractionLayer.getComputerSystem();

        FOKLogger.info(getClass().getName(), "Calculating the device identifier based on the following info:");
        FOKLogger.info(getClass().getName(), "OS Family: " + operatingSystem.getFamily());
        FOKLogger.info(getClass().getName(), "OS Version: " + operatingSystem.getVersion().getVersion());

        hasher.putString(operatingSystem.getFamily(), Charset.forName("UTF-8"));
        hasher.putString(operatingSystem.getVersion().getVersion(), Charset.forName("UTF-8"));

        FOKLogger.info(getClass().getName(), "Drive info:");
        int hddCounter = 0;
        UsbDevice[] usbDevices = hardwareAbstractionLayer.getUsbDevices(false);
        for (HWDiskStore store : hardwareAbstractionLayer.getDiskStores()) {
            if (store.getSerial() != null && !store.getSerial().equals("unknown") && !isRemovableDrive(store, usbDevices)) {
                FOKLogger.info(getClass().getName(), "Drive index: " + hddCounter);
                FOKLogger.info(getClass().getName(), "Drive model: " + store.getModel());
                FOKLogger.info(getClass().getName(), "Drive serial: " + store.getSerial());
                hasher.putString(store.getSerial(), Charset.forName("UTF-8"));
            }
            hddCounter++;
        }

        FOKLogger.info(getClass().getName(), "CPU info: ");
        FOKLogger.info(getClass().getName(), "CPU core count: " + centralProcessor.getLogicalProcessorCount());

        hasher.putInt(centralProcessor.getLogicalProcessorCount());

        FOKLogger.info(getClass().getName(), "computer system info: ");
        FOKLogger.info(getClass().getName(), "CS serial number: " + computerSystem.getSerialNumber());

        if (computerSystem.getSerialNumber() != null && !computerSystem.getSerialNumber().equals("") && !computerSystem.getSerialNumber().equalsIgnoreCase("unknown")) {
            hasher.putString(computerSystem.getSerialNumber(), Charset.forName("UTF-8"));
        }

        return hasher.hash().toString();
    }

    @Override
    public boolean isRemovableDrive(HWDiskStore store, UsbDevice[] usbDevices, double jaccardSimilarityThreshold) {
        try {
            for (UsbDevice device : usbDevices) {
                // check if one contains the other
                if (store.getModel().equalsIgnoreCase(device.getName()) || store.getModel().contains(device.getName()) || device.getName().contains(store.getModel())) {
                    return true;
                }

                if (SingletonMap.getInstance(JaccardSimilarity.class).apply(store.getModel(), device.getName()) > jaccardSimilarityThreshold) {
                    return true;
                }
            }
        } catch (InstantiationException | IllegalAccessException e) {
            FOKLogger.log(getClass().getName(), Level.SEVERE, "Unable to create the JaccardSimilarity instance", e);
        }

        return false;
    }
}
