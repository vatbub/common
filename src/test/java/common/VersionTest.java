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


import org.junit.Test;

public class VersionTest {

	@Test
	public void compareReleaseVersionTest() {
		String olderVersion = "0.0.1";
		String newerVersion = "0.0.3";
		assert new Version(olderVersion).compareTo(new Version(newerVersion)) == -1;
		assert new Version(newerVersion).compareTo(new Version(olderVersion)) == 1;
	}

	@Test
	public void compareSnapshotVersionTest() {
		String olderVersion = "0.0.1-SNAPSHOT";
		String newerVersion = "0.0.1";
		assert new Version(olderVersion).compareTo(new Version(newerVersion)) == -1;
		assert new Version(newerVersion).compareTo(new Version(olderVersion)) == 1;
	}

	@Test
	public void compareSnapshotVersionWithBuildNumberTest() {
		String olderVersionString = "0.0.1-SNAPSHOT";
		String oldBuildNumber = "10";
		String newerVersionString = "0.0.1-SNAPSHOT";
		String newBuildNumber = "11";
		Version oldVersion = new Version(olderVersionString, oldBuildNumber);
		Version newVersion = new Version(newerVersionString, newBuildNumber);
		assert oldVersion.compareTo(newVersion) == -1;
		assert newVersion.compareTo(oldVersion) == 1;
	}
	
	@Test
	public void compareSnapshotVersionWithTimestampTest(){
		String olderVersionString = "0.0.1-SNAPSHOT";
		String oldTimestamp = "20160401";
		String newerVersionString = "0.0.1-SNAPSHOT";
		String newTimestamp = "20160402";
		Version oldVersion = new Version(olderVersionString);
		oldVersion.setTimestamp(oldTimestamp);
		Version newVersion = new Version(newerVersionString);
		newVersion.setTimestamp(newTimestamp);
		assert oldVersion.compareTo(newVersion) == -1;
		assert newVersion.compareTo(oldVersion) == 1;
	}

	@Test
	public void compareEqualReleaseVersionTest() {
		String olderVersion = "0.0.1";
		String newerVersion = "0.0.1";
		assert new Version(olderVersion).compareTo(new Version(newerVersion)) == 0;
		assert new Version(olderVersion).equals(new Version(newerVersion));
	}

	@Test
	public void compareEqualSnapshotVersionTest() {
		String olderVersion = "0.0.1-SNAPSHOT";
		String newerVersion = "0.0.1-SNAPSHOT";
		assert new Version(olderVersion).compareTo(new Version(newerVersion)) == 0;
		assert new Version(olderVersion).equals(new Version(newerVersion));
	}

	@Test
	public void illegalVersionFormatTest() {
		String olderVersion = "lkjzughvihkgv";
		try {
			// this is an illegal version format so an exception should be
			// thrown
			new Version(olderVersion);

			// No exception occurred
			assert false;
		} catch (Exception e) {
			if (e instanceof IllegalArgumentException) {
				assert true;
			} else {
				// Any other exception was thrown
				e.printStackTrace();
			}
		}
	}

	@Test
	public void versionNullTest() {
		try {
			// this is an illegal version format so an exception should be
			// thrown
			new Version(null);

			// No exception occurred
			assert false;
		} catch (Exception e) {
			if (e instanceof IllegalArgumentException) {
				assert true;
			} else {
				// Any other exception was thrown
				e.printStackTrace();
			}
		}
	}

	@Test
	public void getVersionTest() {
		String olderVersion = "0.0.1";
		assert new Version(olderVersion).getVersion().equals(olderVersion);
	}

	@Test
	public void toStringTest() {
		final String versionString1 = "0.0.1";
		final String versionString2 = versionString1 + "-SNAPSHOT";
		final String timestamp = "11111111";
		final String buildNumber = "162";

		Version v1 = new Version(versionString1);
		Version v2 = new Version(versionString2);
		assert v1.toString().equals(versionString1);

		// Snapshot remark should be removed
		assert v2.toString().equals(versionString1);

		// Explicitly leave the snapshot remark
		assert v2.toString(false).equals(versionString2);

		// Add timestamp
		v1.setTimestamp(timestamp);
		assert v1.getTimestamp().equals(timestamp);
		assert v1.toString().equals(versionString1 + "-" + timestamp);
		v1.setTimestamp("");

		v1.setBuildNumber(buildNumber);
		assert v1.getBuildNumber().equals(buildNumber);
		assert v1.toString().equals(versionString1 + "-" + buildNumber);
	}

	@SuppressWarnings({"EqualsBetweenInconvertibleTypes", "EqualsWithItself", "ObjectEqualsNull"})
	@Test
	public void equalTest() {
		Version v1 = new Version("0.0.1");
		String someString = "123";
		assert v1.equals(v1);
		assert !(v1.equals(null));
		assert !(v1.equals(someString));
	}

	@Test
	public void cloneTest() {
		Version v1 = new Version("0.0.1");
		assert v1.equals(v1.clone());
	}

}
