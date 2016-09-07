package common;

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
		String olderVersion = "0.0.1-SNAPSHOT";
		String oldBuildNumber = "10";
		String newerVersion = "0.0.1-SNAPSHOT";
		String newBuildNumber = "11";
		Version old = new Version(olderVersion, oldBuildNumber);
		Version n = new Version(newerVersion, newBuildNumber);
		assert old.compareTo(n) == -1;
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
	public void compareToNullTest(){
		String olderVersion = "0.0.1-SNAPSHOT";
		assert new Version(olderVersion).compareTo(null) == 1;
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
			}else {
				// Any other exception was thrown
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void versionNullTest(){
		String olderVersion = null;
		try {
			// this is an illegal version format so an exception should be
			// thrown
			new Version(olderVersion);

			// No exception occurred
			assert false;
		} catch (Exception e) {
			if (e instanceof IllegalArgumentException) {
				assert true;
			}else {
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

}
