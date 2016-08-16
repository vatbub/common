package common;

// Code taken from http://stackoverflow.com/questions/198431/how-do-you-compare-two-version-strings-in-java
public class Version implements Comparable<Version> {

	private String version;
	private String buildNumber;
	private String timestamp;
	private static String snapshotSuffix = "-SNAPSHOT";

	public void setVersion(String version) {
		this.version = version;
	}

	public void setBuildNumber(String buildNumber) {
		this.buildNumber = buildNumber;
	}

	public String getVersion() {
		return this.version;
	}

	public String getBuildNumber() {
		return this.buildNumber;
	}

	/**
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp
	 *            the timestamp to set
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Converts this Version to a {@link String} representation. Removes the
	 * -SNAPSHOT-Annotation if this version is a snapshot.
	 * 
	 * @return A {@link String} representation of this version.
	 */
	@Override
	public final String toString() {
		return toString(this.isSnapshot());
	}

	/**
	 * Converts this Version to a {@link String} representation.
	 * 
	 * @param removeSnapshotAnnotaion
	 *            If {@code true}, the -SNAPSHOT-Annotation will be removed for
	 *            snapshot versions.
	 * @return A {@link String} representation of this version.
	 */
	public final String toString(boolean removeSnapshotAnnotaion) {
		String res = getVersion();

		if (removeSnapshotAnnotaion) {
			res = res.replace(snapshotSuffix, "");
		}

		if (getTimestamp() != null && !getTimestamp().equals("")) {
			res = res + "-" + getTimestamp();
		}

		if (getBuildNumber() != null && !getBuildNumber().equals("")) {
			res = res + "-" + getBuildNumber();
		}

		return res;
	}

	public Version(String version) {
		this(version, null);
	}

	public Version(String version, String buildNumber) {
		this(version, buildNumber, null);
	}

	public Version(String version, String buildNumber, String timestamp) {
		setBuildNumber(buildNumber);
		setTimestamp(timestamp);

		if (version == null)
			throw new IllegalArgumentException("Version can not be null");
		if (!version.matches("[0-9]+(\\.[0-9]+)*(" + snapshotSuffix + ")?"))
			throw new IllegalArgumentException("Invalid version format");
		setVersion(version);
	}

	public boolean isSnapshot() {
		return (this.getVersion().endsWith(snapshotSuffix));
	}

	@Override
	public int compareTo(Version that) {
		if (that == null)
			return 1;
		String[] thisParts = this.getVersion().replace(snapshotSuffix, "").split("\\.");
		boolean thisIsSnapshot = this.isSnapshot();
		String[] thatParts = that.getVersion().replace(snapshotSuffix, "").split("\\.");
		boolean thatIsSnapshot = that.isSnapshot();
		int length = Math.max(thisParts.length, thatParts.length);

		for (int i = 0; i < length; i++) {
			int thisPart = i < thisParts.length ? Integer.parseInt(thisParts[i]) : 0;
			int thatPart = i < thatParts.length ? Integer.parseInt(thatParts[i]) : 0;
			if (thisPart < thatPart)
				return -1;
			if (thisPart > thatPart)
				return 1;
		}

		// Version numbers are equal, check if there is a snapshot involved
		if (thisIsSnapshot && !thatIsSnapshot) {
			// that is a release and thus a newer version
			return -1;
		} else if (!thisIsSnapshot && thatIsSnapshot) {
			// this is a release version and thus newer
			return 1;

			// both versions are snapshots, check build number
		} else if (getBuildNumber() != null && !getBuildNumber().equals("")) {
			if (Double.parseDouble(this.getBuildNumber()) > Double.parseDouble(that.getBuildNumber())) {
				// this build number is greater and thus this version is newer
				return 1;
			} else if (Double.parseDouble(this.getBuildNumber()) < Double.parseDouble(that.getBuildNumber())) {
				// that build number is greater and thus that version is newer
				return -1;
			}

			// build numbers are equal, compare timestamps
		} else if (getTimestamp() != null && !getTimestamp().equals("")) {
			if (Double.parseDouble(this.getTimestamp()) > Double.parseDouble(that.getTimestamp())) {
				// this timestamp is greater and thus version is newer
				return 1;
			} else if (Double.parseDouble(this.getTimestamp()) < Double.parseDouble(that.getTimestamp())) {
				// that timestamp is greater and thus version is newer
				return -1;
			}
		}
		
		// We only arrive here if everything is equal so versions are equal too
		return 0;
	}

	@Override
	public boolean equals(Object that) {
		if (this == that)
			return true;
		if (that == null)
			return false;
		if (this.getClass() != that.getClass())
			return false;
		return this.compareTo((Version) that) == 0;
	}
	
	@Override
	public Version clone(){
		return new Version(this.getVersion(), this.getBuildNumber(), this.getTimestamp());
	}

}
