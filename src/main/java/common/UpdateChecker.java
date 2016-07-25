package common;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.FileUtils;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 * This class can be used to self-update applications that are deployed using a
 * maven repository.
 * 
 * @author frede
 *
 */
public class UpdateChecker {

	private static String latestSeenVersionPrefKey = "updates.latestVersionOnWebsite";
	private static Prefs updatePrefs = new Prefs(UpdateChecker.class.getName());

	/**
	 * Versions lower or equal to {@code ver} will be ignored when using
	 * {@link #isUpdateAvailable(URL, String, String, String)}
	 * 
	 * @param ver
	 */
	public static void ignoreUpdate(Version ver) {
		updatePrefs.setPreference(latestSeenVersionPrefKey, ver.toString());
	}

	/**
	 * Checks if a new release has been published on the website. This does not
	 * compare the current app version to the release version on the website,
	 * just checks if something happened on the website. This ensures that
	 * updates that were ignored by the user do not show up again.
	 * 
	 * @return {@code true} if a new release is available and the user did not
	 *         ignore it.
	 */
	public static UpdateInfo isUpdateAvailable(URL repoBaseURL, String mavenGroupID, String mavenArtifactID,
			String mavenClassifier) {
		String savedSetting = updatePrefs.getPreference(latestSeenVersionPrefKey, "");
		UpdateInfo res = null;
		try {
			System.out.println("Checking for updates...");
			res = getLatestUpdateInfo(repoBaseURL, mavenGroupID, mavenArtifactID, mavenClassifier);

			if (savedSetting.equals("")) {
				// Never checked for updates before
				res.showAlert = false;
			} else {

				Version savedVersion = new Version(savedSetting);
				Version currentVersion = new Version(Common.getAppVersion());

				if (res.toVersion.compareTo(savedVersion) == 1) {
					// new update found
					System.out.println("Update available!");
					System.out.println("Version after update: " + res.toVersion.toString());
					System.out.println("Filesize:             " + res.fileSizeInMB + "MB");
					res.showAlert = true;
				} else if (res.toVersion.compareTo(currentVersion) == 1) {
					// found update that is ignored
					System.out.println("Update available (Update was ignored by the user)!");
					System.out.println("Version after update: " + res.toVersion.toString());
					System.out.println("Filesize:             " + res.fileSizeInMB + "MB");
				}
			}
		} catch (JDOMException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * Checks if a new release has been published on the website. This compares
	 * the current appVersion to the version available on the website and thus
	 * does not take into account if the user ignored that update.
	 * 
	 * @return {@code true} if a new release is available.
	 */
	public static UpdateInfo isUpdateAvailableCompareAppVersion(URL repoBaseURL, String mavenGroupID,
			String mavenArtifactID, String mavenClassifier) {
		UpdateInfo res = null;
		try {
			System.out.println("Checking for updates...");
			res = getLatestUpdateInfo(repoBaseURL, mavenGroupID, mavenArtifactID, mavenClassifier);

			Version currentVersion = new Version(Common.getAppVersion());

			if (res.toVersion.compareTo(currentVersion) == 1) {
				// new update found
				updatePrefs.setPreference(latestSeenVersionPrefKey, res.toVersion.toString());
				System.out.println("Update available!");
				System.out.println("Version after update: " + res.toVersion.toString());
				System.out.println("Filesize:             " + res.fileSizeInMB + "MB");
				res.showAlert = true;
			}
		} catch (JDOMException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * Retreives the {@link UpdateInfo} for the latest version available on the
	 * specified maven repo for the specified artifact.
	 * 
	 * @param repoBaseURL
	 *            The base url where the repo can be reached. For Maven Central,
	 *            this is {@link http://repo1.maven.org/maven/}
	 * @param mavenGroupID
	 *            The groupID of the artifact to be looked up.
	 * @param mavenArtifactID
	 *            The artifactId of the artifact to be looked up.
	 * @return The {@link UpdateINfo} of the latest version of the artifact
	 *         available at the specified repository.
	 * @throws JDOMException
	 *             If mavens {@code maven-metadata.xml} is not parseable (WHich
	 *             will never be the case unless you don't modify it manually).
	 * @throws IOException
	 *             In case mavens {@code maven-metadata.xml} cannot be retreived
	 *             for any other reason.
	 */
	@SuppressWarnings("unused")
	private static UpdateInfo getLatestUpdateInfo(URL repoBaseURL, String mavenGroupID, String mavenArtifactID)
			throws JDOMException, IOException {
		return getLatestUpdateInfo(repoBaseURL, mavenGroupID, mavenArtifactID, "");
	}

	private static UpdateInfo getLatestUpdateInfo(URL repoBaseURL, String mavenGroupID, String mavenArtifactID,
			String mavenClassifier) throws JDOMException, IOException {
		UpdateInfo res = new UpdateInfo();

		Document mavenMetadata = getMavenMetadata(repoBaseURL, mavenGroupID, mavenArtifactID);

		// get version number
		res.toVersion = new Version(
				mavenMetadata.getRootElement().getChild("versioning").getChild("latest").getValue());

		// get file size
		String url = "";
		if (!mavenClassifier.equals("")) {
			// classifier specified
			url = repoBaseURL.toString() + "/" + mavenGroupID + "/" + mavenArtifactID + res.toVersion + "/"
					+ mavenArtifactID + "-" + res.toVersion + "-" + mavenClassifier + ".jar";
		} else {
			url = repoBaseURL.toString() + "/" + mavenGroupID + "/" + mavenArtifactID + res.toVersion + "/"
					+ mavenArtifactID + "-" + res.toVersion + ".jar";
		}

		URLConnection connection = new URL(url).openConnection();
		res.fileSizeInMB = connection.getContentLength() / 1024.0 / 1024.0;

		res.mavenArtifactID = mavenArtifactID;
		res.mavenGroupID = mavenGroupID;
		res.mavenClassifier = mavenClassifier;
		res.mavenRepoBaseURL = repoBaseURL;

		return res;
	}

	// TODO: Fix javadoc
	/**
	 * Get a DOM of mavens {@code maven-metadata.xml}-file of the specified
	 * artifact.
	 * 
	 * @param repoBaseURL
	 *            The base url where the repo can be reached. For Maven Central,
	 *            this is {@link http://repo1.maven.org/maven/}
	 * @param mavenGroupID
	 *            The groupID of the artifact to be looked up.
	 * @param mavenArtifactID
	 *            The artifactId of the artifact to be looked up.
	 * @return A JDOM {@link Document} representation of mavens
	 *         {@code maven-metadata.xml}
	 * @throws JDOMException
	 *             If mavens {@code maven-metadata.xml} is not parseable (WHich
	 *             will never be the case unless you don't modify it manually).
	 * @throws IOException
	 *             In case mavens {@code maven-metadata.xml} cannot be retreived
	 *             for any other reason.
	 */
	private static Document getMavenMetadata(URL repoBaseURL, String mavenGroupID, String mavenArtifactID)
			throws JDOMException, IOException {
		Document doc = new SAXBuilder().build(
				new URL(repoBaseURL.toString() + "/" + mavenGroupID + "/" + mavenArtifactID + "/maven-metadata.xml"));
		return doc;
	}

	/**
	 * Downloads the specified update as a jar-file and launches it. The jar
	 * file will be saved at the same location as the currently executed file
	 * but will not replace it (unless it has the same filename but this will
	 * never happen)
	 * 
	 * @param updateToInstall
	 *            The {@link UpdateInfo}-object that contains the information
	 *            about the update to download
	 * @throws IllegalStateException
	 *             if maven fails to download or copy the new artifact.
	 * @throws IOException
	 *             If the updated artifact cannot be launched.
	 */
	public static void downloadAndInstallUpdate(UpdateInfo updateToInstall) throws IllegalStateException, IOException {
		downloadAndInstallUpdate(updateToInstall, null);
	}

	/**
	 * Downloads the specified update as a jar-file and launches it. The jar
	 * file will be saved at the same location as the currently executed file
	 * but will not replace it (unless it has the same filename but this will
	 * never happen)
	 * 
	 * @param updateToInstall
	 *            The {@link UpdateInfo}-object that contains the information
	 *            about the update to download
	 * @throws IllegalStateException
	 *             if maven fails to download or copy the new artifact.
	 * @throws IOException
	 *             If the updated artifact cannot be launched.
	 */
	public static void downloadAndInstallUpdate(UpdateInfo updateToInstall, UpdateProgressDialog gui)
			throws IllegalStateException, IOException {
		downloadAndInstallUpdate(updateToInstall, gui, true);
	}

	/**
	 * Downloads the specified update as a jar-file and launches it. The jar
	 * file will be saved at the same location as the currently executed file
	 * but will not replace it (unless it has the same filename but this will
	 * never happen)
	 * 
	 * @param updateToInstall
	 *            The {@link UpdateInfo}-object that contains the information
	 *            about the update to download
	 * @throws IllegalStateException
	 *             if maven fails to download or copy the new artifact.
	 * @throws IOException
	 *             If the updated artifact cannot be launched.
	 */
	public static void downloadAndInstallUpdate(UpdateInfo updateToInstall, UpdateProgressDialog gui,
			boolean launchUpdateAfterInstall) throws IllegalStateException, IOException {
		downloadAndInstallUpdate(updateToInstall, gui, true, true);
	}

	/**
	 * Downloads the specified update as a jar-file and launches it. The jar
	 * file will be saved at the same location as the currently executed file
	 * but will not replace it (unless it has the same filename but this will
	 * never happen)
	 * 
	 * @param updateToInstall
	 *            The {@link UpdateInfo}-object that contains the information
	 *            about the update to download
	 * @param gui
	 *            The reference to an {@link UpdateProgressDialog} that displays
	 *            the current update status.
	 * @throws IllegalStateException
	 *             if maven fails to download or copy the new artifact.
	 * @throws IOException
	 *             If the updated artifact cannot be launched.
	 */
	public static void downloadAndInstallUpdate(UpdateInfo updateToInstall, UpdateProgressDialog gui,
			boolean launchUpdateAfterInstall, boolean deleteOldVersion) throws IllegalStateException, IOException {

		if (gui != null) {
			gui.preparePhaseStarted();
		}

		String destFolder = System.getProperty("user.dir");
		String destFilename;

		// Construct file name of output file
		if (updateToInstall.mavenClassifier.equals("")) {
			// No classifier
			destFilename = updateToInstall.mavenArtifactID + "-" + updateToInstall.toVersion.toString() + ".jar";
		} else {
			destFilename = updateToInstall.mavenArtifactID + "-" + updateToInstall.toVersion.toString() + "-"
					+ updateToInstall.mavenClassifier + ".jar";
		}

		URL artifactURL;

		// Construct the download url
		if (updateToInstall.mavenClassifier.equals("")) {
			artifactURL = new URL(updateToInstall.mavenRepoBaseURL.toString() + "/" + updateToInstall.mavenGroupID + "/"
					+ updateToInstall.mavenArtifactID + "/" + updateToInstall.toVersion.toString() + "/"
					+ updateToInstall.mavenArtifactID + "-" + updateToInstall.toVersion.toString() + ".jar");
		} else {
			artifactURL = new URL(updateToInstall.mavenRepoBaseURL.toString() + "/" + updateToInstall.mavenGroupID + "/"
					+ updateToInstall.mavenArtifactID + "/" + updateToInstall.toVersion.toString() + "/"
					+ updateToInstall.mavenArtifactID + "-" + updateToInstall.toVersion.toString() + "-"
					+ updateToInstall.mavenClassifier + ".jar");
		}

		// Create empty file
		File outputFile = new File(destFolder + File.separator + destFilename);

		// Download
		if (gui != null) {
			gui.downloadStarted();
		}

		System.out.println("Downloading artifact from " + artifactURL.toString() + "...");
		System.out.println("Downloading to: " + outputFile.getAbsolutePath());
		FileUtils.copyURLToFile(artifactURL, outputFile);

		// Perform install steps (none at the moment)
		if (gui != null) {
			gui.installStarted();
		}

		// launch the app
		if (gui != null) {
			gui.launchStarted();
		}

		if (launchUpdateAfterInstall) {
			ProcessBuilder pb = null;
			if (deleteOldVersion) {
				pb = new ProcessBuilder("java", "-jar", destFolder + File.separator + destFilename, "deleteFile=");
			} else {
				pb = new ProcessBuilder("java", "-jar", destFolder + File.separator + destFilename);
			}
			pb.start();
		}
	}

}
