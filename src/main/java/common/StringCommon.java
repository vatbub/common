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


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class StringCommon {

    public static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)(.*)" + regex, "$1" + replacement);
    }

    /**
     * Reads the text from a text file
     *
     * @param file The file to read
     * @return The text in that text file
     */
    public static String fromFile(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        while (line != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
            line = br.readLine();
        }
        return sb.toString();
    }

    public static String[] formatMessage(String[] messageToFormat) {
        ArrayList<String> res = new ArrayList<>(messageToFormat.length + 2);
        // get the length of the longest line
        int maxLength = messageToFormat[0].length();
        for (String line : messageToFormat) {
            if (line.length() > maxLength)
                maxLength = line.length();
        }

        String separator = "";
        for (int i = 0; i < maxLength + 2; i++) {
            separator = separator + "#";
        }

        res.add(separator);
        for (String line : messageToFormat) {
            res.add("# " + line + getRequiredSpaces(line) + " #");
        }
        res.add(separator);

        return (String[]) res.toArray();
    }

    /**
     * Formats a message to be printed on the console
     *
     * @param message The line to be formatted
     * @return The formatted version of {@code message}
     */
    private static String getRequiredSpaces(String message) {
        String res = "";
        final String reference = "#########################################################################";
        int requiredSpaces = reference.length() - message.length() - 4;

        for (int i = 0; i < requiredSpaces; i++) {
            res = res + " ";
        }

        return res;
    }
}
