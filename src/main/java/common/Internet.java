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


import logging.FOKLogger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

/**
 * All custom internet functions
 * 
 * @author Frederik Kammel
 *
 */
public class Internet {
	/**
	 * Sends an event to the IFTTT Maker Channel. See
	 * <a href="https://maker.ifttt.com/use/">https://maker.ifttt.com/use/</a>
	 * for more information.
	 * 
	 * @param IFTTTMakerChannelApiKey
	 *            Your Maker API Key. Get your one on
	 *            <a href="https://ifttt.com/maker">https://ifttt.com/maker</a>
	 * @param eventName
	 *            The name of the event to trigger.
	 * @return The response text from IFTTT
	 * @throws IOException
	 *             Should actually never be thrown but occurs if something is
	 *             wrong with the connection (e. g. not connected)
	 */
	public static String sendEventToIFTTTMakerChannel(String IFTTTMakerChannelApiKey, String eventName)
			throws IOException {
		return sendEventToIFTTTMakerChannel(IFTTTMakerChannelApiKey, eventName, "");
	}

	/**
	 * Sends an event to the IFTTT Maker Channel. See
	 * <a href="https://maker.ifttt.com/use/">https://maker.ifttt.com/use/</a>
	 * for more information.
	 * 
	 * @param IFTTTMakerChannelApiKey
	 *            Your Maker API Key. Get your one on
	 *            <a href="https://ifttt.com/maker">https://ifttt.com/maker</a>
	 * @param eventName
	 *            The name of the event to trigger.
	 * @param Details1
	 *            You can send up to three additional fields to the MAker
	 *            channel which you can use then as IFTTT ingredients. See
	 *            <a href=
	 *            "https://maker.ifttt.com/use/">https://maker.ifttt.com/use/</a>
	 *            for more information.
	 * @return The response text from IFTTT
	 * @throws IOException
	 *             Should actually never be thrown but occurs if something is
	 *             wrong with the connection (e. g. not connected)
	 */
	public static String sendEventToIFTTTMakerChannel(String IFTTTMakerChannelApiKey, String eventName, String Details1)
			throws IOException {
		return sendEventToIFTTTMakerChannel(IFTTTMakerChannelApiKey, eventName, Details1, "");
	}

	/**
	 * Sends an event to the IFTTT Maker Channel. See
	 * <a href="https://maker.ifttt.com/use/">https://maker.ifttt.com/use/</a>
	 * for more information.
	 * 
	 * @param IFTTTMakerChannelApiKey
	 *            Your Maker API Key. Get your one on
	 *            <a href="https://ifttt.com/maker">https://ifttt.com/maker</a>
	 * @param eventName
	 *            The name of the event to trigger.
	 * @param Details1
	 *            You can send up to three additional fields to the MAker
	 *            channel which you can use then as IFTTT ingredients. See
	 *            <a href=
	 *            "https://maker.ifttt.com/use/">https://maker.ifttt.com/use/</a>
	 *            for more information.
	 * @param Details2
	 *            The second additional parameter.
	 * @return The response text from IFTTT
	 * @throws IOException
	 *             Should actually never be thrown but occurs if something is
	 *             wrong with the connection (e. g. not connected)
	 */
	public static String sendEventToIFTTTMakerChannel(String IFTTTMakerChannelApiKey, String eventName, String Details1,
			String Details2) throws IOException {
		return sendEventToIFTTTMakerChannel(IFTTTMakerChannelApiKey, eventName, Details1, Details2, "");
	}

	/**
	 * Sends an event to the IFTTT Maker Channel. See
	 * <a href="https://maker.ifttt.com/use/">https://maker.ifttt.com/use/</a>
	 * for more information.
	 * 
	 * @param IFTTTMakerChannelApiKey
	 *            Your Maker API Key. Get your one on
	 *            <a href="https://ifttt.com/maker">https://ifttt.com/maker</a>
	 * @param eventName
	 *            The name of the event to trigger.
	 * @param Details1
	 *            You can send up to three additional fields to the MAker
	 *            channel which you can use then as IFTTT ingredients. See
	 *            <a href=
	 *            "https://maker.ifttt.com/use/">https://maker.ifttt.com/use/</a>
	 *            for more information.
	 * @param Details2
	 *            The second additional parameter.
	 * @param Details3
	 *            The third additional parameter.
	 * @return The response text from IFTTT
	 * @throws IOException
	 *             Should actually never be thrown but occurs if something is
	 *             wrong with the connection (e. g. not connected)
	 */
	public static String sendEventToIFTTTMakerChannel(String IFTTTMakerChannelApiKey, String eventName, String Details1,
			String Details2, String Details3) throws IOException {
		HttpURLConnection connection = null;
		String response = "";

		URL url;
		try {
			url = new URL("https://maker.ifttt.com/trigger/" + eventName + "/with/key/" + IFTTTMakerChannelApiKey);
			String postData = "{ \"value1\" : \"" + Details1 + "\", \"value2\" : \"" + Details2 + "\", \"value3\" : \""
					+ Details3 + "\" }";
			byte[] postData2 = postData.getBytes(StandardCharsets.UTF_8);

			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("charset", "utf-8");
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			// ByteStringConverter bs = new ByteStringConverter();

			wr.write(postData2);
			connection.connect();

			Reader in;

			in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			for (int c; (c = in.read()) >= 0;) {
				response = response + Character.toString((char) c);
			}
			return response;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			FOKLogger.log(Internet.class.getName(), Level.SEVERE, "An error occured!", e);
			return "";
		}

	}

	/**
	 * Sends a GET request to url and retreives the server response
	 * 
	 * @param url
	 *            The url to call.
	 * @return The server response
	 * @throws IOException
	 *             No Internet connection
	 */
	public static String webread(URL url) throws IOException {
		StringBuilder result = new StringBuilder();
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		rd.close();
		return result.toString();
	}

	/**
	 * Checks if the computer is connected to the internet by calling
	 * {@code http://www.google.com}
	 * 
	 * @return {@code true} if the computer is connected to the internet,
	 *         {@code false} otherwise.
	 */
	public static boolean isConnected() {
		try {
			return isConnected(new URL("https://www.google.com"));
		} catch (MalformedURLException e) {
			// Just in case I made a typo in the hardcoded value...
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Checks if the computer is connected to the internet by calling
	 * {@code urlToTest}
	 * 
	 * @param urlToTest
	 *            The url to be used to test the connection. It is recommended
	 *            to use a reliable server (like Google) for this to ensure that
	 *            the server is always online.
	 * @return {@code true} if the computer is connected to the internet,
	 *         {@code false} otherwise.
	 */
	public static boolean isConnected(URL urlToTest) {
		try {
			webread(urlToTest);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}
