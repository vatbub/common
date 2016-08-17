package common;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.jcabi.manifests.Manifests;
import com.sun.javafx.tk.TKStage;

import javafx.stage.Stage;

import org.apache.commons.lang.SystemUtils;

public class Common {

	private static String appName;
	public static String UNKNOWN_APP_VERSION = "unknown version";
	public static String UNKNOWN_BUILD_NUMBER = "unknown build number";
	private static String mockAppVersion = "";
	private static String mockBuildNumber = "";

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
	 *         "yyyy-MM-dd_HH-mm-ss"
	 */
	public static String getLaunchTimeStamp() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");// dd/MM/yyyy
		String strDate = sdfDate.format(launchDate);
		return strDate;
	}

	/**
	 * Gets the appData directory of the os. In case the current OS is Windows,
	 * it returns C:\Users\(username)\AppData\Roaming, in case of Mac, it
	 * returns (home directory)/Library/Application Support and in case of
	 * Linux, it returns the home directory.<br>
	 * <br>
	 * 
	 * @return The sub directory of the home directory of the os where the app
	 *         can save all files that need to persist, e. g. settings
	 */
	public static String getAppDataPath() {
		if (appName == null) {
			throw new NullPointerException(
					"Cannot retreive AppDataPath. No appName specified. Use setAppName(String appName) to set one.");
		}

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

		return workingDirectory + File.separator + appName + File.separator;
	}
	
	public static String getAndCreateAppDataPath(){
		String path = getAppDataPath();
		
		new File(path).mkdirs();
		
		return path;
	}

	/**
	 * Get the current name of the app that was set with
	 * {@link #setAppName(String)}.
	 * 
	 * @return
	 */
	public static String getAppName() {
		return appName;
	}

	/**
	 * Sets the name of the app. This is necessary to get the AppDataPath using
	 * {@link #getAppDataPath()}.
	 * 
	 * @param appName
	 */
	public static void setAppName(String appName) {
		Common.appName = appName;
	}

	/**
	 * Enables a mock app version. If a mock app version is set,
	 * {@link #getAppVersion()} will not return the mock app version instead of
	 * the actual version.
	 * 
	 * @param version
	 *            The version string to be used as the mock app version
	 */
	public static void setMockAppVersion(String version) {
		System.out.println("Now using mock app version " + version);
		mockAppVersion = version;
	}

	/**
	 * Returns the current value to be used as a mock app version.
	 * 
	 * @return The current mock app version value or "" if no mock app version
	 *         is in use.
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
	 * {@link #getAppVersion(boolean)} will not return the mock build number
	 * instead of the actual build number.
	 * 
	 * @param buildNumber
	 *            The build number to be used as the mock build number
	 */
	public static void setMockBuildNumber(String buildNumber) {
		System.out.println("Now using mock build number " + buildNumber);
		mockBuildNumber = buildNumber;
	}

	/**
	 * Returns the current value to be used as a mock build number.
	 * 
	 * @return The current mock build number value or "" if no mock build number
	 *         is in use.
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

	/**
	 * Returns the current artifact version. If a mock app version is specified,
	 * the mock app version is returned and not the actual app version.
	 * 
	 * @return The current artifact version or the String
	 *         {@link #UNKNOWN_APP_VERSION} if the version cannot be determined
	 *         or the mock app version if one is specified.
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
				return ver;
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
	 *         {@link #UNKNOWN_BUILD_NUMBER} if the build number cannot be
	 *         determined or the mock build number if one is specified.
	 */
	public static String getBuildNumber() {
		if (!mockBuildNumber.equals("")) {
			// A mock build number was defined
			return mockBuildNumber;
		} else if (Manifests.exists(buildNumberManifestEntry)) {
			return Manifests.read(buildNumberManifestEntry);
		} else {
			return UNKNOWN_BUILD_NUMBER;
		}
	}

	/**
	 * Returns the current value of the attribute name where the app looks for
	 * its build number when using {@link #getBuildNumber()}.
	 * 
	 * @return The current value of the attribute name where the app looks for
	 *         its build number when using {@link #getBuildNumber()}.
	 */
	public static String getBuildNumberManifestEntry() {
		return buildNumberManifestEntry;
	}

	/**
	 * Changes the Manifest entry where the app looks for its build number when
	 * using {@link #getBuildNumber()}. The default value (if you never call
	 * this method) is {@code "Custom-Implementation-Build"}
	 * 
	 * @param buildNumberManifestEntry
	 *            The new entry to use.
	 */
	public static void setBuildNumberManifestEntry(String buildNumberManifestEntry) {
		Common.buildNumberManifestEntry = buildNumberManifestEntry;
	}
	
	/**
	 * Returns the native window pointer of the specified {@link Stage} (Windows only).
	 * @param stage The stage to which the pointer shall be returned.
	 * @return
	 */
	public static long getWindowPointer(Stage stage) {
	    try {
	        TKStage tkStage = stage.impl_getPeer();
	        Method getPlatformWindow = tkStage.getClass().getDeclaredMethod("getPlatformWindow" );
	        getPlatformWindow.setAccessible(true);
	        Object platformWindow = getPlatformWindow.invoke(tkStage);
	        Method getNativeHandle = platformWindow.getClass().getMethod( "getNativeHandle" );
	        getNativeHandle.setAccessible(true);
	        Object nativeHandle = getNativeHandle.invoke(platformWindow);
	        return (Long) nativeHandle;
	    } catch (Throwable e) {
	        System.err.println("Error getting Window Pointer");
	        return 0;
	    }
	}

}
