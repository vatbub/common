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


import com.jcabi.manifests.Manifests;
import logging.FOKLogger;
import org.apache.commons.lang.SystemUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;

public class Common {

    private static String appName;
    public static final String UNKNOWN_APP_VERSION = "unknown version";
    public static final String UNKNOWN_BUILD_NUMBER = "unknown build number";
    private static String mockAppVersion = "";
    private static String mockBuildNumber = "";
    private static String mockPackaging = "";

    private static String buildNumberManifestEntry = "Custom-Implementation-Build";

    /**
     * Time when the app was launched. A simple call of the {@link Date}
     * -constructor will get the current timestamp. As this variable is
     * initialized when the app is launched, this represents the time when the
     * app was launched.
     */
    private static Date launchDate = new Date();

    // General

    /**
     * Returns the current time as a String.
     *
     * @return The current time in the format "yyyy-MM-dd_HH-mm-ss"
     */
    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");// dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    /**
     * Returns the time when the app was launched as a String.
     *
     * @return The time when the app was launched in the format
     * "yyyy-MM-dd_HH-mm-ss"
     */
    public static String getLaunchTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");// dd/MM/yyyy
        String strDate = sdfDate.format(launchDate);
        return strDate;
    }

    /**
     * Gets the appData directory of the os including a subdirectory for this
     * app.<br>
     * In case the current OS is Windows, it returns
     * {@code C:\Users\(username)\AppData\Roaming\(appName)\}, in case of Mac,
     * it returns
     * {@code (home directory)/Library/Application Support/(appName)/} and in
     * case of Linux, it returns the home directory.<br>
     * <br>
     * The path returned by this method always finishes with a \ (Windows) or a
     * / (Mac, Linux) (See {@code File#seperator})<br>
     * As this method uses the app name, you need to set the app name using
     * {@link Common#setAppName(String)} or you will get a
     * {@code NullPointerException}<br>
     * <br>
     *
     * @return The sub directory of the home directory of the os where the app
     * can save all files that need to persist, e. g. settings
     * @throws NullPointerException if no app name is specified using
     *                              {@link Common#setAppName(String)}
     * @see Common#setAppName(String)
     * @see Common#getAndCreateAppDataPath()
     */
    public static String getAppDataPath() {
        if (appName == null) {
            throw new NullPointerException(
                    "Cannot retreive AppDataPath. No appName specified. Use setAppName(String appName) to set one.");
        }

        String workingDirectory;

        if (SystemUtils.IS_OS_WINDOWS) {
            // it is simply the location of the "AppData" folder
            workingDirectory = System.getenv("AppData"); // $COVERAGE-IGNORE$
        } else if (SystemUtils.IS_OS_MAC) {
            workingDirectory = System.getProperty("user.home"); // $COVERAGE-IGNORE$
            workingDirectory += "/Library/Application Support";
        } else {
            workingDirectory = System.getProperty("user.home"); // $COVERAGE-IGNORE$
            // Support"
            workingDirectory += "/.local/share";
        }

        return workingDirectory + File.separator + appName + File.separator;
    }

    /**
     * Same as {@link #getAppDataPath()} but converts its output into a {@code File} object.
     *
     * @return Same as {@link #getAppDataPath()} but converts its output into a {@code File} object.
     */
    public static File getAppDataPathAsFile() {
        return new File(Common.getAppDataPath());
    }

    /**
     * Returns the same path as {@link Common#getAppDataPath()} but also creates
     * the whole underlying folder structure if not already present.
     *
     * @return The sub directory of the home directory of the os where the app
     * can save all files that need to persist, e. g. settings
     * @throws NullPointerException if no app name is specified using
     *                              {@link Common#setAppName(String)}
     * @see Common#setAppName(String)
     * @see Common#getAppDataPath()
     */
    public static String getAndCreateAppDataPath() {
        String path = getAppDataPath();

        new File(path).mkdirs();

        return path;
    }

    /**
     * Same as {@link #getAndCreateAppDataPath()} but converts its output into a {@code File} object.
     *
     * @return Same as {@link #getAndCreateAppDataPath()} but converts its output into a {@code File} object.
     */
    public static File getAndCreateAppDataPathAsFile() {
        File path = Common.getAppDataPathAsFile();
        path.mkdirs();
        return path;
    }

    /**
     * Get the current name of the app that was set with
     * {@link #setAppName(String)}.
     *
     * @return The current name of the app that was set with
     * {@link #setAppName(String)}.
     */
    public static String getAppName() {
        return appName;
    }

    /**
     * Sets the name of the app. This is necessary to get the AppDataPath using
     * {@link #getAppDataPath()}.
     *
     * @param appName The name of the app.
     */
    public static void setAppName(String appName) {
        Common.appName = appName;
    }

    /**
     * Enables a mock app version. If a mock app version is set,
     * {@link #getAppVersion()} will not return the mock app version instead of
     * the actual version.
     *
     * @param version The version string to be used as the mock app version
     */
    public static void setMockAppVersion(String version) {
        FOKLogger.info(Common.class.getName(), "Now using mock app version " + version);
        mockAppVersion = version;
    }

    /**
     * Returns the current value to be used as a mock app version.
     *
     * @return The current mock app version value or "" if no mock app version
     * is in use.
     */
    public static String getMockAppVersion() {
        return mockAppVersion;
    }

    /**
     * Clears a mock app version set using {@link #setMockAppVersion(String)}
     * and replaces it with the actual app version.
     */
    public static void clearMockAppVersion() {
        setMockAppVersion("");
    }

    /**
     * Enables a mock build number. If a mock build number is set,
     * {@link #getAppVersion()} will not return the mock build number instead of
     * the actual build number.
     *
     * @param buildNumber The build number to be used as the mock build number
     */
    public static void setMockBuildNumber(String buildNumber) {
        FOKLogger.info(Common.class.getName(), "Now using mock build number " + buildNumber);
        mockBuildNumber = buildNumber;
    }

    /**
     * Returns the current value to be used as a mock build number.
     *
     * @return The current mock build number value or "" if no mock build number
     * is in use.
     */
    public static String getMockBuildNumber() {
        return mockBuildNumber;
    }

    /**
     * Clears a mock build number set using {@link #setMockBuildNumber(String)}
     * and replaces it with the actual build number.
     */
    public static void clearMockBuildVersion() {
        setMockBuildNumber("");
    }

    public static void setMockPackaging(String packaging) {
        FOKLogger.info(Common.class.getName(), "Now using mock packaging " + packaging);
        mockPackaging = packaging;
    }

    public static String getMockPackaging() {
        return mockPackaging;
    }

    public static void clearMockPackaging() {
        setMockPackaging("");
    }

    /**
     * Returns the current artifact version. If a mock app version is specified,
     * the mock app version is returned and not the actual app version.
     *
     * @return The current artifact version or the String
     * {@link #UNKNOWN_APP_VERSION} if the version cannot be determined
     * or the mock app version if one is specified.
     */
    public static String getAppVersion() {
        if (!mockAppVersion.equals("")) {
            // A mock version was defined
            return mockAppVersion;
        } else {
            String ver = Common.class.getPackage().getImplementationVersion();
            if (ver == null) {
                return UNKNOWN_APP_VERSION;
            } else {
                return ver; // $COVERAGE-IGNORE$
            }
        }
    }

    /**
     * Returns the current artifact build number. If a mock build number is
     * specified, the mock build number is returned and not the actual build
     * number.<br>
     * <br>
     * For this method to work, the build number must be saved in the
     * MANIFEST.MF file in the attribute "Custom-Implementation-Build" (if you
     * didn't specify anything else in
     * {@link #setBuildNumberManifestEntry(String)}).
     *
     * @return The current artifact build number or the String
     * {@link #UNKNOWN_BUILD_NUMBER} if the build number cannot be
     * determined or the mock build number if one is specified.
     */
    public static String getBuildNumber() {
        if (!mockBuildNumber.equals("")) {
            // A mock build number was defined
            return mockBuildNumber;
        } else if (Manifests.exists(buildNumberManifestEntry)) {
            return Manifests.read(buildNumberManifestEntry); // $COVERAGE-IGNORE$
        } else {
            return UNKNOWN_BUILD_NUMBER;
        }
    }

    /**
     * Returns the current value of the attribute name where the app looks for
     * its build number when using {@link #getBuildNumber()}.
     *
     * @return The current value of the attribute name where the app looks for
     * its build number when using {@link #getBuildNumber()}.
     */
    public static String getBuildNumberManifestEntry() {
        return buildNumberManifestEntry;
    }

    /**
     * Changes the Manifest entry where the app looks for its build number when
     * using {@link #getBuildNumber()}. The default value (if you never call
     * this method) is {@code "Custom-Implementation-Build"}
     *
     * @param buildNumberManifestEntry The new entry to use.
     */
    public static void setBuildNumberManifestEntry(String buildNumberManifestEntry) {
        Common.buildNumberManifestEntry = buildNumberManifestEntry;
    }

    /**
     * Returns the Path and name of the jar file this app was launcehd from.
     *
     * @return The Path and name of the jar file this app was launcehd from.
     */
    public static String getPathAndNameOfCurrentJar() {
        String path = UpdateChecker.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        try {
            return URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            FOKLogger.log(Common.class.getName(), Level.SEVERE, "An error occurred", e); // $COVERAGE-IGNORE$
            return null;
        }
    }

    /**
     * Returns how this java program is packaged. To be more specific, the file
     * extension of this program is returned. If this program is packaged as a
     * *.jar file, this method will return {@code "jar"}. If it is packaged as a
     * Windows Executable, this method will return {@code "exe"} and so on.
     *
     * @return The file extension of this program or {@code null} if the
     * packaging cannot be determined.
     */
    public static String getPackaging() {
        if (!Common.getMockPackaging().equals("")) {
            // return the mock packaging
            return Common.getMockPackaging();
        } else {
            // return the true packaging
            String path = Common.getPathAndNameOfCurrentJar();

            int positionOfLastDot = path.lastIndexOf(".");
            if (positionOfLastDot != -1) {
                return path.substring(positionOfLastDot + 1); // $COVERAGE-IGNORE$
            } else {
                return null;
            }
        }
    }

    /**
     * Returns a list of {@code Locale}s that are supported by the specified
     * {@code ResourceBundle}.<br>
     * <b><i>Important Note:</i></b> This method will not check for the default
     * bundle file {@code <resourceBaseName>.properties}.
     *
     * @param bundle The bundle to be checked
     * @return A set of locales supported by the specified resource bundle
     */
    public static List<Locale> getLanguagesSupportedByResourceBundle(ResourceBundle bundle) {
        return getLanguagesSupportedByResourceBundle(bundle.getBaseBundleName());
    }

    /**
     * Returns a list of {@code Locale}s that are supported by the
     * {@code ResourceBundle} with the given base name.<br>
     * <b><i>Important Note:</i></b> This method will not check for the default
     * bundle file {@code <resourceBaseName>.properties}.
     *
     * @param resourceBaseName The base name of the resource bundle
     * @return A set of locales supported by the specified resource bundle
     */
    public static List<Locale> getLanguagesSupportedByResourceBundle(String resourceBaseName) {
        List<Locale> locales = new ArrayList<Locale>();

        for (Locale locale : Locale.getAvailableLocales()) {
            try {
                // Try to load the resource bundle with the locale
                ResourceBundle bundle = ResourceBundle.getBundle(resourceBaseName, locale);
                // No fail so add the locale to the list
                if (bundle.getLocale().equals(locale)) {
                    locales.add(locale);
                }
            } catch (MissingResourceException ex) {
                // failed, so don't add it to the list
            }
        }

        return Collections.unmodifiableList(locales);
    }

}
