package view.motd;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.rometools.rome.io.FeedException;

import common.Common;

public class MOTDTest {

	private static MOTD motd;
	private static URL testFeedURL;
	private static final String appName = "fokprojectUnitTests";

	@Before
	public void prepare() throws MalformedURLException {
		Common.setAppName(appName);
		testFeedURL = new URL("https://fokprojects.mo-mar.de/message-of-the-day/feed/");
	}

	@Test
	public void getMOTDTest() throws IllegalArgumentException, FeedException, IOException, ClassNotFoundException {
		motd = MOTD.getLatestMOTD(testFeedURL);

		assert motd.isMarkedAsRead() == false;
		assert motd.getEntry() != null;
		assert motd.getFeedTitle() != null;
		assert motd.getImage() != null;

		// mark as read
		motd.markAsRead();
		assert motd.isMarkedAsRead() == true;
	}

	@After
	public void cleanUp() {
		for (File f : MOTD.getSerializedMOTFiles()) {
			f.delete();
		}
	}
}
