package common;

import java.io.File;

public class Common {

	private static String appName;
	public static String UNKNOWN_APP_VERSION = "unknown";
	private static String mockAppVersion = "";

	// General

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
		// here, we assign the name of the OS, according to Java, to a
		// variable...
		String OS = (System.getProperty("os.name")).toUpperCase();
		// to determine what the workingDirectory is.
		// if it is some version of Windows
		if (OS.contains("WIN")) {
			// it is simply the location of the "AppData" folder
			workingDirectory = System.getenv("AppData");
		}
		// Otherwise, we assume Linux or Mac
		else {
			// in either case, we would start in the user's home directory
			workingDirectory = System.getProperty("user.home");
			// if we are on a Mac, we are not done, we look for "Application
			// Support"
			workingDirectory += "/Library/Application Support";
		}

		return workingDirectory + File.separator + appName + File.separator;
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
	 * ENables a mock app version. If a mock app version is set,
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
	 * Clears a mock app version set using {@link #setMockAppVersion(String)} and replaces it with the actual app version.
	 */
	public static void clearMockAppVersion(){
		setMockAppVersion("");
	}

	/**
	 * Returns the current artifact version. If a mock app version is specified, the mock app version is returned and not the actual app version.
	 * 
	 * @return The current artifact version or the String
	 *         {@link #UNKNOWN_APP_VERSION} if the version cannot be determined or the mock app version if one is specified.
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

}
