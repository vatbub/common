package view.motd;

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


import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndImage;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import common.Common;
import logging.FOKLogger;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class that represents a message of the day
 * 
 * @author frede
 * @since 0.0.15
 *
 */
@SuppressWarnings("ConstantConditions")
public class MOTD {

	/**
	 * The name of the folder which is used to save serialized messages of the
	 * day.
	 */
	protected static final String latestMOTDSerializedFilePath = "motd";

	/**
	 * The file name which is used to save serialized messages of the day. Use
	 * {index} to separate different messages. Indexes start at 0 and will
	 * continue to grow with each read message.
	 */
	protected static final String latestMOTDSerializedFileName = "messageOfTheDay{index}.serializedObject";

	/**
	 * The message icon
	 */
	private SyndImage image;

	/**
	 * The title of the underlying rss feed
	 */
	private String feedTitle;

	/**
	 * The rss entry that corresponds to this message of the day
	 */
	private SyndEntry entry;

	/**
	 * Creates a new messaeg of the day instance
	 * 
	 * @param image
	 *            The feed icon
	 * @param entry
	 *            The rss entry that corresponds to this message of the day
	 */
	public MOTD(SyndImage image, String feedTitle, SyndEntry entry) {
		this.image = image;
		this.entry = entry;
		this.feedTitle = feedTitle;
	}

	/**
	 * @return the message icon
	 */
	public SyndImage getImage() {
		return image;
	}

	/**
	 * @param image
	 *            the message icon to set
	 */
    @SuppressWarnings({"unused"})
	public void setImage(SyndImage image) {
		this.image = image;
	}

	/**
	 * @return the rss entry that corresponds to this message of the day
	 */
	public SyndEntry getEntry() {
		return entry;
	}

	/**
	 * @param entry
	 *            the rss entry that corresponds to this message of the day
	 */
    @SuppressWarnings({"unused"})
	public void setEntry(SyndEntry entry) {
		this.entry = entry;
	}

	/**
	 * @return the title of the underlying rss feed
	 */
	public String getFeedTitle() {
		return feedTitle;
	}

	/**
	 * @param feedTitle
	 *            the feedTitle to set
	 */
	@SuppressWarnings({"unused"})
	public void setFeedTitle(String feedTitle) {
		this.feedTitle = feedTitle;
	}

	/**
	 * Marks this message of the day as read.
	 * 
	 * @throws IOException
	 *             If the info if this message of the day is marked as read
	 *             cannot be read from the computers hard disk
	 * @throws ClassNotFoundException
	 *             This exception occurs if you did not add the
	 *             <a href="https://rometools.github.io/rome/index.html">ROME
	 *             library</a> to your classpath. If you use maven don't worry
	 *             about this exception as maven already added the library for
	 *             you.
	 */
	public void markAsRead() throws IOException, ClassNotFoundException {
		if (!this.isMarkedAsRead()) {
			FileOutputStream fileOut = new FileOutputStream(Common.getAndCreateAppDataPath()
					+ latestMOTDSerializedFilePath + File.separator + getNextSerializationFileName());
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(entry);
			out.close();
			fileOut.close();
		}
	}

	/**
	 * Checks if this message of the day is marked as read using
	 * {@link #markAsRead()}.
	 * 
	 * @return {@code true} if this message of the day has been marked as read
	 *         using {@link #markAsRead()} and {@code false} otherwise.
	 * @throws IOException
	 *             If the info that this message of the day is marked as read
	 *             cannot be written to the computers hard disk
	 * @throws ClassNotFoundException
	 *             This exception occurs if you did not add the
	 *             <a href="https://rometools.github.io/rome/index.html">ROME
	 *             library</a> to your classpath. If you use maven don't worry
	 *             about this exception as maven already added the library for
	 *             you.
	 */
	public boolean isMarkedAsRead() throws IOException, ClassNotFoundException {
		List<SyndEntry> entryList = new ArrayList<>();

		for (File file : getSerializedMOTFiles()) {
			FileInputStream fileIn = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			entryList.add((SyndEntry) in.readObject());
			in.close();
			fileIn.close();
		}

		return entryList.contains(entry);
	}

