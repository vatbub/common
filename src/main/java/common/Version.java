package common;

// Code taken from http://stackoverflow.com/questions/198431/how-do-you-compare-two-version-strings-in-java
public class Version implements Comparable<Version> {

	private String version;
	private String buildNumber;
	private static String snapshotSuffix = "-SNAPSHOT";

	
	public String getVersion(){
		return this.version;
	}
	
	public String getBuildNumber(){
		return this.buildNumber;
	}
	
	@Override
	public final String toString() {
		String res = getVersion();
		
		if (getBuildNumber()!=null){
			res = res + "-" + getBuildNumber();
		}
		
		return res;
	}
	
	public Version(String version){
		this (version, null);
	}

	public Version(String version, String buildNumber) {
		if (buildNumber!=null){
			this.buildNumber = buildNumber;
		}
		
		if (version == null)
			throw new IllegalArgumentException("Version can not be null");
		if (!version.matches("[0-9]+(\\.[0-9]+)*(" + snapshotSuffix + ")?"))
			throw new IllegalArgumentException("Invalid version format");
		this.version = version;
	}
	
	public boolean isSnapshot(){
		return (this.toString().endsWith(snapshotSuffix));
	}

	@Override
	public int compareTo(Version that) {
		if (that == null)
			return 1;
		String[] thisParts = this.toString().replace(snapshotSuffix, "").split("\\.");
		boolean thisIsSnapshot = this.isSnapshot();
		String[] thatParts = that.toString().replace(snapshotSuffix, "").split("\\.");
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
		} else {
			// both versions are equal
			return 0;
		}
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

}
