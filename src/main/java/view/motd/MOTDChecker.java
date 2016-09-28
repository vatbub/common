package view.motd;

import java.io.IOException;
import java.net.URL;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import common.*;

/**
 * Checks if a given RSS feed contains a new message of the day
 * 
 * @author frede
 *
 */
public class MOTDChecker {

	private Prefs prefs = new Prefs(MOTDChecker.class.getName());
	private static final String latestPostHaschKey = "latestMOTDHash";

	public static void checkForNewMOTDAndMarkItAsRead(URL feedUrl) {

	}

	public static void checkForNewMOTD(URL feedUrl) throws IllegalArgumentException, FeedException, IOException {
		SyndFeed feed = (new SyndFeedInput()).build(new XmlReader(feedUrl));
	}

	public static void markMOTDAsRead() {

	}

}
