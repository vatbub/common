package com.github.vatbub.common.view.motd;

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
import com.rometools.rome.io.FeedException;
import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;

@Ignore
public class MOTDTest {

    private static final String appName = "fokprojectUnitTests";
    private static URL testFeedURL;
    private static URL testEmptyFeedURL;

    @Before
    public void prepare() throws MalformedURLException {
        Common.getInstance().setAppName(appName);
        testFeedURL = new URL("https://fredplus10.me/de/feed/");
        testEmptyFeedURL = new URL("https://fredplus10.me/oiuztrfgrdesfg/feed/");
    }

    @Test
    public void getEmptyFeedTest() throws IllegalArgumentException, FeedException, IOException {
        MOTD motd = MOTD.getLatestMOTD(testEmptyFeedURL);

        Assert.assertNull(motd);
    }

    @Test
    public void getMOTDTest() throws IllegalArgumentException, FeedException, IOException, ClassNotFoundException {
        MOTD motd = MOTD.getLatestMOTD(testFeedURL);

        Assert.assertNotNull(motd);
        Assert.assertFalse(motd.isMarkedAsRead());
        Assert.assertNotNull(motd.getEntry());
        Assert.assertNotNull(motd.getFeedTitle());
        Assert.assertNotNull(motd.getImage());

        // mark as read
        motd.markAsRead();
        Assert.assertTrue(motd.isMarkedAsRead());
    }

    @After
    public void cleanUp() throws IOException {
        for (File f : MOTD.getSerializedMOTFiles()) {
            Files.delete(f.toPath());
        }
    }
}
