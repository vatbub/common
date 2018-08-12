package com.github.vatbub.common.internet;

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


import com.github.vatbub.common.core.Common;
import com.github.vatbub.common.core.logging.FOKLogger;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.logging.Level;

/**
 * All custom common.internet functions
 *
 * @author Frederik Kammel
 */
@SuppressWarnings("SameParameterValue")
public class Internet {
    private Internet() {
        throw new IllegalStateException("Class may not be instantiated");
    }

    /**
     * Sends an event to the IFTTT Maker Channel. See
     * <a href="https://maker.ifttt.com/use/">https://maker.ifttt.com/use/</a>
     * for more information.
     *
     * @param IFTTTMakerChannelApiKey Your Maker API Key. Get your one on
     *                                <a href="https://ifttt.com/maker">https://ifttt.com/maker</a>
     * @param eventName               The name of the event to trigger.
     * @return The response text from IFTTT
     * @throws IOException Should actually never be thrown but occurs if something is
     *                     wrong with the connection (e. g. not connected)
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
     * @param IFTTTMakerChannelApiKey Your Maker API Key. Get your one on
     *                                <a href="https://ifttt.com/maker">https://ifttt.com/maker</a>
     * @param eventName               The name of the event to trigger.
     * @param details1                You can send up to three additional fields to the Maker
     *                                channel which you can use then as IFTTT ingredients. See
     *                                <a href=
     *                                "https://maker.ifttt.com/use/">https://maker.ifttt.com/use/</a>
     *                                for more information.
     * @return The response text from IFTTT
     * @throws IOException Should actually never be thrown but occurs if something is
     *                     wrong with the connection (e. g. not connected)
     */
    public static String sendEventToIFTTTMakerChannel(String IFTTTMakerChannelApiKey, String eventName, String details1)
            throws IOException {
        return sendEventToIFTTTMakerChannel(IFTTTMakerChannelApiKey, eventName, details1, "");
    }

    /**
     * Sends an event to the IFTTT Maker Channel. See
     * <a href="https://maker.ifttt.com/use/">https://maker.ifttt.com/use/</a>
     * for more information.
     *
     * @param IFTTTMakerChannelApiKey Your Maker API Key. Get your one on
     *                                <a href="https://ifttt.com/maker">https://ifttt.com/maker</a>
     * @param eventName               The name of the event to trigger.
     * @param details1                You can send up to three additional fields to the Maker
     *                                channel which you can use then as IFTTT ingredients. See
     *                                <a href=
     *                                "https://maker.ifttt.com/use/">https://maker.ifttt.com/use/</a>
     *                                for more information.
     * @param details2                The second additional parameter.
     * @return The response text from IFTTT
     * @throws IOException Should actually never be thrown but occurs if something is
     *                     wrong with the connection (e. g. not connected)
     */
    public static String sendEventToIFTTTMakerChannel(String IFTTTMakerChannelApiKey, String eventName, String details1,
                                                      String details2) throws IOException {
        return sendEventToIFTTTMakerChannel(IFTTTMakerChannelApiKey, eventName, details1, details2, "");
    }

    /**
     * Sends an event to the IFTTT Maker Channel. See
     * <a href="https://maker.ifttt.com/use/">https://maker.ifttt.com/use/</a>
     * for more information.
     *
     * @param IFTTTMakerChannelApiKey Your Maker API Key. Get your one on
     *                                <a href="https://ifttt.com/maker">https://ifttt.com/maker</a>
     * @param eventName               The name of the event to trigger.
     * @param details1                You can send up to three additional fields to the Maker
     *                                channel which you can use then as IFTTT ingredients. See
     *                                <a href=
     *                                "https://maker.ifttt.com/use/">https://maker.ifttt.com/use/</a>
     *                                for more information.
     * @param details2                The second additional parameter.
     * @param details3                The third additional parameter.
     * @return The response text from IFTTT
     * @throws IOException Should actually never be thrown but occurs if something is
     *                     wrong with the connection (e. g. not connected)
     */
    public static String sendEventToIFTTTMakerChannel(String IFTTTMakerChannelApiKey, String eventName, String details1,
                                                      String details2, String details3) throws IOException {
        HttpURLConnection connection;
        StringBuilder response = new StringBuilder();

        URL url;
        try {
            url = new URL("https://maker.ifttt.com/trigger/" + eventName + "/with/key/" + IFTTTMakerChannelApiKey);
            String postData = "{ \"value1\" : \"" + details1 + "\", \"value2\" : \"" + details2 + "\", \"value3\" : \""
                    + details3 + "\" }";
            byte[] postData2 = postData.getBytes(StandardCharsets.UTF_8);

            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("charset", "utf-8");
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

            wr.write(postData2);
            connection.connect();

            Reader in;

            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            for (int c; (c = in.read()) >= 0; ) {
                response.append(Character.toString((char) c));
            }
            return response.toString();
        } catch (MalformedURLException e) {
            FOKLogger.log(Internet.class.getName(), Level.SEVERE, "An error occurred!", e);
            return "";
        }

    }

