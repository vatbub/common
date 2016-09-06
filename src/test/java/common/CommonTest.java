package common;

import java.io.File;

import org.apache.commons.lang.SystemUtils;
import org.junit.Test;

public class CommonTest {
	@Test
	public void getAppDataPathFailiureTest() {
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
	public void setMockAppVersionTest() {
		String mockVersion = "111";
		// set app name
		String appName = "UnitTests";

		Common.setAppName(appName);
		assert Common.getAppVersion().equals(Common.UNKNOWN_APP_VERSION);

		Common.setMockAppVersion(mockVersion);
		assert Common.getAppVersion().equals(mockVersion);
		
		Common.clearMockAppVersion();
		assert Common.getAppVersion().equals(Common.UNKNOWN_APP_VERSION);
	}
	
	@Test
	public void setMockBuildNumberTest(){
		String mockBuildNumber = "111";
		// set app name
		String appName = "UnitTests";

		Common.setAppName(appName);
		assert Common.getBuildNumber().equals(Common.UNKNOWN_BUILD_NUMBER);
		
		Common.setMockBuildNumber(mockBuildNumber);
		assert Common.getBuildNumber().equals(mockBuildNumber);
		
		Common.clearMockAppVersion();
		assert Common.getBuildNumber().equals(Common.UNKNOWN_BUILD_NUMBER);
	}
}
