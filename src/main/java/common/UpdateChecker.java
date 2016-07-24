package common;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/*import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;*/

public class UpdateChecker {

	private static String latestSeenVersionPrefKey = "updates.latestVersionOnWebsite";
	private static Prefs updatePrefs = new Prefs(UpdateChecker.class.getName());

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
			res = getLatestUpdateInfo(repoBaseURL, mavenGroupID, mavenArtifactID, mavenClassifier);

			if (savedSetting.equals("")) {
				// Never checked for updates before
				res.showAlert = false;
			} else {

				Version savedVersion = new Version(savedSetting);

				if (res.toVersion.compareTo(savedVersion) == 1) {
					// new update found
					updatePrefs.setPreference(latestSeenVersionPrefKey, res.toVersion.toString());
					res.showAlert = true;
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
			res = getLatestUpdateInfo(repoBaseURL, mavenGroupID, mavenArtifactID, mavenClassifier);

			Version currentVersion = new Version(Common.getAppVersion());

			if (res.toVersion.compareTo(currentVersion) == 1) {
				// new update found
				updatePrefs.setPreference(latestSeenVersionPrefKey, res.toVersion.toString());
				res.showAlert = true;
			}
		} catch (JDOMException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return res;
	}

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

		return res;
	}

	private static Document getMavenMetadata(URL repoBaseURL, String mavenGroupID, String mavenArtifactID)
			throws JDOMException, IOException {
		Document doc = new SAXBuilder()
				.build(repoBaseURL.toString() + "/" + mavenGroupID + "/" + mavenArtifactID + "/maven-metadata.xml");
		return doc;
	}

}