    /**
     * Sends a GET request to url and retrieves the server response
     *
     * @param url The url to call.
     * @return The server response
     * @throws IOException No Internet connection
     */
    @SuppressWarnings("UnusedReturnValue")
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
     * Checks if the computer is connected to the common.internet by calling
     * {@code http://www.google.com}
     *
     * @return {@code true} if the computer is connected to the common.internet,
     * {@code false} otherwise.
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
     * Checks if the computer is connected to the common.internet by calling
     * {@code urlToTest}
     *
     * @param urlToTest The url to be used to test the connection. It is recommended
     *                  to use a reliable server (like Google) for this to ensure that
     *                  the server is always online.
     * @return {@code true} if the computer is connected to the common.internet,
     * {@code false} otherwise.
     */
    public static boolean isConnected(URL urlToTest) {
        try {
            webread(urlToTest);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Sends a error message via gMail. This method requires a gMail account to send emails from. Get a new account <a href="https://accounts.google.com/SignUp?continue=https%3A%2F%2Fwww.google.com%2F%3Fgfe_rd%3Dcr%26ei%3D30aJWLDMDrP08Af3oLrwDg%26gws_rd%3Dssl&hl=en">here</a>
     *
     * @param phase         The phase in which the error occurred. This can be any string that helps you to identify the part of the code where the exception happened
     * @param e             The exception that occurred
     * @param gMailUsername The username of the gMail-account to use to send the mail from, including {@code {@literal @}gmail.com}
     * @param gMailPassword The password of the gMail-account
     */

    public static void sendErrorMail(String phase, Throwable e, String gMailUsername, String gMailPassword) {
        sendErrorMail(phase, null, e, gMailUsername, gMailPassword);
    }

    /**
     * Sends a error message via gMail. This method requires a gMail account to send emails from. Get a new account <a href="https://accounts.google.com/SignUp?continue=https%3A%2F%2Fwww.google.com%2F%3Fgfe_rd%3Dcr%26ei%3D30aJWLDMDrP08Af3oLrwDg%26gws_rd%3Dssl&hl=en">here</a>
     *
     * @param phase         The phase in which the error occurred. This can be any string that helps you to identify the part of the code where the exception happened.
     * @param requestBody   The body of the http request that caused the exception
     * @param e             The exception that occurred
     * @param gMailUsername The username of the gMail-account to use to send the mail from, including {@code {@literal @}gmail.com}
     * @param gMailPassword The password of the gMail-account
     */
    public static void sendErrorMail(String phase, String requestBody, Throwable e, String gMailUsername, String gMailPassword) {
        final String toAddress = "vatbub123+automatederrorreports@gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                        return new javax.mail.PasswordAuthentication(gMailUsername, gMailPassword);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(gMailUsername));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toAddress));
            message.setSubject("[" + Common.getInstance().getAppName() + "] An error occurred in your application");

            String messageText = "Exception occurred in phase: " + phase;
            if (requestBody != null) {
                messageText = messageText + "\n\nRequest that caused the exception:\n" + requestBody;
            }

            messageText = messageText + "\n\nStacktrace of the exception:\n" + ExceptionUtils.getFullStackTrace(e);

            message.setText(messageText);

            Transport.send(message);

            System.out.println("Sent email with error message to " + toAddress);

        } catch (MessagingException e2) {
            throw new RuntimeException(e2);
        }
    }

    /**
     * Returns the reason for the given http code as a human-readable string. Reasons taken from <a href="https://de.wikipedia.org/wiki/HTTP-Statuscode">Wikipedia (German)</a>
     *
     * @param httpCode The http code to get the reason for
     * @return The reason for the given http code as a human-readable string or {@code null}, if the given http code is unknown
     */
    public static String getReasonForHTTPCode(int httpCode) {
        switch (httpCode) {
            case 100:
                return "Continue";
            case 101:
                return "Switching Protocols";
            case 102:
                return "Processing";
            case 200:
                return "OK";
            case 201:
                return "Created";
            case 202:
                return "Accepted";
            case 203:
                return "Non-Authoritative Information";
            case 204:
                return "No Content";
            case 205:
                return "Reset Content";
            case 206:
                return "Partial Content";
            case 207:
                return "Multi-Status";
            case 208:
                return "Already Reported";
            case 226:
                return "IM Used";
            case 300:
                return "Multiple Choices";
            case 301:
                return "Moved Permanently";
            case 302:
                return "Moved Temporarily";
            case 303:
                return "See Other";
            case 304:
                return "Not Modified";
            case 305:
                return "Use Proxy";
            case 307:
                return "Temporary Redirect";
            case 308:
                return "Permanent Redirect";
            case 400:
                return "Bad Request";
            case 401:
                return "Unauthorized";
            case 402:
                return "Payment Required";
            case 403:
                return "Forbidden";
            case 404:
                return "Not Found";
            case 405:
                return "Method Not Allowed";
            case 406:
                return "Not Acceptable";
            case 407:
                return "Proxy Authentication Required";
            case 408:
                return "Request Time-out";
            case 409:
                return "Conflict";
            case 410:
                return "Gone";
            case 411:
                return "Length required";
            case 412:
                return "Precondition failed";
            case 413:
                return "Request Entity Too Large";
            case 414:
                return "Request-URL Too Long";
            case 415:
                return "UnsupportedMedia Type";
            case 416:
                return "Requested range not satisfiable";
            case 417:
                return "Expectation failed";
            case 418:
                return "I'm a teapot";
            case 420:
                return "Policy Not Fulfilled";
            case 421:
                return "Misdirected Request";
            case 422:
                return "Unprocessable Entity";
            case 423:
                return "Locked";
            case 424:
                return "Failed Dependency";
            case 425:
                return "Unordered Collection";
            case 426:
                return "Upgrade Required";
            case 428:
                return "Precondition Required";
            case 429:
                return "Too Many Requests";
            case 431:
                return "Request Header Fields Too Large";
            case 451:
                return "Unavailable For Legal Reasons";
            case 500:
                return "Internal Server Error";
            case 501:
                return "Not Implemented";
            case 502:
                return "Bad Gateway";
            case 503:
                return "Service Unavailable";
            case 504:
                return "Gateway Time-out";
            case 505:
                return "HTTP Version not supported";
            case 506:
                return "Variant Also Negotiates";
            case 507:
                return "Insufficient Storage";
            case 508:
                return "Loop Detected";
            case 509:
                return "Bandwidth Limit Exceeded";
            case 510:
                return "Not Extended";
            case 511:
                return "Network Authentication Required";
            default:
                return null;
        }
    }

    /**
     * OPens the specified url in the default browser.
     *
     * @param url The url to open
     * @throws IOException If the URL cannot be opened
     */

    public static void openInDefaultBrowser(URL url) throws IOException {
        Runtime rt = Runtime.getRuntime();

        if (SystemUtils.IS_OS_WINDOWS) {
            rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
        } else if (SystemUtils.IS_OS_MAC) {
            rt.exec("open" + url);
        } else {
            String[] browsers = {"epiphany", "firefox", "mozilla", "konqueror",
                    "netscape", "opera", "links", "lynx"};

            StringBuilder cmd = new StringBuilder();
            for (int i = 0; i < browsers.length; i++)
                cmd.append(i == 0 ? "" : " || ").append(browsers[i]).append(" \"").append(url).append("\" ");

            rt.exec(new String[]{"sh", "-c", cmd.toString()});
        }
    }
}
