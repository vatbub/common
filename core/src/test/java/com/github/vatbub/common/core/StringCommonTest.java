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


import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class StringCommonTest {

    @Test
    public void replaceLastTest() {
        String testStr = "BlablubBla";
        assert StringCommon.replaceLast(testStr, "Bla", "").equals("Blablub");
    }

    @Test
    public void fromFileTest() throws URISyntaxException, IOException {
        File testFile = new File(getClass().getResource("testString.txt").toURI());
        Assert.assertEquals(FileUtils.readFileToString(testFile, "UTF-8"), StringCommon.fromFile(testFile));
    }

    @Test
    public void formatMessageTest() {
        List<String> testMessage = new ArrayList<>();
        testMessage.add("line 1");
        testMessage.add("is very interesting");
        testMessage.add("each line should have");
        testMessage.add("at least one space");
        testMessage.add("before and after it");

        List<String> formattedMessage = StringCommon.formatMessage(testMessage);
        Assert.assertEquals(testMessage.size() + 2, formattedMessage.size());

        for (String line : formattedMessage)
            System.out.println(line);

        String testRegex = "#*";
        Assert.assertTrue(formattedMessage.get(0).matches(testRegex));
        Assert.assertTrue(formattedMessage.get(formattedMessage.size() - 1).matches(testRegex));

        for (int i = 1; i < formattedMessage.size()-1; i++) {
            Assert.assertTrue(formattedMessage.get(i).matches("# " + testMessage.get(i - 1) + " *#"));
        }
    }
}
