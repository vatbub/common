package common;

import java.io.File;

public class Common {
	
	private static String appName;
	public static String  UNKNOWN_APP_VERSION = "unknown";

	//General
	
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
			if (appName==null){
				throw new NullPointerException("Cannot retreive AppDataPath. No appName specified. Use setAppName(String appName) to set one.");
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

		public static String getAppName() {
			return appName;
		}

		public static void setAppName(String appName) {
			Common.appName = appName;
		}
		
		/**
		 * Returns the current artifact version
		 * 
		 * @return The current artifact version or the String unknown if the version
		 *         cannot be determined.
		 */
		public static String getAppVersion() {
			String ver = Common.class.getPackage().getImplementationVersion();
			if (ver == null) {
				return UNKNOWN_APP_VERSION;
			} else {
				return ver;
			}
		}

}