	/**
	 * Returns the file name to be used for serializing a new {@code MOTD}
	 * 
	 * @return The file name to be used for serializing a new {@code MOTD}
	 */
	private static String getNextSerializationFileName() {
		List<Integer> usedIndexes = getUsedIndexes();

		int maxUsedIndex;

		try {
			maxUsedIndex = Collections.max(usedIndexes);
		} catch (Exception e) {
			maxUsedIndex = -1;
		}

		return latestMOTDSerializedFileName.replace("{index}", Integer.toString(maxUsedIndex + 1));
	}

	/**
	 * Returns a list of indexes already used by serialized {@code MOTD}s
	 * 
	 * @return A list of indexes already used by serialized {@code MOTD}s
	 */
	private static List<Integer> getUsedIndexes() {
		File folder = new File(Common.getAndCreateAppDataPath() + latestMOTDSerializedFilePath);
		List<Integer> res = new ArrayList<>();

		for (File file : folder.listFiles()) {
			if (file.isFile() && file.getName().matches(getRegexToMatchSerializedMOTs())) {
				res.add(getIndexFromFilename(file.getName()));
			}
		}

		return res;
	}

	/**
	 * Returns a list of files that contain serialized {@code MOTD}s
	 * 
	 * @return A list of files that contain serialized {@code MOTD}s
	 */
	public static List<File> getSerializedMOTFiles() {
		File folder = new File(Common.getAndCreateAppDataPath() + latestMOTDSerializedFilePath);
		folder.mkdirs();
		List<File> res = new ArrayList<>();

		for (File file : folder.listFiles()) {
			if (file.isFile() && file.getName().matches(getRegexToMatchSerializedMOTs())) {
				res.add(file);
			}
		}

		return res;
	}

	/**
	 * Returns the index used in the given file name of a serialized
	 * {@code MOTD}
	 * 
	 * @param fileName
	 *            The file name to be analyzed
	 * @return The index used in the given file name of a serialized
	 *         {@code MOTD}
	 */
	private static int getIndexFromFilename(String fileName) {
		// remove the beginning of the file name
		String resStr = fileName.replace(
				latestMOTDSerializedFileName.substring(0, latestMOTDSerializedFileName.indexOf("{index}")), "");

		// remove the end of the file name
		resStr = resStr.replace(
				latestMOTDSerializedFileName.substring(latestMOTDSerializedFileName.indexOf("{index}") + 7), "");

		return Integer.parseInt(resStr);
	}

	/**
	 * Returns the regex to be used to match a file name of a serialized
	 * {@code MOTD}
	 * 
	 * @return The regex to be used to match a file name of a serialized
	 *         {@code MOTD}
	 */
	public static String getRegexToMatchSerializedMOTs() {
		return latestMOTDSerializedFileName.replaceAll("\\.", "\\\\.").replace("{index}", "\\d+");
	}

	/**
	 * Gets the latest {@link MOTD} from the specified RSS-feed
	 * 
	 * @param feedUrl
	 *            The {@code URL} of the RSS-feed to get the {@link MOTD} from.
	 * @return The latest {@link MOTD} from the specified RSS-feed
	 * @throws IllegalArgumentException
	 *             thrown if feed type could not be understood by any of the
	 *             underlying parsers.
	 * @throws FeedException
	 *             if the feed could not be parsed
	 * @throws IOException
	 *             thrown if there is a problem reading the stream of the URL.
	 */
	public static MOTD getLatestMOTD(URL feedUrl) throws IllegalArgumentException, FeedException, IOException {
		FOKLogger.info(MOTD.class.getName(), "Retreiving latest MOTD from url " + feedUrl.toString());
		SyndFeed feed = (new SyndFeedInput()).build(new XmlReader(feedUrl));
		return new MOTD(feed.getImage(), feed.getTitle(), feed.getEntries().get(0));
	}

}
