package com.github.vatbub.common.core;

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


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class StringCommon {

    public static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
    }

    /**
     * Reads the text from a text file
     *
     * @param file The file to read
     * @return The text in that text file
     * @throws IOException if the file cannot be read
     */
    public static String fromFile(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        while (line != null) {
            sb.append(line);
            line = br.readLine();
            if (line != null)
                sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    public static List<String> formatMessage(List<String> messageToFormat) {
        ArrayList<String> res = new ArrayList<>(messageToFormat.size() + 2);
        // get the length of the longest line
        int maxLength = messageToFormat.get(0).length();
        for (String line : messageToFormat) {
            if (line.length() > maxLength)
                maxLength = line.length();
        }

        StringBuilder separator = new StringBuilder();
        for (int i = 0; i < maxLength + 4; i++) {
            separator.append("#");
        }

        res.add(separator.toString());
        for (String line : messageToFormat) {
            res.add("# " + line + getRequiredSpaces(separator.toString(), line) + " #");
        }
        res.add(separator.toString());

        return res;
    }

    /**
     * Formats a message to be printed on the console
     *
     * @param message The line to be formatted
     * @return The formatted version of {@code message}
     */
    private static String getRequiredSpaces(String reference, String message) {
        StringBuilder res = new StringBuilder();
        int requiredSpaces = reference.length() - message.length() - 4;

        for (int i = 0; i < requiredSpaces; i++) {
            res.append(" ");
        }

        return res.toString();
    }

    public static int countOccurrencesInString(String stringToSearch, String searchString) {
        return (stringToSearch.length() - stringToSearch.replace(searchString, "").length()) / searchString.length();
    }

    public static String convertFileSizeToReadableString(double kilobytes) {
        ResourceBundle bundle = ResourceBundle.getBundle("com.github.vatbub.common.core.filesizeUnitStrings");
        String res;
        if (kilobytes < 1024) {
            res = Double.toString(Math.round(kilobytes * 100.0) / 100.0) + " "
                    + bundle.getString("kilobyte");
        } else if ((kilobytes / 1024) < 1024) {
            res = Double.toString(Math.round((kilobytes * 100.0) / 1024) / 100.0)
                    + " " + bundle.getString("megabyte");
        } else if (((kilobytes / 1024) / 1024) < 1024) {
            res = Double
                    .toString(Math.round(((kilobytes * 100.0) / 1024) / 1024) / 100.0) + " "
                    + bundle.getString("gigabyte");
        } else {
            res = Double
                    .toString(Math.round((((kilobytes * 100.0) / 1024) / 1024) / 1024) / 100.0)
                    + " " + bundle.getString("terabyte");
        }
        return res;
    }
}
