package common;

import java.net.URL;

/**
 * A class to communicate update information.
 * @see UpdateChecker
 * @author frede
 *
 */
public class UpdateInfo {
	/**
	 * Base URL of the maven repo where the artifact can be downloaded from.
	 */
	public URL mavenRepoBaseURL;
	
	/**
	 * The artifacts group id.
	 */
	public String mavenGroupID;
	
	/**
	 * The artifacts artifact id
	 */
	public String mavenArtifactID;
	
	/**
	 * The artifacts classifier or {@code ""} if the default artifact shall be used.
	 */
	public String mavenClassifier;
	
	/**
	 * {@code true} if a dialog should be presented to the user to download the update
	 */
	public boolean showAlert;
	
	/**
	 * The artifact version after the update
	 */
	public Version toVersion;
	
	/**
	 * The file size of the artifact in Megabytes. If the file size could not be determined, the value of this field is {@code -1}.
	 */
	public double fileSizeInMB;
}
